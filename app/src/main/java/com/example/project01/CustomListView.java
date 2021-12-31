package com.example.project01;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
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
                String folderPath = viewGroup.getContext().getFilesDir().getAbsolutePath().toString();
                if (i > -1) {
                    Reader freader = null;
                    JSONObject jsonObj = null;
                    try {
                        freader = new FileReader(folderPath+"/numberList.json");
                        JSONParser parser = new JSONParser();
                        jsonObj = (JSONObject) parser.parse(freader);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    JSONArray jsonArr = (JSONArray) jsonObj.get("book");
                    jsonArr.remove(i);
                    JSONObject finalJson = new JSONObject();
                    finalJson.put("book", jsonArr);

                    FileWriter file;
                    try {
                        String filePath = folderPath + "/numberList.json";
                        System.out.println(filePath + "DONE");
                        file = new FileWriter(filePath);
                        file.write(finalJson.toJSONString());
                        file.flush();
                        file.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    listViewData.remove(i);
                    notifyDataSetChanged();
                }
            }
        });

        mainImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String changedColor = randomColor();
                mainImg.setColorFilter(Color.parseColor(changedColor), PorterDuff.Mode.SRC_IN);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    private String randomColor(){
        String color = "#";
        final Random random = new Random();
        final String[] letters = Arrays.copyOfRange("0123456789ABCDEF".split(""), 1, 17);
        for (int i = 0; i < 6; i++) {
            int tmpInt = random.nextInt(16);
            String tmpLetter = letters[tmpInt];
            color += tmpLetter;
        }
        return color;
    }

}
