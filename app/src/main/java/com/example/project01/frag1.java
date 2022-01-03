package com.example.project01;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

//import org.json.JSONArray;
import org.json.JSONException;
//import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class frag1 extends Fragment {

    public frag1() {
        // Required empty public constructor
    }

    ListView jList;
    String folderPath;
    ListAdapter objAdapter;
    ArrayList<ListData> arrList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_frag1, container, false);
        jList = v.findViewById(R.id.json_list);
        folderPath = getActivity().getFilesDir().getAbsolutePath().toString();

        arrList = new ArrayList<>();
        JSONArray jsonArr = new JSONArray();
        AssetManager assetManager = getContext().getAssets();
        try {
            InputStream is = assetManager.open("jsons/test.json");

            File dir = new File(folderPath);
            File files[] = dir.listFiles();

            File storageFile = new File(folderPath+"/numberList0.json");
            try {
                if(storageFile.createNewFile()){
                    try (FileOutputStream outputStream = new FileOutputStream(storageFile)) {
                        int read;
                        byte[] bytes = new byte[1024];
                        while ((read = is.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, read);
                        }
                    }
                    System.out.println("File Saved.");
                }
                else{
                    System.out.println("File Already Exists.");
                }
            } catch (IOException e){
                e.printStackTrace();
            }

            Reader freader = new FileReader(folderPath+"/numberList0.json");
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(freader);
            jsonArr = (JSONArray) jsonObj.get("book");
            Iterator<JSONObject> iterator = jsonArr.iterator();
            int i = 0;
            while(iterator.hasNext()){
                ListData listData = new ListData();
                JSONObject jObj = (JSONObject) jsonArr.get(i);
                iterator.next();
                listData.profileImage = R.drawable.call_resize;
                listData.peopleName = (String) jObj.get("name");
                listData.peopleNum = (String) jObj.get("number");
                listData.peopleAdd = (String) jObj.get("add");
                listData.url = (String) jObj.get("url");
                listData.profileImgColor = (String) jObj.get("color");
                arrList.add(listData);
                i++;
            }

            objAdapter = new CustomListView(arrList);
            jList.setAdapter(objAdapter);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        Button addBtn = v.findViewById(R.id.addBtn);
        JSONArray finalJsonArr = jsonArr;
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.DialogTheme));

                alert.setTitle("ADD");
                alert.setMessage("Input name/number/address/url");

                LayoutInflater alertInflater = getLayoutInflater();
                final View alertView = alertInflater.inflate(R.layout.add_alert, null);
                alert.setView(alertView);

                alert.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ListData addedData = new ListData();
                        addedData.profileImage = R.drawable.call_resize;
                        EditText edit_name = (EditText)alertView.findViewById(R.id.editName);
                        EditText edit_num = (EditText)alertView.findViewById(R.id.editNum);
                        EditText edit_add = (EditText)alertView.findViewById(R.id.editAdd);
                        EditText edit_url = (EditText)alertView.findViewById(R.id.editUrl);
                        String str_name = edit_name.getText().toString();
                        String str_num = edit_num.getText().toString();
                        String str_add = edit_add.getText().toString();
                        String str_url = edit_url.getText().toString();
                        if(str_name.trim().getBytes().length > 0 && str_num.trim().getBytes().length > 0) {
                            final String REGEX = "[0-9]+";
                            String test_num = str_num.trim();
                            if(test_num.matches(REGEX)){
                                addedData.peopleName = str_name;
                                addedData.peopleNum = str_num;
                                addedData.peopleAdd = str_add;
                                addedData.url = str_url;
                                arrList.add(addedData);

                                JSONObject tmpJson = new JSONObject();
                                tmpJson.put("name", str_name);
                                tmpJson.put("number", str_num);
                                tmpJson.put("add", str_add);
                                tmpJson.put("url", str_url);
                                tmpJson.put("color", "#000000");
                                finalJsonArr.add(tmpJson);

                                JSONObject finalJson = new JSONObject();
                                finalJson.put("book", finalJsonArr);

                                FileWriter file;
                                try {
                                    String filePath = folderPath + "/numberList0.json";
                                    file = new FileWriter(filePath);
                                    file.write(finalJson.toJSONString());
                                    file.flush();
                                    file.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                objAdapter = new CustomListView(arrList);
                                jList.setAdapter(objAdapter);
                            }
                            else {
                                AlertDialog.Builder numAlertBuilder = new AlertDialog.Builder(getContext());
                                numAlertBuilder.setTitle("Alert");
                                numAlertBuilder.setMessage("Please fill only number in 'NUM'.");
                                numAlertBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog,int which){
                                    }
                                });
                                numAlertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                numAlertBuilder.show();
                            }
                        }
                        else{
                            AlertDialog.Builder nullAlertBuilder = new AlertDialog.Builder(getContext());
                            nullAlertBuilder.setTitle("Alert");
                            nullAlertBuilder.setMessage("Please fill in the blanks.");
                            nullAlertBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog,int which){
                                }
                            });
                            nullAlertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            nullAlertBuilder.show();
                        }

                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.show();
            }
        });
        return v;
    }
}