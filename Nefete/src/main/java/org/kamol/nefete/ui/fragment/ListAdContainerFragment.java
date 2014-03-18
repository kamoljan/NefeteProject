package org.kamol.nefete.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etsy.android.grid.StaggeredGridView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;
import org.kamol.nefete.R;
import org.kamol.nefete.adapter.PinterestAdapter;
import org.kamol.nefete.http.GoRestClient;

import java.util.ArrayList;

public class ListAdContainerFragment extends Fragment {
    private static final String TAG = "ListAdContainerFragment";
    public static final String SAVED_DATA_KEY = "SAVED_DATA";
    private static final int FETCH_DATA_TASK_DURATION = 2000;

    private StaggeredGridView mGridView;
    private PinterestAdapter mAdapter;
    private ArrayList<String> mData;

    public static ListAdContainerFragment newInstance() {
        return new ListAdContainerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_container_list_ad, container, false);
        mGridView = (StaggeredGridView) view.findViewById(R.id.grid_view);

        mGridView.setEmptyView(view.findViewById(android.R.id.empty));
//        mAdapter = new PinterestAdapter(getActivity(), R.id.txt_line1);
        mAdapter = new PinterestAdapter(getActivity(), R.id.iv_image);

        // TODO: Handle savedInstanceState
        // do we have saved data?
//        if (savedInstanceState != null) {
//            mData = savedInstanceState.getStringArrayList(SAVED_DATA_KEY);
//            fillAdapter();
//        }

//        if (mData == null) {
//            mData = SampleData.generateSampleData();
//        }

//        for (String data : mData) {
//            mAdapter.add(data);
//        }

        mGridView.setAdapter(mAdapter);
        // TODO: Add listener for OnScroll and OnItemClick to mGridView

        fetchData();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public class ListResult {
        private String status;
        private Result[] result;
        private String message;

        public ListResult() {
            this(null, null);
        }

        public ListResult(String s, Result[] r) {
            this.status = s;
            this.result = r;
        }

        public Result[] getResult() {
            return result;
        }

        public void setResult(Result[] result) {
            this.result = result;
        }
    }

    public class Result {
        private String title;
        private String price;
        private String image;
    }

    /*
    POST: http://localhost:8080/list?limit=20
    {
	    status: "OK"
	    result: [
	        0:{
	            title: "test"
	            price: 6468
	            image: "0001_72a53f664db6f415e9e862c607d9c0ba177c20af_655B4C_100_75"
	        ...
        ]
    }
    */
    private void fetchData() {
        RequestParams p = new RequestParams();
        p.put("limit", 20);
        GoRestClient.post(":8080/list", p, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Log.d(TAG, jsonObject.toString());
                Gson gson = new GsonBuilder().create();
                ListResult listResult = gson.fromJson(jsonObject.toString(), ListResult.class);
                if (listResult.status.equals("OK")) { //if not found it will returns "ERROR"
                    String imgUrl = "";
                    for (int i = 0; i < listResult.getResult().length; i++) {
                        imgUrl = listResult.getResult()[i].image;
                        Log.d(TAG, "i = " + imgUrl);
                        if (imgUrl.length() != 0) {
                            mAdapter.add(imgUrl);
                        }
                    }
                }
            }
        });
    }
}