/**
 * (주)오픈잇 | http://www.openit.co.kr
 * Copyright (c)2006-2017, openit Inc.
 * All rights reserved.
 */
package com.sbproject.miribyul.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.sbproject.miribyul.R;
import com.sbproject.miribyul.listener.AnimationListener;

public class AnimationInOut {
    public static final int LEFT_IN = 7001;
    public static final int RIGHT_IN = 7002;
    public static final int BOTTOM_OUT = 7003;
    public static final int BOTTOM_IN = 7004;
    private Animation animTransInAlphaLeft;
    private Animation animTransOutAlphaLeft;
    private Animation animTransInAlphaRight;
    private Animation animTransOutAlphaRight;
    private Animation animTransOutAlphaBottom;
    private Animation animTransInAlphaBottom;
    private int directionIn;
    private Context context;
    private View view;
    private AnimationListener listener;
    private boolean isPlay = false;

    public AnimationInOut(Context context, AnimationListener listener) {
        this.context = context;
        this.listener = listener;
        animTransInAlphaLeft = AnimationUtils.loadAnimation(context, R.anim.anim_slide_in_left);
        animTransOutAlphaLeft = AnimationUtils.loadAnimation(context, R.anim.anim_slide_out_left);
        animTransInAlphaRight = AnimationUtils.loadAnimation(context, R.anim.anim_slide_in_right);
        animTransOutAlphaRight = AnimationUtils.loadAnimation(context, R.anim.anim_slide_out_right);
        animTransOutAlphaBottom = AnimationUtils.loadAnimation(context, R.anim.anim_slide_out_bottom);
        animTransInAlphaBottom = AnimationUtils.loadAnimation(context, R.anim.anim_slide_in_bottom);
        animTransInAlphaLeft.setAnimationListener(end);
        animTransInAlphaRight.setAnimationListener(end);
        animTransOutAlphaLeft.setAnimationListener(leftOut);
        animTransOutAlphaRight.setAnimationListener(rightOut);
        animTransOutAlphaBottom.setAnimationListener(bottom);
        animTransInAlphaBottom.setAnimationListener(bottom);
    }

    public void startAnimation(View view, int directionIn) {
        if (!isPlay) {
            isPlay = true;
            if (LEFT_IN == directionIn) {
                view.startAnimation(animTransOutAlphaRight);
            } else if (RIGHT_IN == directionIn) {
                view.startAnimation(animTransOutAlphaLeft);
            } else if (BOTTOM_OUT == directionIn) {
                view.startAnimation(animTransOutAlphaBottom);
            } else if (BOTTOM_IN == directionIn) {
                view.setVisibility(View.VISIBLE);
                view.startAnimation(animTransInAlphaBottom);
            } else {
                isPlay = false;
            }
            if (isPlay) {
                this.view = view;
                this.directionIn = directionIn;
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            listener.onEvent(directionIn);
        }
    };
    Animation.AnimationListener end = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            isPlay = false;
            listener.onEnd(directionIn);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
    Animation.AnimationListener leftOut = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            listener.onStart(directionIn);
            handler.sendEmptyMessageDelayed(0, 100);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (LEFT_IN == directionIn) {
                view.startAnimation(animTransInAlphaLeft);
            } else if (RIGHT_IN == directionIn) {
                view.startAnimation(animTransInAlphaRight);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
    Animation.AnimationListener rightOut = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            listener.onStart(directionIn);
            handler.sendEmptyMessageDelayed(0, 300);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (LEFT_IN == directionIn) {
                view.startAnimation(animTransInAlphaLeft);
            } else if (RIGHT_IN == directionIn) {
                view.startAnimation(animTransInAlphaRight);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
    Animation.AnimationListener bottom = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            listener.onStart(directionIn);
            if (BOTTOM_IN == directionIn) {
                handler.sendEmptyMessage(0);
            } else if (BOTTOM_OUT == directionIn) {
                handler.sendEmptyMessageDelayed(0, 200);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            isPlay = false;
            listener.onEnd(directionIn);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

}
