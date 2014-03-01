package org.kamol.nefete.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.otto.Subscribe;

import org.kamol.nefete.R;
import org.kamol.nefete.adapter.InsertAdImageAdapter;
import org.kamol.nefete.bus.BusProvider;
import org.kamol.nefete.event.ActivityResultEvent;
import org.kamol.nefete.http.GoRestClient;

public class InsertAdFragment extends Fragment implements ImageChooserDialogFragment.OnImageChooserDialogListener {
    private static final String TAG = "InsertAdFragment";

    private InsertAdImageAdapter insertAdImageAdapter;
    private static final int REQUEST_TAKE = 1888;
    private static final int REQUEST_BROWSE = 1999;
    private static final int THUMBNAIL_SIZE = 500;

    @Override
    public void onCloseDialog(int item) {
        if (item == 0) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = getTempImageFile();
            try {
                file.getParentFile().mkdirs();
                if (!file.getParentFile().exists()) throw new IOException("mkdirs() failed.");
                file.createNewFile();
                if (!file.exists()) throw new IOException("createNewFile() failed.");
                Uri imageUri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQUEST_TAKE);
            } catch (IOException e) {
                Toast.makeText(getActivity(), "Picture Error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, e.toString());
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_BROWSE);
        }
    }

    private File getTempImageFile() {
        return new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + getActivity().getPackageName() + "/cache/insert_ad.jpg");
    }

    @Subscribe
    public void onActivityResultEvent(ActivityResultEvent event) {
        if (event.resultCode != Activity.RESULT_OK) return;
        Uri imageUri = null;

        switch (event.requestCode) {
            case REQUEST_TAKE: // data = null
                // REQUEST_TAKE (file://)
                File file = getTempImageFile();
                imageUri = Uri.fromFile(file);
                break;
            case REQUEST_BROWSE:
                // REQUEST_BROWSE (content:// or file://)
                imageUri = event.data.getData();
                break;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            Bitmap bitmap = getThumbnail(imageUri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] byteArray = stream.toByteArray();
        try {
            putImage(byteArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = getActivity().getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        if (input != null) {
            input.close();
        }
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = getActivity().getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        if (input != null) {
            input.close();
        }
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
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