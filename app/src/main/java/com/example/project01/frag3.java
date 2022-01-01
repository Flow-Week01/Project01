package com.example.project01;

import static java.util.concurrent.TimeUnit.SECONDS;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.ArraySet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.filament.gltfio.Animator;
import com.google.android.filament.gltfio.FilamentAsset;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag3 extends Fragment implements Scene.OnPeekTouchListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "TEST";
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private Renderable renderable;

    private int[] armenu = {R.raw.tmp1, R.raw.tmp2, R.raw.tmp3, R.raw.table, R.raw.table6, R.raw.ttt, R.raw.chair, R.raw.cbeech, R.raw.cblack, R.raw.couch, R.raw.bed, R.raw.conudim, R.raw.gumball, R.raw.oilcan, R.raw.toyramp, R.raw.fireplace};
    private String[] armenu_string = {"closet A", "closet B", "closet C", "table A", "table B", "table C", "chair A", "chair B", "chair C", "couch", "bed", "conudim", "gumball", "oilcan", "toyramp", "fireplace"};
    private int ar_cur = 0;
    private int ar_prv = 0;
    private TextView[] armenu_text;

    private Button delete_btn;
    private Button reset_btn;

    private TransformableNode modelToRemove = null;
    private List<TransformableNode> model_all;

    private static class AnimationInstance {
        Animator animator;
        Long startTime;
        float duration;
        int index;

        AnimationInstance(Animator animator, int index, Long startTime) {
            this.animator = animator;
            this.startTime = startTime;
            this.duration = animator.getAnimationDuration(index);
            this.index = index;
        }
    }

    private final Set<AnimationInstance> animators = new ArraySet<>();

    private final List<Color> colors =
            Arrays.asList(
                    new Color(0, 0, 0, 1),
                    new Color(1, 0, 0, 1),
                    new Color(0, 1, 0, 1),
                    new Color(0, 0, 1, 1),
                    new Color(1, 1, 0, 1),
                    new Color(0, 1, 1, 1),
                    new Color(1, 0, 1, 1),
                    new Color(1, 1, 1, 1));
    private int nextColor = 0;

    public frag3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frag3.
     */
    // TODO: Rename and change types and number of parameters
    public static frag3 newInstance(String param1, String param2) {
        frag3 fragment = new frag3();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =  inflater.inflate(R.layout.fragment_frag3, container, false);
        if (!checkIsSupportedDeviceOrFinish((MainActivity)getActivity())) {
            return v;
        }

        delete_btn = v.findViewById(R.id.model_delete_btn);
        reset_btn = v.findViewById(R.id.model_reset_btn);
        model_all = new ArrayList<>();

        arFragment = (ArFragment) getChildFragmentManager().findFragmentById(R.id.ux_fragment);
        LinearLayout armenu_view = v.findViewById(R.id.armenu);
        ar_cur = 0;
        ar_prv = 0;
        armenu_text = new TextView[armenu.length];

        WeakReference<frag3> weakActivity = new WeakReference<>(this);

        ModelRenderable.builder()
                .setSource(v.getContext(), armenu[ar_cur])
                .setIsFilamentGltf(true)
                .build()
                .thenAccept(
                        modelRenderable -> {
                            frag3 activity = weakActivity.get();
                            if (activity != null) {
                                activity.renderable = modelRenderable;
                            }
                        })
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(v.getContext(), "Unable to load the selected model", Toast.LENGTH_LONG);

                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        arFragment.getArSceneView().getScene().addOnPeekTouchListener(this);

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {

                    if (renderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable model and add it to the anchor.
                    TransformableNode model = new TransformableNode(arFragment.getTransformationSystem());
                    model.setParent(anchorNode);
                    model.setRenderable(renderable);
                    model.select();

                    model_all.add(model);

                    model.setOnTapListener((HitTestResult hitTestResult, MotionEvent Event) ->
                    {
                        if(delete_btn.getAlpha() == 0f) {
                            modelToRemove = model;
                            delete_btn.setEnabled(true);
                            delete_btn.animate().alpha(1f);
                        }
                    });

                    FilamentAsset filamentAsset = model.getRenderableInstance().getFilamentAsset();
                    if (filamentAsset.getAnimator().getAnimationCount() > 0) {
                        animators.add(new AnimationInstance(filamentAsset.getAnimator(), 0, System.nanoTime()));
                    }

                    Color color = colors.get(nextColor);
                    nextColor = (nextColor + 1) % 8;
                    for (int i = 0; i < renderable.getSubmeshCount(); ++i) {
                        Material material = renderable.getMaterial(i);
                        material.setFloat4("baseColorFactor", color);
                    }
                });

        arFragment.getArSceneView()
                .getScene()
                .addOnUpdateListener(
                        frameTime -> {
                            Long time = System.nanoTime();
                            for (AnimationInstance animator : animators) {
                                animator.animator.applyAnimation(
                                        animator.index,
                                        (float) ((time - animator.startTime) / (double) SECONDS.toNanos(1))
                                                % animator.duration);
                                animator.animator.updateBoneMatrices();
                            }
                        });

        for(int i = 0; i < armenu.length; i++) {
            View view = inflater.inflate(R.layout.item_armenu, armenu_view, false);
            armenu_text[i] = view.findViewById(R.id.textView);
            armenu_text[i].setText(armenu_string[i]);
            if(i == ar_cur) {
                armenu_text[i].setAlpha(1.0f);
            }
            else {
                armenu_text[i].setAlpha(0.5f);
            }
            armenu_text[i].setId(i);
            armenu_text[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ar_prv = ar_cur;
                    ar_cur = view.getId();
                    armenu_text[ar_prv].setAlpha(0.5f);
                    armenu_text[ar_cur].setAlpha(1.0f);

                    if(delete_btn.getAlpha() == 1f) {
                        modelToRemove = null;
                        delete_btn.setEnabled(false);
                        delete_btn.animate().alpha(0f);
                    }

                    ModelRenderable.builder()
                            .setSource(v.getContext(), armenu[ar_cur])
                            .setIsFilamentGltf(true)
                            .build()
                            .thenAccept(
                                    modelRenderable -> {
                                        frag3 activity = weakActivity.get();
                                        if (activity != null) {
                                            activity.renderable = modelRenderable;
                                        }
                                    })
                            .exceptionally(
                                    throwable -> {
                                        Toast toast =
                                                Toast.makeText(v.getContext(), "Unable to load the selected model", Toast.LENGTH_LONG);

                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        return null;
                                    });

                    arFragment.setOnTapArPlaneListener(
                            (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {

                                if (renderable == null) {
                                    return;
                                }

                                // Create the Anchor.
                                Anchor anchor = hitResult.createAnchor();
                                AnchorNode anchorNode = new AnchorNode(anchor);
                                anchorNode.setParent(arFragment.getArSceneView().getScene());

                                // Create the transformable model and add it to the anchor.
                                TransformableNode model = new TransformableNode(arFragment.getTransformationSystem());
                                model.setParent(anchorNode);
                                model.setRenderable(renderable);
                                model.select();

                                model_all.add(model);

                                model.setOnTapListener((HitTestResult hitTestResult, MotionEvent Event) ->
                                {
                                    if(delete_btn.getAlpha() == 0f) {
                                        modelToRemove = model;
                                        delete_btn.setEnabled(true);
                                        delete_btn.animate().alpha(1f);
                                    }
                                });

                                FilamentAsset filamentAsset = model.getRenderableInstance().getFilamentAsset();
                                if (filamentAsset.getAnimator().getAnimationCount() > 0) {
                                    animators.add(new AnimationInstance(filamentAsset.getAnimator(), 0, System.nanoTime()));
                                }

                                Color color = colors.get(nextColor);
                                nextColor = (nextColor + 1) % 8;
                                for (int i = 0; i < renderable.getSubmeshCount(); ++i) {
                                    Material material = renderable.getMaterial(i);
                                    material.setFloat4("baseColorFactor", color);
                                }
                            });

                    arFragment.getArSceneView()
                            .getScene()
                            .addOnUpdateListener(
                                    frameTime -> {
                                        Long time = System.nanoTime();
                                        for (AnimationInstance animator : animators) {
                                            animator.animator.applyAnimation(
                                                    animator.index,
                                                    (float) ((time - animator.startTime) / (double) SECONDS.toNanos(1))
                                                            % animator.duration);
                                            animator.animator.updateBoneMatrices();
                                        }
                                    });
                }
            });

            armenu_view.addView(view);
        }

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(delete_btn.getAlpha()==1f && modelToRemove != null) {
                    modelToRemove.getParent().removeChild(modelToRemove);
                    model_all.remove(modelToRemove);
//                    modelToRemove.setParent(null);
                    modelToRemove = null;
                    delete_btn.setEnabled(false);
                    delete_btn.animate().alpha(0f);
                }
            }
        });

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(delete_btn.getAlpha() == 1f) {
                    modelToRemove = null;
                    delete_btn.setEnabled(false);
                    delete_btn.animate().alpha(0f);
                }

                // Log.d("::::::::BEFORE size of models Array: ", String.valueOf(model_all.size()));

                Iterator<TransformableNode> i = model_all.iterator();
                while(i.hasNext()) {
                    TransformableNode rmv_node = i.next();
                    rmv_node.getParent().removeChild(rmv_node);
                    i.remove();
                }

                // Log.d("::::::::NOW size of models Array: ", String.valueOf(model_all.size()));
            }
        });

        return v;
    }

    @Override
    public void onPeekTouch(HitTestResult hitTestResult, MotionEvent tap) {
        if(delete_btn.getAlpha() == 1f) {
            modelToRemove = null;
            delete_btn.setEnabled(false);
            delete_btn.animate().alpha(0f);
        }
    }

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
}