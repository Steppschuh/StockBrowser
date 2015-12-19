package net.steppschuh.stockbrowser.ui;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;

import net.steppschuh.stockbrowser.R;

import static android.animation.ValueAnimator.ofObject;

public final class ColorHelper {

    public static final float HARD_OVERLAY_ALPHA = 0.66f;
    public static final float SOFT_OVERLAY_ALPHA = 0.33f;


    /**
     * Reduces the alpha value of a given color by a given factor
     */
    public static int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    /**
     * Returns the background color of a given view if present,
     * transparent if not
     */
    public static int getBackgroundColor(View view) {
        int color = Color.TRANSPARENT;
        Drawable background = view.getBackground();
        if (background instanceof ColorDrawable) {
            color = ((ColorDrawable) background).getColor();
        }
        return color;
    }

    public static void fadeBackgroundColor(final View view, int colorTo) {
        fadeBackgroundColor(view, getBackgroundColor(view), colorTo, AnimationHelper.DEFAULT_FADE_DURATION);
    }

    /**
     * Animates a background color change of a given view
     */
    public static void fadeBackgroundColor(final View view, int colorFrom, int colorTo, int duration) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((Integer) animator.getAnimatedValue());
            }
        });
        colorAnimation.setDuration(duration);
        colorAnimation.start();
    }

    public static void fadeStatusBarToDefaultColor(final Activity activity) {
        fadeStatusBarToColor(activity, ContextCompat.getColor(activity, R.color.colorPrimary), AnimationHelper.DEFAULT_FADE_DURATION);
    }

    public static void fadeStatusBarToColor(final Activity activity, int targetColor, long duration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int currentColor = activity.getWindow().getStatusBarColor();
            ValueAnimator colorAnimation = ofObject(new ArgbEvaluator(), currentColor, targetColor);
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @SuppressLint("NewApi")
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    activity.getWindow().setStatusBarColor((Integer) animator.getAnimatedValue());
                }
            });
            colorAnimation.setDuration(duration);
            colorAnimation.start();
        }
    }

}
