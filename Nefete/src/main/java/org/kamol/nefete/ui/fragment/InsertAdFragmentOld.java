package org.kamol.nefete.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import org.kamol.nefete.R;
import org.kamol.nefete.adapter.InsertAdImageAdapter;


public class InsertAdFragmentOld extends Fragment implements ImageChooserDialogFragment.OnImageChooserDialogListener, ImageChooserListener {
    private static final String TAG = "InsertAdFragment";

    private ImageChooserManager imageChooserManager;
    private ProgressBar pbar;
    private int chooserType;
    private String filePath;

    @Override
    public void onError(String reason) {
        Toast.makeText(getActivity(), reason, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //pbar.setVisibility(View.GONE);
                if (image != null) {
                    Toast.makeText(getActivity(), image.getFilePathOriginal(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Image is null", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_PICK_PICTURE, "myfolder", true);
        imageChooserManager.setImageChooserListener(InsertAdFragmentOld.this);
        try {
            //pbar.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
            Toast.makeText(getActivity(), "filePath = " + filePath, Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_CAPTURE_PICTURE, "myfolder", true);
        imageChooserManager.setImageChooserListener(this);
        try {
            //pbar.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
            Toast.makeText(getActivity(), "filePath = " + filePath, Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCloseDialog(int item) {
        Toast.makeText(getActivity(), "Item = " + item, Toast.LENGTH_SHORT).show();
        if (item == 0) {
            takePicture();
        } else {
            chooseImage();
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK
                && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
        } else {
            pbar.setVisibility(View.GONE);
        }
    }

    // Should be called if for some reason the ImageChooserManager is null (Due
    // to destroying of activity for low memory situations)
    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType, "myfolder", true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insert_ad, container, false);
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
        InsertAdImageAdapter insertAdImageAdapter = new InsertAdImageAdapter(getActivity());
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
                imgDialog.setOnImageChooserDialogListener(InsertAdFragmentOld.this);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("chooser_type", chooserType);
        outState.putString("media_path", filePath);
        super.onSaveInstanceState(outState);
    }
}