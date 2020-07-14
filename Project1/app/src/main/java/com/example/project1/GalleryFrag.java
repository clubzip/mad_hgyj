package com.example.project1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GalleryFrag extends Fragment {
    private View view;
    private Animator currentAnimator;
    private int shortAnimationDuration;

    public static GalleryFrag newInstance() {
        GalleryFrag galleryFrag = new GalleryFrag();
        return galleryFrag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery, container, false);

        final View thumb1View = view.findViewById(R.id.thumb_button_1);
        thumb1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(thumb1View, R.drawable.kim);
            }
        });

        final View thumb2View = view.findViewById(R.id.thumb_button_2);
        thumb2View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view_) {
                zoomImageFromThumb(thumb2View, R.drawable.kim);
            }
        });

        final View thumb3View = view.findViewById(R.id.thumb_button_3);
        thumb3View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view_) {
                zoomImageFromThumb(thumb3View, R.drawable.suelgi);
            }
        });

        final View thumb4View = view.findViewById(R.id.thumb_button_4);
        thumb4View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view_) {
                zoomImageFromThumb(thumb4View, R.drawable.suelgi);
            }
        });

        final View thumb5View = view.findViewById(R.id.thumb_button_5);
        thumb5View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view_) {
                zoomImageFromThumb(thumb5View, R.drawable.beenzino);
            }
        });

        final View thumb6View = view.findViewById(R.id.thumb_button_6);
        thumb6View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view_) {
                zoomImageFromThumb(thumb6View, R.drawable.beenzino);
            }
        });

        final View thumb7View = view.findViewById(R.id.thumb_button_7);
        thumb7View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view_) {
                zoomImageFromThumb(thumb7View, R.drawable.shj);
            }
        });

        final View thumb8View = view.findViewById(R.id.thumb_button_8);
        thumb8View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view_) {
                zoomImageFromThumb(thumb8View, R.drawable.shj);
            }
        });

        final View thumb9View = view.findViewById(R.id.thumb_button_9);
        thumb9View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view_) {
                zoomImageFromThumb(thumb9View, R.drawable.js);
            }
        });

        final View thumb10View = view.findViewById(R.id.thumb_button_10);
        thumb10View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view_) {
                zoomImageFromThumb(thumb10View, R.drawable.js);
            }
        });



        return view;
    }

    private void zoomImageFromThumb(final View thumbView, int imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) view.findViewById(R.id.expanded_image);
        expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        view.findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }
}
