package com.example.xyzreader.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;

/**
 * Created by emil.ivanov on 6/6/18.
 */
public class UtilsTransitions {


    public static void createSlideTransition(Context context, Window window, View viewToAnimate, int direction, int interpolator) {

        Slide slide = new Slide(direction);
        slide.addTarget(viewToAnimate);
        slide.setInterpolator(AnimationUtils.loadInterpolator(context, interpolator));
        slide.setDuration(500);
        window.setEnterTransition(slide);
    }


}
