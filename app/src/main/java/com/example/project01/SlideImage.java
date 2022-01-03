package com.example.project01;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SlideImage extends Fragment implements View.OnClickListener{
    ArrayList<ImageView> imageView_arr;
    private int prev_sel_idx;
    private int cur_sel_idx;
    ImageView selectedImageView;
    LinearLayout gallery;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SlideImage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.slide_layout, container, false);
        gallery = v.findViewById(R.id.gallery);
        selectedImageView = (ImageView) v.findViewById(R.id.selectedImageView);

        imageView_arr = new ArrayList<ImageView>();
        prev_sel_idx = 0;
        cur_sel_idx = 0;

        selectedImageView.setImageBitmap(BitmapFactory.decodeFile(frag2.getFile_at(cur_sel_idx).getAbsolutePath()));

        for(int i = 0; i < frag2.File_cnt(); i++) {
            View view = inflater.inflate(R.layout.item, gallery, false);
            imageView_arr.add(view.findViewById(R.id.imageView));
            imageView_arr.get(i).setImageBitmap(frag2.decodeSampledBitmapFromResource(frag2.getFile_at(i).getAbsolutePath(), 150, 150));
            if(i == cur_sel_idx) {
                imageView_arr.get(i).setAlpha(1.0f);
            }
            else {
                imageView_arr.get(i).setAlpha(0.5f);
            }
            imageView_arr.get(i).setId(i);
            imageView_arr.get(i).setOnClickListener(this);
            // Log.d("=================id_check: ", String.valueOf(imageView_arr[i].getId()));

            gallery.addView(view);
        }

        return v;
    }

    public void Slide_reload() {
        imageView_arr.clear();
        gallery.removeAllViews();
        selectedImageView.setImageBitmap(BitmapFactory.decodeFile(frag2.getFile_at(cur_sel_idx).getAbsolutePath()));

        for(int i = 0; i < frag2.File_cnt(); i++) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.item, gallery, false);
            imageView_arr.add(view.findViewById(R.id.imageView));
            imageView_arr.get(i).setImageBitmap(frag2.decodeSampledBitmapFromResource(frag2.getFile_at(i).getAbsolutePath(), 150, 150));
            if(i == cur_sel_idx) {
                imageView_arr.get(i).setAlpha(1.0f);
            }
            else {
                imageView_arr.get(i).setAlpha(0.5f);
            }
            imageView_arr.get(i).setId(i);
            imageView_arr.get(i).setOnClickListener(this);
            // Log.d("=================id_check: ", String.valueOf(imageView_arr[i].getId()));

            gallery.addView(view);
        }
    }

    public void Slide_add(File file) {
        int new_idx = frag2.File_cnt() - 1;
        View view = getActivity().getLayoutInflater().inflate(R.layout.item, gallery, false);
        imageView_arr.add(view.findViewById(R.id.imageView));
        imageView_arr.get(new_idx).setImageBitmap(frag2.decodeSampledBitmapFromResource(frag2.getFile_at(new_idx).getAbsolutePath(), 150, 150));
        imageView_arr.get(new_idx).setAlpha(0.5f);
        imageView_arr.get(new_idx).setId(new_idx);
        imageView_arr.get(new_idx).setOnClickListener(this);

        gallery.addView(view);
    }

    public void onClick(View view){
        // Log.d("=================id_check: ", String.valueOf(view.getId()));
        prev_sel_idx = cur_sel_idx;
        cur_sel_idx = view.getId();
        imageView_arr.get(prev_sel_idx).setAlpha(0.5f);
        imageView_arr.get(cur_sel_idx).setAlpha(1.0f);
        selectedImageView.setImageBitmap(BitmapFactory.decodeFile(frag2.getFile_at(cur_sel_idx).getAbsolutePath()));
    }
}
