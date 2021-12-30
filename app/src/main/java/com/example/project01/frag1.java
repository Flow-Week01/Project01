package com.example.project01;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class frag1 extends Fragment {

    public frag1() {
        // Required empty public constructor
    }

    ListView jList;
    String folderPath;

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

        final ArrayList<ListData> arrList = new ArrayList<>();
        JSONArray jsonArr = new JSONArray();
        System.out.println(folderPath);
        AssetManager assetManager = getContext().getAssets();
        try {
            InputStream is = assetManager.open("jsons/test.json");

            File dir = new File(folderPath);
            File files[] = dir.listFiles();
            for (int j = 0; j < files.length; j++) {

                System.out.println("file: " + files[j].getName());
            }

            File storageFile = new File(folderPath+"/numberList.json");
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

            Reader freader = new FileReader(folderPath+"/numberList.json");
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(freader);
            jsonArr = (JSONArray) jsonObj.get("book");
            Iterator<JSONObject> iterator = jsonArr.iterator();
            int i = 0;
            while(iterator.hasNext()){
//                if(i<)
                ListData listData = new ListData();
                JSONObject jObj = (JSONObject) jsonArr.get(i);
                iterator.next();
                listData.profileImage = R.drawable.call_resize;
                listData.peopleName = (String) jObj.get("name");
                listData.peopleNum = (String) jObj.get("number");
                arrList.add(listData);
                i++;
            }

            ListAdapter objAdapter = new CustomListView(arrList);
            jList.setAdapter(objAdapter);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

//
        Button addBtn = v.findViewById(R.id.addBtn);
        JSONArray finalJsonArr = jsonArr;
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                alert.setTitle("ADD");
                alert.setMessage("Input name & number");

                LayoutInflater alertInflater = getLayoutInflater();
                final View alertView = alertInflater.inflate(R.layout.add_alert, null);
                alert.setView(alertView);

                alert.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ListData addedData = new ListData();
                        addedData.profileImage = R.drawable.call_resize;
                        EditText edit_name = (EditText)alertView.findViewById(R.id.editName);
                        EditText edit_num = (EditText)alertView.findViewById(R.id.editNum);
                        String str_name = edit_name.getText().toString();
                        String str_num = edit_num.getText().toString();
                        System.out.println(str_name.trim().getBytes());
                        System.out.println(str_name.trim().getBytes().length);
                        System.out.println(str_name.trim().getBytes());
                        System.out.println(str_num.trim().getBytes().length);
                        if(str_name.trim().getBytes().length > 0 && str_num.trim().getBytes().length > 0) {
                            final String REGEX = "[0-9]+";
                            String test_num = str_num.trim();
                            if(test_num.matches(REGEX)){
                                addedData.peopleName = str_name;
                                addedData.peopleNum = str_num;
                                arrList.add(addedData);

                                JSONObject tmpJson = new JSONObject();
                                tmpJson.put("name", str_name);
                                tmpJson.put("number", str_num);
                                finalJsonArr.add(tmpJson);

                                JSONObject finalJson = new JSONObject();
                                finalJson.put("book", finalJsonArr);

                                System.out.println(finalJson);

                                FileWriter file;
                                try {
                                    String filePath = folderPath + "/numberList.json";
                                    System.out.println(filePath);
                                    file = new FileWriter(filePath);
                                    file.write(finalJson.toJSONString());
                                    file.flush();
                                    file.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                AlertDialog.Builder numAlertBuilder = new AlertDialog.Builder(getContext());
                                numAlertBuilder.setTitle("Alert");
                                numAlertBuilder.setMessage("Please fill only number in 'NUM'.");
                                numAlertBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog,int which){
                                    }
                                });
                                numAlertBuilder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
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
                            nullAlertBuilder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
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