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
import android.widget.Toast;

import com.google.android.filament.gltfio.Animator;
import com.google.android.filament.gltfio.FilamentAsset;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag3 extends Fragment {

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

        arFragment = (ArFragment) getChildFragmentManager().findFragmentById(R.id.ux_fragment);
        Log.d("------------------whwywwhwyww: ", String.valueOf(arFragment.getArSceneView()));

        WeakReference<frag3> weakActivity = new WeakReference<>(this);

        ModelRenderable.builder()
                .setSource(v.getContext(), R.raw.toyramp)
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
                                    Toast.makeText(v.getContext(), "Unable to load Tiger renderable", Toast.LENGTH_LONG);

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

                    FilamentAsset filamentAsset = model.getRenderableInstance().getFilamentAsset();
                    if (filamentAsset.getAnimator().getAnimationCount() > 0) {
                        animators.add(new AnimationInstance(filamentAsset.getAnimator(), 0, System.nanoTime()));
                    }

                    Color color = colors.get(nextColor);
                    nextColor++;
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

        Button changeBtn = v.findViewById(R.id.changeBtn);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModelRenderable.builder()
                        .setSource(v.getContext(), R.raw.oilcan)
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
                                            Toast.makeText(v.getContext(), "Unable to load Tiger renderable", Toast.LENGTH_LONG);

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

                            FilamentAsset filamentAsset = model.getRenderableInstance().getFilamentAsset();
                            if (filamentAsset.getAnimator().getAnimationCount() > 0) {
                                animators.add(new AnimationInstance(filamentAsset.getAnimator(), 0, System.nanoTime()));
                            }

                            Color color = colors.get(nextColor);
                            nextColor++;
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
        return v;
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