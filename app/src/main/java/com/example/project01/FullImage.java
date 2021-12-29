package com.example.project01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;

public class FullImage extends Activity {
    ImageSwitcher Switch;
    ImageView images;
    float initialX;
    Adapter1  imageAdapter;
    private  int  position ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);
        Switch = (ImageSwitcher) findViewById(R.id.imageSwitcher);

        Intent i = getIntent();
        position = i.getExtras().getInt("id");
        imageAdapter = new Adapter1(this);

        images = (ImageView) findViewById(R.id.full_image_view);
        images.setImageResource(imageAdapter.mThumbIds[position]);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float finalX = event.getX();
                if (initialX > finalX) {

                    if (position == imageAdapter.mThumbIds.length-1) {

                        position = 0;
                        images.setImageResource(imageAdapter.mThumbIds[0]);
                        Toast.makeText(getApplicationContext(), "First Image",
                                Toast.LENGTH_LONG).show();
                        Switch.showPrevious();

                    } else {
                        position++;
                        images.setImageResource(imageAdapter.mThumbIds[position]);
                        Toast.makeText(getApplicationContext(), "Next Image",
                                Toast.LENGTH_LONG).show();
                        Switch.showNext();
                    }

                }
                else
                {
                    if(position > 0)
                    {

                        position= position-1;
                        images.setImageResource(imageAdapter.mThumbIds[position]);
                        Toast.makeText(getApplicationContext(), "Previous Image",
                                Toast.LENGTH_LONG).show();
                        Switch.showPrevious();

                    }
                    else
                    {
                        position = imageAdapter.mThumbIds.length-1;
                        images.setImageResource(imageAdapter.mThumbIds[position]);
                        Toast.makeText(getApplicationContext(), "Last Image",
                                Toast.LENGTH_LONG).show();
                        Switch.showPrevious();
                    }
                }
                break;
        }
        return false;
    }
}