package com.junker.custom.view.customview.rebound;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class JunkerReboundLayout extends LinearLayout {
    private static final String TAG = JunkerReboundLayout.class.getSimpleName();
    private final static String TYPE_ANIMATION_TRANSLATION_Y = "translationY";

    private float downX;                  //首次触摸屏幕时的X坐标
    private float downY;                  //首次触摸屏幕时的Y坐标

    private float difX;                   //停止滑动触摸屏幕前，最新一次的X坐标
    private float difY;                   //停止滑动触摸屏幕前，最新一次的X坐标

    private float offsetX;                //滑动View的X坐标偏移量：offsetX = difX - downX;
    private float offsetY;                //滑动View的Y坐标偏移量：offsetY = difY - downY;

    private float beforeOffsetX;          //下一次触摸屏幕前的剩余X坐标偏移量
    private float beforeOffsetY;          //下一次触摸屏幕前的剩余Y坐标偏移量

    private ObjectAnimator animator;
    private int mDuration = 200;

    public JunkerReboundLayout(Context context) {
        this(context, null);
    }

    public JunkerReboundLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JunkerReboundLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        difY = ev.getRawY();
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downY = difY;
                if (animator != null && animator.isRunning()) {
                    animator.pause();
                    float animatedFraction = animator.getAnimatedFraction();//获取上一次动画已经播放的百分比
                    beforeOffsetY = offsetY - offsetY * animatedFraction;//获取动画还未执行完时，又再次触摸屏幕时的剩余偏移量，便于再次基于当前位置继续滑动
                    animator.cancel();
                } else {
                    beforeOffsetY = 0;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        super.dispatchTouchEvent(ev);
        return true;
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//                Log.e(TAG, "onInterceptTouchEvent -> ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.e(TAG, "onInterceptTouchEvent -> ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                Log.e(TAG, "onInterceptTouchEvent -> ACTION_UP/ACTION_CANCEL");
//                break;
//        }
//        boolean flag = super.onInterceptTouchEvent(ev);
//        Log.e(TAG, "onInterceptTouchEvent -> super.onInterceptTouchEvent(event) is " + flag + " -> return is true");
//        return flag;
//    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                offsetY = (difY - downY) / 2 + beforeOffsetY;
                if (offsetY != 0) {
                    setTranslationY(offsetY);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (animator != null && animator.isRunning()) {
                    animator.cancel();
                }
                animator = ObjectAnimator.ofFloat(this, TYPE_ANIMATION_TRANSLATION_Y, offsetY, 0).setDuration(mDuration);
                animator.start();
                break;
        }
        return super.onTouchEvent(ev);
    }
}
