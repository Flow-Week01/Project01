package com.example.project01;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

public class SlideImage extends Fragment implements View.OnClickListener{
    ImageView[] imageView_arr;
    private int prev_sel_idx;
    private int cur_sel_idx;
    ImageView selectedImageView;

    int[] images = {R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5,
            R.drawable.img6,
            R.drawable.img7,
            R.drawable.img8,
            R.drawable.img9,
            R.drawable.img10,
            R.drawable.img11,
            R.drawable.img12,
            R.drawable.img13,
            R.drawable.img14,
            R.drawable.img15,
            R.drawable.img16,
            R.drawable.img17,
            R.drawable.img18,
            R.drawable.img19,
            R.drawable.img20,};

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
        LinearLayout gallery = v.findViewById(R.id.gallery);
        selectedImageView = (ImageView) v.findViewById(R.id.selectedImageView);

        imageView_arr = new ImageView[images.length];
        prev_sel_idx = 0;
        cur_sel_idx = 0;

        selectedImageView.setImageResource(images[cur_sel_idx]);
        for(int i = 0; i < images.length; i++) {
            View view = inflater.inflate(R.layout.item, gallery, false);
            imageView_arr[i] = view.findViewById(R.id.imageView);
            imageView_arr[i].setImageResource(images[i]);
            if(i == cur_sel_idx) {
                imageView_arr[i].setAlpha(1.0f);
            }
            else {
                imageView_arr[i].setAlpha(0.5f);
            }
            imageView_arr[i].setId(i);
            imageView_arr[i].setOnClickListener(this);
            // Log.d("=================id_check: ", String.valueOf(imageView_arr[i].getId()));

            gallery.addView(view);
        }

        return v;
    }

    public void onClick(View view){
        // Log.d("=================id_check: ", String.valueOf(view.getId()));
        prev_sel_idx = cur_sel_idx;
        cur_sel_idx = view.getId();
        imageView_arr[prev_sel_idx].setAlpha(0.5f);
        imageView_arr[cur_sel_idx].setAlpha(1.0f);
        selectedImageView.setImageResource(images[cur_sel_idx]);
    }
}
