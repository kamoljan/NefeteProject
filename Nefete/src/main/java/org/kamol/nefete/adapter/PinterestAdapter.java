package org.kamol.nefete.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import org.kamol.nefete.R;
import org.kamol.nefete.datasets.AdList;
import org.kamol.nefete.http.GoRestClient;
import org.kamol.nefete.ui.activity.ViewActivity;

/**
 * PINTEREST ADAPTER
 */
public class PinterestAdapter extends ArrayAdapter<AdList> {
  private static final String TAG = "PinterestAdapter";
  public final static String AD_ID = "org.kamol.nefete.AD_ID";

  static class ViewHolder {
    DynamicHeightImageView ivPicture;
    TextView tvPrice;
  }

  private final LayoutInflater mLayoutInflater;

  public PinterestAdapter(final Context context, final int textViewResourceId) {
    super(context, textViewResourceId);
    mLayoutInflater = LayoutInflater.from(context);
  }

  @Override
  public View getView(final int position, View convertView, final ViewGroup parent) {
    ViewHolder vh;
    //AdList ad = getItem(position);
    String egg = getItem(position).getImage();
    final String adId = getItem(position).getId();
    //0001_c6ce48134c6e593ecff1b3216e82f611de1eb042_5C5958_400_300
    String[] parts = egg.split("_");
    int w = Integer.parseInt(parts[3]);
    int h = Integer.parseInt(parts[4]);
    if (convertView == null) {
      convertView = mLayoutInflater.inflate(R.layout.list_item_sample, parent, false);
      vh = new ViewHolder();
      vh.ivPicture = (DynamicHeightImageView) convertView.findViewById(R.id.iv_image);
      vh.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
      convertView.setTag(vh);
    } else {
      vh = (ViewHolder) convertView.getTag();
    }
    vh.tvPrice.setText(getItem(position).getCurrency() + " " + getItem(position).getPrice());
    vh.ivPicture.setImageDrawable(null); //https://github.com/square/picasso/issues/54 issue
    Picasso.with(convertView.getContext())
        .load(GoRestClient.getAbsoluteUrl(":9090/egg/" + egg))
        .into(vh.ivPicture);
    convertView.setBackgroundColor(Color.parseColor("#" + parts[2]));
    vh.ivPicture.setHeightRatio((double) h / w);
    vh.ivPicture.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(final View v) {
        Toast.makeText(getContext(), "The image is " + position + " clicked!",
            Toast.LENGTH_SHORT).show();

        Intent i = new Intent(getContext(), ViewActivity.class);
        Bundle b = new Bundle();
        b.putString("adId", adId);
        i.putExtras(b);
        getContext().startActivity(i);
      }
    });
    return convertView;
  }
}