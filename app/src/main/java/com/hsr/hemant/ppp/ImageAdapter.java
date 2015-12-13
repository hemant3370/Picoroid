package com.hsr.hemant.ppp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageAdapter extends BaseAdapter {
    public String[] pp;
    private Context mContext;

    public ImageAdapter(Context c, String[] pps) {
        mContext = c;
        this.pp = pps;
    }

    public String[] getPp() {
        return pp;
    }

    @Override
    public int getCount() {

        return pp.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView view = (ImageView) convertView;
        if (view == null) {
            view = new ImageView(mContext);
        }
        String url = pp[position];
        Picasso.with(mContext).load(url).into(view);
//        com.android.volley.toolbox.NetworkImageView imageView;
//        ImageLoader mImageLoader;
//        mImageLoader = VolleySingleton.getInstance().getImageLoader();
//        if (convertView == null) {
//            // if it's not recycled, initialize some attributes
//          //  imageView = new NetworkImageView(mContext);
//
//
//            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(1, 1, 1, 1);
        view.setLayoutParams(new GridView.LayoutParams(200, 200));
        view.setScaleType(ImageView.ScaleType.FIT_CENTER);
        view.setPadding(1, 1, 1, 1);
//        } else {
//            imageView = (NetworkImageView) convertView;
//        }
//
//       // imageView.setDefaultImageResId(R.drawable.powered_by_google_light);
//      //  imageView.setErrorImageResId(R.drawable.common_google_signin_btn_icon_light);
//        imageView.setAdjustViewBounds(true);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            imageView.setElevation(5);
//        }
        //imageView.setImageUrl(pp[position], mImageLoader);


        return view;
    }

}