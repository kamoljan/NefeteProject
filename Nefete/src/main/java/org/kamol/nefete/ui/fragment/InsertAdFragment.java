package org.kamol.nefete.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Subscribe;

import org.kamol.nefete.R;
import org.kamol.nefete.adapter.InsertAdImageAdapter;
import org.kamol.nefete.bus.BusProvider;
import org.kamol.nefete.event.ActivityResultEvent;
import org.kamol.nefete.http.GoRestClient;


public class InsertAdFragment extends Fragment implements ImageChooserDialogFragment.OnImageChooserDialogListener {
    private static final String TAG = "InsertAdFragment";

    private InsertAdImageAdapter insertAdImageAdapter;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;

    @Override
    public void onCloseDialog(int item) {
        if (item == 0) {
            Toast.makeText(getActivity(), "item = " + item, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "item = " + item, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Subscribe
    public void onActivityResultEvent(ActivityResultEvent event) {
        if (event.requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (event.resultCode == Activity.RESULT_OK) {
                Bitmap bmp = (Bitmap) event.data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                try {
                    putImage(byteArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void putImage(byte[] data) throws JSONException {
        RequestParams params = new RequestParams();
        params.put("picture[image]", new ByteArrayInputStream(data));

        GoRestClient.put(":9090", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Log.d(TAG, jsonObject.toString());
                Gson gson = new GsonBuilder().create();
                Message mes = gson.fromJson(jsonObject.toString(), Message.class);
                if (mes.Status.equals("OK")) {
                    Toast.makeText(getActivity(), "Uploaded successfully", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, mes.Result.Origin);
                    Log.d(TAG, mes.Result.Newborn);
                    Log.d(TAG, mes.Result.Infant);
                    Log.d(TAG, mes.Result.Baby);
                    insertAdImageAdapter.addItem(GoRestClient.getAbsoluteUrl(":9090/egg/" + mes.Result.Baby));
                }
            }
        });
    }

    public class Message {
        /* Camel case, thus, Go! sends like that
         * we can change it by, `json:"name"` in Go! */
        public String Status;
        public Result Result;
        public class Result {
            private String Origin;
            private String Baby;
            private String Infant;
            private String Newborn;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_insert_ad, container, false);
        setGvImagesContent(view);
        setSpCurrencyContent(view);
        setSpCategoryContent(view);
        return view;
    }

    private void setSpCurrencyContent(View view) {
        Spinner spCurrency = (Spinner) view.findViewById(R.id.sp_currency);
        spCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(), "Currency = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setGvImagesContent(View view) {
        insertAdImageAdapter = new InsertAdImageAdapter(getActivity());
        GridView gvImages = (GridView) view.findViewById(R.id.gv_images);
        gvImages.setColumnWidth(GridView.AUTO_FIT);
        gvImages.setAdapter(insertAdImageAdapter);
        gvImages.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }
        });
        gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "position = " + position);
                ImageChooserDialogFragment imgDialog = new ImageChooserDialogFragment();
                imgDialog.setOnImageChooserDialogListener(InsertAdFragment.this);
                imgDialog.show(getFragmentManager(), "ImageChooserDialogFragment");
            }
        });
    }

    private void setSpCategoryContent(View view) {
        Spinner spCategory = (Spinner) view.findViewById(R.id.sp_category);
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(), "Category = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}