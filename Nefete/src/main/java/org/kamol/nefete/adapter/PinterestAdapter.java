package org.kamol.nefete.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.etsy.android.grid.util.DynamicHeightImageView;
//import com.etsy.android.grid.util.DynamicHeightTextView;
import com.squareup.picasso.Picasso;

import org.kamol.nefete.R;
import org.kamol.nefete.http.GoRestClient;

import java.util.ArrayList;
import java.util.Random;

/**
 * PINTEREST ADAPTER
 */
public class PinterestAdapter extends ArrayAdapter<String> {

    private static final String TAG = "PinterestAdapter";

    static class ViewHolder {
        //DynamicHeightTextView txtLineOne;
        DynamicHeightImageView mImageView;
    }

    private final LayoutInflater mLayoutInflater;
//    private final Random mRandom;
//    private final ArrayList<Integer> mBackgroundColors;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public PinterestAdapter(final Context context, final int textViewResourceId) {
        super(context, textViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);

//        mRandom = new Random();
//        mBackgroundColors = new ArrayList<Integer>();
//        mBackgroundColors.add(R.color.orange);
//        mBackgroundColors.add(R.color.green);
//        mBackgroundColors.add(R.color.blue);
//        mBackgroundColors.add(R.color.yellow);
//        mBackgroundColors.add(R.color.grey);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_sample, parent, false);
            vh = new ViewHolder();
            //vh.txtLineOne = (DynamicHeightTextView) convertView.findViewById(R.id.txt_line1);
            vh.mImageView = (DynamicHeightImageView) convertView.findViewById(R.id.iv_image);
            // Trigger the download of the URL asynchronously into the image view.
            Picasso.with(this.getContext())
                    .load(GoRestClient.getAbsoluteUrl(":9090/egg/" + getItem(position)))
                            //.placeholder(android.R.drawable.ic_menu_camera)
                    .error(android.R.drawable.ic_input_delete)
                            //.resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
                            //.centerInside()
                    .into(vh.mImageView);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position, getItem(position));
        //int backgroundIndex = position >= mBackgroundColors.size() ? position % mBackgroundColors.size() : position;

        //ColorDrawable cd = new ColorDrawable(Integer.decode(getChromosome(getItem(position), 2)));
//        convertView.setBackgroundColor(Integer.decode(getChromosome(getItem(position), 2)));
        String color = "#" + getChromosome(getItem(position), 2);
        convertView.setBackgroundColor(Color.parseColor(color));

//        convertView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));

        Log.d(TAG, "getView position:" + position + " h:" + positionHeight);

        vh.mImageView.setHeightRatio(positionHeight);
        //vh.txtLineOne.setHeightRatio(positionHeight);
        //vh.txtLineOne.setText(getItem(position) + position);

        return convertView;
    }

    private String getChromosome(final String egg, int i) {
        String[] parts = egg.split("_");
        return parts[i];
    }

    private double getPositionRatio(final int position, final String egg) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
//            ratio = getRandomHeightRatio();
            //0001_689124d1438e909ad97c0d929d590f6b8728d084_D0D2D3_400_741
            String[] parts = egg.split("_");
            int w = Integer.valueOf(parts[3]);
            int h = Integer.valueOf(parts[4]);
            double r = (double) h / w;
            sPositionHeightRatios.append(position, r);
//            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

//    private double getRandomHeightRatio() {
//        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
//    }
}