package com.example.project01;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

public class GridImage extends Fragment {
    GridView gridView;
    View v;

    public GridImage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.grid_layout, container, false);
        gridView = (GridView) v.findViewById(R.id.grid_view);
        gridView.setAdapter(new Adapter1(v.getContext()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent i = new Intent(v.getContext(), FullImage.class);
                i.putExtra("id", position);
                startActivity(i);
            }
        });
        return v;
    }

    public void Grid_Reload() {
        if(gridView != null) {
            gridView.invalidateViews();
            gridView.setAdapter(new Adapter1(v.getContext()));
        }
    }
}
