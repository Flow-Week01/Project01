package com.example.project01;

import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

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
                listData.profileImage = R.drawable.ic_launcher_foreground;
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
        return v;
    }
}