package org.kamol.nefete.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.kamol.nefete.R;


public class InsertAdImageAdapter extends BaseAdapter {
    private static final int DEFAULT_MAX_COUNT = 3;
    private List<String> imageIds = new ArrayList<String>(DEFAULT_MAX_COUNT);
    private int maxCount = DEFAULT_MAX_COUNT;

    @Override
    public int getCount() {
        return maxCount;
    }

    @Override
    public Object getItem(int position) {
        return imageIds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        View view;
        if (position <= getRealCount()) {
            view = LayoutInflater.from(context).inflate(R.layout.insert_ad_image, null);
            if (position < getRealCount()) {
                final ImageView ivThumb = (ImageView) view.findViewById(R.id.iv_thumb);
                //String url = ACInsertAdPictureChooser.getThumbUrl(imageIds.get(position));
                //ivThumb.setTag(url);
                ivThumb.setImageResource(android.R.drawable.ic_btn_speak_now);
            }
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.insert_ad_image_empty, null);
        }
        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        //return true;
        return (position <= getRealCount());
    }

    //@Override
    public Map<String, String> getState() {
        Map<String, String> params = new HashMap<String, String>();
        for (int i = 0; i < imageIds.size(); i++) {
            params.put("image_id" + i, imageIds.get(i));
        }
        return params;
    }

    //@Override
    public void setState(Map<String, String> params) {
        imageIds.clear();
        for (int i = 0; i < getCount(); i++) {
            String id = params.get("image_id" + i);
            if (id != null) {
                imageIds.add(id);
            }
        }
        notifyDataSetChanged();
    }

    //@Override
    public int setErrors(JSONObject errors) {
        // should never have errors
        return 0;
    }

    public int getRealCount() {
        return imageIds.size();
    }

    public void addItem(String imageId) {
        imageIds.add(imageId);
        notifyDataSetChanged();
    }

    public void setItem(int position, String imageId) {
        imageIds.set(position, imageId);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position < getRealCount()) {
            imageIds.remove(position);
        }
        notifyDataSetChanged();
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        while (getRealCount() > getMaxCount()) {
            removeItem(getRealCount() - 1);
        }
        notifyDataSetChanged();
    }
}
