package net.steppschuh.stockbrowser.ui;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

public final class ColorHelper {

    public static final int DEFAULT_FADE_DURATION = 500;

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
        fadeBackgroundColor(view, getBackgroundColor(view), colorTo, DEFAULT_FADE_DURATION);
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

}
