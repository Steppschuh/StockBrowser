package net.steppschuh.stockbrowser.ui;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.view.View;

import static android.animation.ValueAnimator.ofObject;

public final class AnimationHelper {

    public static final int DEFAULT_FADE_DURATION = 500;

    public static void fadeToOpacity(final View view, float toValue, long duration) {
        fadeToOpacity(view, view.getAlpha(), toValue, duration);
    }

    public static void fadeToOpacity(final View view, float fromValue, float toValue, long duration) {
        ValueAnimator valueAnimator = ofObject(new FloatEvaluator(), fromValue, toValue);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setAlpha((float) animator.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public static void fadeInView(final View view, long duration) {
        float fromValue = 0.0f;
        float toValue = 1.0f;
        view.setAlpha(fromValue);
        view.setVisibility(View.VISIBLE);
        view.requestLayout();
        ValueAnimator valueAnimator = ofObject(new FloatEvaluator(), fromValue, toValue);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setAlpha((float) animator.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public static void fadeOutView(final View view, long duration) {
        if (view.getVisibility() != View.VISIBLE) {
            return;
        }

        float fromValue = 1.0f;
        float toValue = 0.0f;
        view.setAlpha(fromValue);
        view.requestLayout();
        ValueAnimator valueAnimator = ofObject(new FloatEvaluator(), fromValue, toValue);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setAlpha((float) animator.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }
}
