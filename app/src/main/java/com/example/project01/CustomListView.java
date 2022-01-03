package com.example.project01;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    ArrayList<ListData> listViewData = null;

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

        String folderPath = viewGroup.getContext().getFilesDir().getAbsolutePath().toString();
        final Reader[] freader = {null};
        final JSONObject[] jsonObj = {null};

        mainImg.setImageResource(listViewData.get(i).profileImage);
        mainImg.setColorFilter(Color.parseColor(listViewData.get(i).profileImgColor), PorterDuff.Mode.SRC_IN);

        name.setText(listViewData.get(i).peopleName);
        number.setText(listViewData.get(i).peopleNum);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shopName = listViewData.get(i).peopleName;
                String shopNumber = listViewData.get(i).peopleNum;
                String shopAddress = listViewData.get(i).peopleAdd;
                String shopUrl = listViewData.get(i).url;

                AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(viewGroup.getContext(), R.style.DialogTheme));

                LayoutInflater alertInflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View alertView = alertInflater.inflate(R.layout.shop_detail, null);
                TextView shopname = alertView.findViewById(R.id.detail_name);
                TextView shopnumber = alertView.findViewById(R.id.detail_num);
                TextView shopadd = alertView.findViewById(R.id.detail_add);
                TextView shopurl = alertView.findViewById(R.id.detail_url);
                shopname.setText(shopName);
                shopnumber.setText(shopNumber);
                shopadd.setText(shopAddress);
                shopurl.setText(shopUrl);
                alert.setView(alertView);

                alert.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.show();
            }
        });

        Button editBtn = (Button) view.findViewById(R.id.editBtn);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert2 = new AlertDialog.Builder(new ContextThemeWrapper(viewGroup.getContext(), R.style.DialogTheme));

                alert2.setTitle("EDIT");
                alert2.setMessage("Edit name/number/address/url");

                LayoutInflater alertInflater2 = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View alertV = alertInflater2.inflate(R.layout.add_alert, null);
                alert2.setView(alertV);

                alert2.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int ii) {
                        ListData addedData = new ListData();
                        EditText edit_name = (EditText)alertV.findViewById(R.id.editName);
                        EditText edit_num = (EditText)alertV.findViewById(R.id.editNum);
                        EditText edit_add = (EditText)alertV.findViewById(R.id.editAdd);
                        EditText edit_url = (EditText)alertV.findViewById(R.id.editUrl);
                        String str_name = edit_name.getText().toString();
                        String str_num = edit_num.getText().toString();
                        String str_add = edit_add.getText().toString();
                        String str_url = edit_url.getText().toString();

                        if(str_name.trim().getBytes().length > 0 && str_num.trim().getBytes().length > 0){
                            freader[0] = null;
                            jsonObj[0] = null;
                            try {
                                freader[0] = new FileReader(folderPath+"/numberList.json");
                                JSONParser parser = new JSONParser();
                                jsonObj[0] = (JSONObject) parser.parse(freader[0]);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            addedData.profileImage = listViewData.get(i).profileImage;
                            addedData.peopleName = str_name;
                            addedData.peopleNum = str_num;
                            addedData.peopleAdd = str_add;
                            addedData.url = str_url;
                            addedData.profileImgColor = listViewData.get(i).profileImgColor;

                            JSONArray jsonArr = (JSONArray) jsonObj[0].get("book");
                            JSONObject tmpJson = new JSONObject();
                            tmpJson.put("name", str_name);
                            tmpJson.put("number", str_num);
                            tmpJson.put("add", str_add);
                            tmpJson.put("url", str_url);
                            tmpJson.put("color", listViewData.get(i).profileImgColor);
                            jsonArr.remove(i);
                            jsonArr.add(i, tmpJson);

                            JSONObject finalJson = new JSONObject();
                            finalJson.put("book", jsonArr);

                            FileWriter file;
                            try {
                                String filePath = folderPath + "/numberList.json";
                                file = new FileWriter(filePath);
                                file.write(finalJson.toJSONString());
                                file.flush();
                                file.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            listViewData.remove(i);
                            listViewData.add(i, addedData);

                            mainImg.setImageResource(listViewData.get(i).profileImage);
                            mainImg.setColorFilter(Color.parseColor(listViewData.get(i).profileImgColor), PorterDuff.Mode.SRC_IN);
                            listViewData.get(i).profileImgColor = addedData.profileImgColor;
                            //listViewData.get(i).profileImgColor = listViewData.get(i).profileImgColor;

                            notifyDataSetChanged();
                        }
                    }
                });
                alert2.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert2.show();

            }
        });

        ImageButton rmvBtn = view.findViewById(R.id.imageButton);
        rmvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i > -1) {
                    freader[0] = null;
                    jsonObj[0] = null;
                    try {
                        freader[0] = new FileReader(folderPath+"/numberList.json");
                        JSONParser parser = new JSONParser();
                        jsonObj[0] = (JSONObject) parser.parse(freader[0]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    JSONArray jsonArr = (JSONArray) jsonObj[0].get("book");
                    jsonArr.remove(i);
                    JSONObject finalJson = new JSONObject();
                    finalJson.put("book", jsonArr);

                    FileWriter file;
                    try {
                        String filePath = folderPath + "/numberList.json";
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

        ImageButton resetBtn = view.findViewById(R.id.imageButtonRe);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    freader[0] = new FileReader(folderPath+"/numberList.json");
                    JSONParser parser = new JSONParser();
                    jsonObj[0] = (JSONObject) parser.parse(freader[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                JSONObject replaceObj = (JSONObject) ((JSONArray)jsonObj[0].get("book")).get(i);
                replaceObj.replace("color", "#000000");

                JSONObject finalJsonRe = new JSONObject();
                finalJsonRe.put("book", (JSONArray)jsonObj[0].get("book"));

                FileWriter file;
                try {
                    String filePath = folderPath + "/numberList.json";
                    file = new FileWriter(filePath);
                    file.write(finalJsonRe.toJSONString());
                    file.flush();
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mainImg.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
                listViewData.get(i).profileImgColor = "#000000";
                notifyDataSetChanged();
            }
        });

        mainImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String changedColor = randomColor();
                try {
                    freader[0] = new FileReader(folderPath+"/numberList.json");
                    JSONParser parser = new JSONParser();
                    jsonObj[0] = (JSONObject) parser.parse(freader[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                JSONObject replaceObj = (JSONObject) ((JSONArray)jsonObj[0].get("book")).get(i);
                replaceObj.replace("color", changedColor);

                JSONObject finalJson2 = new JSONObject();
                finalJson2.put("book", (JSONArray)jsonObj[0].get("book"));

                FileWriter file;
                try {
                    String filePath = folderPath + "/numberList.json";
                    file = new FileWriter(filePath);
                    file.write(finalJson2.toJSONString());
                    file.flush();
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mainImg.setColorFilter(Color.parseColor(changedColor), PorterDuff.Mode.SRC_IN);
                listViewData.get(i).profileImgColor = changedColor;
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
