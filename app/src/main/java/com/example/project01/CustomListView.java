package com.example.project01;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class CustomListView extends BaseAdapter {
    LayoutInflater layoutInflater = null;
    private ArrayList<ListData> listViewData = null;

    public CustomListView(ArrayList<ListData> listData){
        listViewData = listData;
    }
    @Override
    public int getCount() {
        return listViewData.size();
    }

    @Override
    public Object getItem(int i) {
        return listViewData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
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

        ImageButton rmvBtn = view.findViewById(R.id.imageButton);
        rmvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i > -1) {
                    listViewData.remove(i);
                    notifyDataSetChanged();
                }
            }
        });

        mainImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainImg.setColorFilter(Color.parseColor(randomColor()), PorterDuff.Mode.SRC_IN);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    private String randomColor(){
        String color = "#";
        final Random random = new Random();
        final String[] letters = "0123456789ABCDEF".split("");
        for (int i = 0; i < 6; i++) {
            color += letters[random.nextInt(16)];
        }
        System.out.println(color);
        return color;
    }

}
