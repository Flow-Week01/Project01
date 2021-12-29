package com.example.project01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListView extends BaseAdapter {
    LayoutInflater layoutInflater = null;
    private ArrayList<ListData> listViewData = null;
    private int count = 0;

    public CustomListView(ArrayList<ListData> listData){
        listViewData = listData;
        count = listViewData.size();
    }
    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            final Context context = viewGroup.getContext();
            if(layoutInflater == null){
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            view = layoutInflater.inflate(R.layout.custom_listview, viewGroup, false);
        }
        ImageView mainImg = view.findViewById(R.id.profileImg);

        TextView name = view.findViewById(R.id.nameTxt);
        TextView number = view.findViewById(R.id.numberTxt);

        mainImg.setImageResource(listViewData.get(i).profileImage);
        name.setText(listViewData.get(i).peopleName);
        number.setText(listViewData.get(i).peopleNum);

        return view;
    }
}
