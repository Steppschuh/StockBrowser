package net.steppschuh.stockbrowser.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionSet;

public final class TransitionHelper {

    /**
     * Helper method used to chain multiple transitions into a set
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static TransitionSet sequence(Transition... transitions) {
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
        for (Transition transition: transitions) {
            transitionSet.addTransition(transition);
        }
        return transitionSet;
    }

}
