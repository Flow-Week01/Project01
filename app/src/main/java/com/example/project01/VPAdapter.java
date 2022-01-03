package com.example.project01;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class VPAdapter extends FragmentPagerAdapter {
    int[] tabIcons = {
            R.drawable.num_resize_padding,
            R.drawable.pic_resize_padding,
            R.drawable.ar_resize_padding
    };

    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();
    private Context context;

    private SpannableString[] sb_list;

    public VPAdapter(@NonNull FragmentManager fm, int behavior, Context context){
        super(fm, behavior);
        this.context = context;
        this.sb_list = new SpannableString[3];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    public void addFragment(Fragment frag, String title) {
        fragmentArrayList.add(frag);
        fragmentTitle.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        Drawable image = context.getResources().getDrawable(tabIcons[position], null);
        if(position == 0) image.setColorFilter(context.getResources().getColor(R.color.blue, null), PorterDuff.Mode.SRC_IN);
        else image.setColorFilter(context.getResources().getColor(R.color.black, null), PorterDuff.Mode.SRC_IN);

        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb_list[position] = new SpannableString(" ");
        sb_list[position].setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb_list[position].setSpan(new AbsoluteSizeSpan(100), 0,1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE); // set size
        return sb_list[position];
    }

    public CharSequence changeColorTitle(int position, boolean selected) {
        Drawable image = context.getResources().getDrawable(tabIcons[position], null);
        if(selected) {
            Log.d("hereee:::::", String.valueOf(position));
            image.setColorFilter(context.getResources().getColor(R.color.blue, null), PorterDuff.Mode.SRC_IN);
        }
        else {
            image.setColorFilter(context.getResources().getColor(R.color.black, null), PorterDuff.Mode.SRC_IN);
        }
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());

        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb_list[position] = new SpannableString(" ");
        sb_list[position].setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb_list[position].setSpan(new AbsoluteSizeSpan(100), 0,1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE); // set size

        return sb_list[position];
    }
}
