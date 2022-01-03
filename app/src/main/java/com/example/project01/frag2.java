package com.example.project01;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag2 extends Fragment implements View.OnClickListener{
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

    private Button camera_btn;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private static String photo_filePath = null;

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

        Button btn = (Button) v.findViewById(R.id.toggle_layout_button);
        btn.setOnClickListener(this);

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
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    refresh_files();
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

        return v;
    }

    public void onClick(View view) {
        is_slide = !is_slide;
        transaction = fragmentManager.beginTransaction();

        if(is_slide){
            transaction.replace(R.id.tab2_frameLayout, slideImage).commitAllowingStateLoss();
        }
        else{
            transaction.replace(R.id.tab2_frameLayout, gridImage).commitAllowingStateLoss();
        }
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
}