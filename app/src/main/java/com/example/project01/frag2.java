package com.example.project01;

import static android.app.Activity.RESULT_OK;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag2 extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentManager fragmentManager;
    private SlideImage slideImage;
    private GridImage gridImage;
    private FragmentTransaction transaction;

    private boolean is_slide = true;

    private ImageButton camera_btn;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private static String photo_filePath = null;

    private LinearLayout gallery_line_view;
    private RelativeLayout relativeLayout_light;
    private ImageView imageView_light;
    private ImageView imageView2_light;
    private ImageView imageView3_light;

    private ValueAnimator animator;
    private ValueAnimator animator2;

    private boolean light_existed = false;
    private boolean light2_existed = false;
    private boolean light3_existed = false;

    private ImageView light_switch;
    private boolean light_on = true;

    int[] images = {R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5,
            R.drawable.img6,
            R.drawable.img7,
            R.drawable.img8,
            R.drawable.img9,
            R.drawable.img10,
            R.drawable.img11,
            R.drawable.img12,
            R.drawable.img13,
            R.drawable.img14,
            R.drawable.img15,
            R.drawable.img16,
            R.drawable.img17,
            R.drawable.img18,
            R.drawable.img19,
            R.drawable.img20,};

    private static File[] files;
    private File captured_file;
    File directory = null;

    public frag2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frag2.
     */
    // TODO: Rename and change types and number of parameters
    public static frag2 newInstance(String param1, String param2) {
        frag2 fragment = new frag2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("WrongThread")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_frag2, container, false);

        fragmentManager = getParentFragmentManager();
        slideImage = new SlideImage();
        gridImage = new GridImage();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.tab2_frameLayout, slideImage).commitAllowingStateLoss();

        camera_btn = v.findViewById(R.id.camera_btn);
        photo_filePath = v.getContext().getFilesDir().getAbsolutePath().toString().concat("/photos/");
        directory = new File(photo_filePath);

        if (! directory.exists()) {
            directory.mkdir();
            // write all res files in the created directory
            for(int i=0; i<images.length; i++) {
                Bitmap PhotoBitmap  = BitmapFactory.decodeResource(v.getContext().getResources(), images[i]);
                File file = new File(directory, String.valueOf(i));

                if (file.exists()) {
                    file.delete();
                }
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    PhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // Log.d("File path for photos: ", photo_filePath);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    if (captured_file.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(captured_file.getAbsolutePath());
                        bitmap = Bitmap.createScaledBitmap(bitmap, 800,bitmap.getHeight()/(bitmap.getWidth()/800),false);

                        ExifInterface exif = null;
                        try {
                            exif = new ExifInterface(captured_file.getAbsoluteFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegrees(exifOrientation);
                        bitmap = rotate(bitmap, exifDegree);

                        captured_file.delete();

                        try {
                            FileOutputStream out = new FileOutputStream(captured_file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        refresh_files();
                    }
                }
            }
        });

        files = directory.listFiles();

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
                String file_name = dateFormat.format(new Date());
                // Log.d("---------saved file name: ", file_name);
                captured_file = new File(directory, file_name);
                Uri imageUri = FileProvider.getUriForFile(v.getContext(), "com.example.project01.fileprovider", captured_file);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    activityResultLauncher.launch(intent);
                }
                else {
                    Toast.makeText(v.getContext(), "지원되지 않는 기능입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        gallery_line_view = v.findViewById(R.id.gallery_line_view);
        animator = ValueAnimator.ofFloat(0.15f, 0.3f);
        animator.setDuration(1000);
        animator.setStartDelay(800);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) gallery_line_view.getLayoutParams();
                params.weight = value;
                gallery_line_view.setLayoutParams(params);
            }
        });

        animator2 = ValueAnimator.ofFloat(0.3f, 0.15f);
        animator2.setDuration(1000);
        animator2.setStartDelay(1200);

        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) gallery_line_view.getLayoutParams();
                params.weight = value;
                gallery_line_view.setLayoutParams(params);
            }
        });

        gallery_line_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_slide = !is_slide;
                transaction = fragmentManager.beginTransaction();
                if (is_slide) {
                    light_on = true;
                    light_switch.setImageResource(R.drawable.switch_on);
                    transaction.replace(R.id.tab2_frameLayout, slideImage).commitAllowingStateLoss();
                } else {
                    light_on = false;
                    light_switch.setImageResource(R.drawable.switch_off);
                    transaction.replace(R.id.tab2_frameLayout, gridImage).commitAllowingStateLoss();
                }

                show_animation(true);
            }
        });

        relativeLayout_light = v.findViewById(R.id.relative_layout_light);

        imageView_light = new ImageView(v.getContext());
        imageView_light.setImageResource(R.drawable.light_circle0);
        imageView_light.setAlpha(0f);

        imageView2_light = new ImageView(v.getContext());
        imageView2_light.setImageResource(R.drawable.light_circle0);
        imageView2_light.setAlpha(0f);

        imageView3_light = new ImageView(v.getContext());
        imageView3_light.setImageResource(R.drawable.light_circle0);
        imageView3_light.setAlpha(0f);

        light_switch = v.findViewById(R.id.light_switch);
        light_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_slide) {
                    light_on = !light_on;
                    if (light_on) {
                        light_switch.setImageResource(R.drawable.switch_on);
                        light_on();
                    }
                    else {
                        light_switch.setImageResource(R.drawable.switch_off);
                        light_off();
                    }
                }
            }
        });

        return v;
    }

    public static String getPhoto_filePath() {
        return photo_filePath;
    }

    public static File getFile_at(int idx) {
        return files[idx];
    }

    public static int File_cnt() {
        return files.length;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String path_name, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path_name, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path_name, options);
    }

    public void refresh_files() {
        if(directory != null) {
            files = directory.listFiles();
            if(is_slide) {
                slideImage.Slide_reload(); // this might be erroneous if the file is overwritten (see line 167~169)
            }
            else {
                gridImage.Grid_Reload();
            }
        }
    }

    public void show_animation(boolean from_click) {
        if(is_slide) {
            if(!from_click && !light_on) {
                light_on = true;
                light_switch.setImageResource(R.drawable.switch_on);
            }

            imageView_light.animate().setStartDelay(1800);
            imageView2_light.animate().setStartDelay(1900);
            imageView3_light.animate().setStartDelay(2000);

            animator.start();

            if(light_existed == false) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200, 200);
                params.leftMargin = relativeLayout_light.getWidth()/2 - 105; // 200/2 + 5
                params.topMargin = gallery_line_view.getHeight() * 2 - 180; // 200/2 + 80
                relativeLayout_light.addView(imageView_light, params);

                light_existed = true;
            }

            if(light2_existed == false) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(350, 350);
                params.leftMargin = relativeLayout_light.getWidth()/2 - 180; // 350/2 + 5
                params.topMargin = gallery_line_view.getHeight() * 2 - 255; // 350/2 + 80
                relativeLayout_light.addView(imageView2_light, params);

                light2_existed = true;
            }

            if(light3_existed == false) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(500, 500);
                params.leftMargin = relativeLayout_light.getWidth()/2 - 255; // 500/2 + 5
                params.topMargin = gallery_line_view.getHeight() * 2 - 330; // 500/2 + 80
                relativeLayout_light.addView(imageView3_light, params);

                light3_existed = true;
            }

            imageView_light.animate().alpha(0.8f);
            imageView2_light.animate().alpha(0.3f);
            imageView3_light.animate().alpha(0.1f);
        }
        else if(from_click){
            imageView_light.animate().setStartDelay(800);
            imageView2_light.animate().setStartDelay(900);
            imageView3_light.animate().setStartDelay(1000);

            imageView3_light.animate().alpha(0f);
            imageView2_light.animate().alpha(0f);
            imageView_light.animate().alpha(0f);

            animator2.start();
        }
    }

    public void reset_gallery_line() {
        if(is_slide) {
            imageView_light.setAlpha(0f);
            imageView2_light.setAlpha(0f);
            imageView3_light.setAlpha(0f);
        }
    }

    public void light_off() {
        if(is_slide) {
            imageView_light.animate().setStartDelay(0);
            imageView2_light.animate().setStartDelay(100);
            imageView3_light.animate().setStartDelay(200);

            imageView3_light.animate().alpha(0f);
            imageView2_light.animate().alpha(0f);
            imageView_light.animate().alpha(0f);
        }
    }

    public void light_on() {
        if(is_slide) {
            imageView_light.animate().setStartDelay(0);
            imageView2_light.animate().setStartDelay(100);
            imageView3_light.animate().setStartDelay(200);

            imageView_light.animate().alpha(0.8f);
            imageView2_light.animate().alpha(0.3f);
            imageView3_light.animate().alpha(0.1f);
        }
    }

    public int exifOrientationToDegrees(int exifOrientation){
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        }else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    public Bitmap rotate(Bitmap bitmap, int degrees)
    {
        if(degrees != 0 && bitmap != null)
        {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);
            try
            {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if(bitmap != converted)
                {
                    bitmap.recycle();
                    bitmap = converted;
                }
            }
            catch(OutOfMemoryError ex)
            {
            }
        }
        return bitmap;
    }
}