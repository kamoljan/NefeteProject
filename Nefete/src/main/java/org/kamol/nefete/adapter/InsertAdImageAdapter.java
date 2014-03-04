package org.kamol.nefete.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ViewAnimator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.squareup.picasso.Picasso;

import org.kamol.nefete.R;

public class InsertAdImageAdapter extends BaseAdapter {
    private static final int DEFAULT_MAX_COUNT = 3;
    private List<String> imageIds = new ArrayList<String>(DEFAULT_MAX_COUNT);
    private int maxCount = DEFAULT_MAX_COUNT;
    private ViewHolder holder;
    private final Context context;

    public InsertAdImageAdapter(Context context) {
        this.context = context;
    }

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

    static class ViewHolder {
        ImageView iv_image;
        ViewAnimator va_animator;
        ImageView iv_empty;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.insert_ad_image, parent, false);
            holder = new ViewHolder();
            holder.iv_image = (ImageView) view.findViewById(R.id.iv_thumb);
            holder.va_animator = (ViewAnimator) view.findViewById(R.id.va_animator);
            holder.iv_empty = (ImageView) view.findViewById(R.id.iv_empty);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (position <= getRealCount()) {
            holder.va_animator.setDisplayedChild(0); // show default or real image
            if (position < getRealCount()) {
                holder.va_animator.setDisplayedChild(0); // show progress bar
                String url = imageIds.get(position);
                // Trigger the download of the URL asynchronously into the image view.
                Picasso.with(context)
                        .load(url)
                        .placeholder(android.R.drawable.ic_menu_camera)
                        .error(android.R.drawable.ic_input_delete)
                        .resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
                        .centerInside()
                        .into(holder.iv_image);
            }
        } else {
            holder.va_animator.setDisplayedChild(2); // show empty box
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
