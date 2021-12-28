package com.example.project01;

import android.app.AlertDialog;
import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public frag1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frag1.
     */
    // TODO: Rename and change types and number of parameters
    public static frag1 newInstance(String param1, String param2) {
        frag1 fragment = new frag1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ListView jList;
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setContentView(R.layout.activity_main);

        jList = findViewById(R.id.json_list);

        final ArrayList<String> arrList = new ArrayList<String>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrList);

        jList.setAdapter(arrayAdapter);

        Button btn = (Button)findViewById(R.id.button);
        AssetManager assetManager = getAssets();
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
                JSONObject jObj = jsonArr.getJSONObject(i);
                name = jObj.optString("name");
                number = jObj.optString("number");
                System.out.println(arrList);
                arrList.add("Name: "+name+"\nNumber: "+number);
                arrayAdapter.notifyDataSetChanged();
            }


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag1, container, false);
    }
}