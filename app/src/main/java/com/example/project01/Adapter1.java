package com.example.project01;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class Adapter1 extends BaseAdapter {
    private Context mContext;

    public Adapter1(Context c){
        mContext = c;
    }

    @Override
    public int getCount() {
        return frag2.File_cnt();
    }

    @Override
    public Object getItem(int position) {
        return frag2.getFile_at(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageBitmap(frag2.decodeSampledBitmapFromResource(frag2.getFile_at(position).getAbsolutePath(), 200, 200));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
        return imageView;
    }
}