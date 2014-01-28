package org.kamol.nefete.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.kamol.nefete.R;
import org.kamol.nefete.adapter.InsertAdImageAdapter;

public class InsertAdFragment extends Fragment {
    private static final String TAG = "InsertAdFragment";

    public static InsertAdFragment newInstance() {
        return new InsertAdFragment();
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
                Toast.makeText(view.getContext(), "Currency = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setGvImagesContent(View view) {
        InsertAdImageAdapter insertAdImageAdapter = new InsertAdImageAdapter();
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
                Toast.makeText(view.getContext(), "Hello :)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSpCategoryContent(View view) {
        Spinner spCategory = (Spinner) view.findViewById(R.id.sp_category);
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(), "Category = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}