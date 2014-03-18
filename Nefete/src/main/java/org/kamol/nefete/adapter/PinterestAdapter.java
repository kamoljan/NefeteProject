package org.kamol.nefete.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import org.kamol.nefete.R;
import org.kamol.nefete.datasets.Ad;
import org.kamol.nefete.datasets.AdList;
import org.kamol.nefete.http.GoRestClient;

/**
 * PINTEREST ADAPTER
 */
public class PinterestAdapter extends ArrayAdapter<AdList> {

    private static final String TAG = "PinterestAdapter";

    static class ViewHolder {
        DynamicHeightImageView ivPicture;
        TextView tvPrice;
    }

    private final LayoutInflater mLayoutInflater;
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public PinterestAdapter(final Context context, final int textViewResourceId) {
        super(context, textViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder vh;
        AdList ad = getItem(position);
        String egg = ad.getImage(); //0001_c6ce48134c6e593ecff1b3216e82f611de1eb042_5C5958_400_300
        String[] parts = egg.split("_");
        int w = Integer.parseInt(parts[3]);
        int h = Integer.parseInt(parts[4]);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_sample, parent, false);
            vh = new ViewHolder();
            vh.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            vh.tvPrice.setText(ad.getCurrency() + ad.getPrice());
            vh.ivPicture = (DynamicHeightImageView) convertView.findViewById(R.id.iv_image);
            Picasso.with(getContext())
                    .load(GoRestClient.getAbsoluteUrl(":9090/egg/" + egg))
                    .into(vh.ivPicture);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        convertView.setBackgroundColor(Color.parseColor("#" + parts[2]));
        vh.ivPicture.setHeightRatio((double) h / w);
        return convertView;
    }
}