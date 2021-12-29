package com.example.project01;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class frag1 extends Fragment {

    public frag1() {
        // Required empty public constructor
    }

    ListView jList;
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

        final ArrayList<ListData> arrList = new ArrayList<>();
        //final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrList);

        //jList.setAdapter(arrayAdapter);
        AssetManager assetManager = getContext().getAssets();
        try {
            InputStream is = assetManager.open("jsons/test.json");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            StringBuffer buf = new StringBuffer();
            String line = br.readLine();
            while(line != null){
                buf.append(line);
                line = br.readLine();
            }
            String name = null;
            String number = null;
            String jsonData = buf.toString();
            JSONObject jsonObj = new JSONObject(jsonData);
            JSONArray jsonArr = jsonObj.getJSONArray("book");
            for (int i = 0; i < jsonArr.length(); i++){
                ListData listData = new ListData();
                JSONObject jObj = jsonArr.getJSONObject(i);
                listData.profileImage = R.drawable.call_resize;
                listData.peopleName = jObj.optString("name");
                listData.peopleNum = jObj.optString("number");
                arrList.add(listData);
                //arrayAdapter.notifyDataSetChanged();
            }

            ListAdapter objAdapter = new CustomListView(arrList);
            jList.setAdapter(objAdapter);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        Button addBtn = v.findViewById(R.id.addBtn);
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
                        addedData.peopleName = str_name;
                        addedData.peopleNum = str_num;
                        arrList.add(addedData);
                        String tmpData = "\"name\":"+str_name+",\"number\":\""+str_num+"\"";
                        JSONObject tmpJson = null;
                        try {
                            tmpJson = new JSONObject(tmpData);
                        } catch (JSONException e) {
                            e.printStackTrace();
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