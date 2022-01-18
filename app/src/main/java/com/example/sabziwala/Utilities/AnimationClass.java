package com.example.sabziwala.Utilities;


import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;


public class AnimationClass {

    final static int durationalpha=500;
    final static int durationtransition=400;
    final static int durationscale=500;

    public static void setAnimationLTR(View view) {
        AnimationSet set = new AnimationSet(true);
        AlphaAnimation anim1 = new AlphaAnimation(0.0f, 1.0f);

        TranslateAnimation anim2 = new TranslateAnimation(-200, 0, 0, 0);

        ScaleAnimation anim3 = new ScaleAnimation(1.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(durationalpha);
        anim2.setDuration(durationtransition);
        anim3.setDuration(durationscale);
        set.addAnimation(anim1);
        set.addAnimation(anim2);
        set.addAnimation(anim3);
        view.startAnimation(set);
    }
    public static void setAnimationParent(View view) {
        AnimationSet set = new AnimationSet(true);
        AlphaAnimation anim1 = new AlphaAnimation(0.0f, 1.0f);
        TranslateAnimation anim2 = new TranslateAnimation(0, 0, 0, 0);
        ScaleAnimation anim3 = new ScaleAnimation(0.95f, 1.0f, 0.95f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(durationalpha);
        anim2.setDuration(durationtransition);
        anim3.setDuration(durationscale+200);
        set.addAnimation(anim1);
        set.addAnimation(anim2);
        set.addAnimation(anim3);
        view.startAnimation(set);
    }public static void setAnimationChildZoom(View view) {
        AnimationSet set = new AnimationSet(true);
        AlphaAnimation anim1 = new AlphaAnimation(0.0f, 1.0f);
        TranslateAnimation anim2 = new TranslateAnimation(0, 0, 0, 0);
        ScaleAnimation anim3 = new ScaleAnimation(0.90f, 1.0f, 0.90f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(durationalpha+200);
        anim2.setDuration(durationtransition);
        anim3.setDuration(durationscale+400);
        set.addAnimation(anim1);
        set.addAnimation(anim2);
        set.addAnimation(anim3);
        view.startAnimation(set);
    }public static void setAnimationAlert(View view) {
        AnimationSet set = new AnimationSet(true);
        AlphaAnimation anim1 = new AlphaAnimation(1f, 1.0f);
        TranslateAnimation anim2 = new TranslateAnimation(0, 0, 0, 0);
        ScaleAnimation anim3 = new ScaleAnimation(2f, 1.0f, 2f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setInterpolator(new OvershootInterpolator());
        anim2.setInterpolator(new OvershootInterpolator());
        anim3.setInterpolator(new BounceInterpolator());
        anim1.setDuration(durationalpha);
        anim2.setDuration(durationtransition);
        anim3.setDuration(durationscale-200);
        set.addAnimation(anim1);
        set.addAnimation(anim2);
        set.addAnimation(anim3);
        view.startAnimation(set);
    }public static void setAnimationRotateSquare(View view) {
        AnimationSet set = new AnimationSet(true);
        AlphaAnimation anim1 = new AlphaAnimation(1f, 1.0f);
        TranslateAnimation anim2 = new TranslateAnimation(0, 0, 0, 0);
        ScaleAnimation anim3 = new ScaleAnimation(2f, 1.0f, 2f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        RotateAnimation anim4=new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        anim1.setInterpolator(new OvershootInterpolator());
        anim2.setInterpolator(new OvershootInterpolator());
        anim3.setInterpolator(new BounceInterpolator());
        anim1.setDuration(durationalpha);
        anim2.setDuration(durationtransition);
        anim3.setDuration(durationscale-200);
        anim4.setDuration(durationscale);
        set.addAnimation(anim1);
        set.addAnimation(anim2);
//        set.addAnimation(anim3);
        set.addAnimation(anim4);
        view.startAnimation(set);
    }public static void setAnimationRotate360(View view) {
        AnimationSet set = new AnimationSet(true);
        AlphaAnimation anim1 = new AlphaAnimation(1f, 1.0f);
        TranslateAnimation anim2 = new TranslateAnimation(0, 0, 0, 0);
        ScaleAnimation anim3 = new ScaleAnimation(2f, 1.0f, 2f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        RotateAnimation anim4=new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        anim1.setInterpolator(new OvershootInterpolator());
        anim2.setInterpolator(new OvershootInterpolator());
        anim3.setInterpolator(new BounceInterpolator());
        anim1.setDuration(durationalpha);
        anim2.setDuration(durationtransition);
        anim3.setDuration(durationscale-200);
        anim4.setDuration(durationscale);
        set.addAnimation(anim1);
        set.addAnimation(anim2);
//        set.addAnimation(anim3);
        set.addAnimation(anim4);
        view.startAnimation(set);
    }
}
