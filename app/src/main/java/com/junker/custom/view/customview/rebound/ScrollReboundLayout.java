package com.junker.custom.view.customview.rebound;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * 关于 ScrollView 中 onOverScrolled、dispatchTouchEvent、onTouchEvent 调用顺序
 * dispatchTouchEvent -> onTouchEvent -> onOverScrolled
 */
public class ScrollReboundLayout extends ScrollView {
    private static final String TAG = ScrollReboundLayout.class.getSimpleName();
    private final static String TYPE_ANIMATION_TRANSLATION_Y = "translationY";

    private boolean isScrolledToTop = false; //判断ScrollView是否滑动到顶部
    private boolean isScrolledToBottom = false; //判断ScrollView是否滑动到顶部

    private float downX;                  //首次触摸屏幕时的X坐标
    private float downY;                  //首次触摸屏幕时的Y坐标

    private float difX;                   //停止滑动触摸屏幕前，最新一次的X坐标
    private float difY;                   //停止滑动触摸屏幕前，最新一次的X坐标

    private float offsetX;                //手指按下屏幕滑动View的X坐标偏移量：offsetX = difX - downX;
    private float offsetY;                //手指按下屏幕滑动View的Y坐标偏移量：offsetY = difY - downY;

    private float beforeOffsetX;          //下一次触摸屏幕前的剩余X坐标偏移量
    private float beforeOffsetY;          //下一次触摸屏幕前的剩余Y坐标偏移量

    private ObjectAnimator animator;
    private int mDuration = 200;

    public ScrollReboundLayout(Context context) {
        this(context, null);
    }

    public ScrollReboundLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollReboundLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOverScrollMode(OVER_SCROLL_NEVER);// 去除原本ScrollView滚动到边界时的阴影效果
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent...");
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
                offsetY = (difY - downY) / 2 + beforeOffsetY;
                isScrolledToTop = getScrollY() == 0;
                View contentView = getChildAt(0);
                if (contentView == null || contentView.getMeasuredHeight() == (getScrollY() + getHeight())) {
                    isScrolledToBottom = true;
                } else {
                    isScrolledToBottom = false;
                }

                Log.e(TAG, "offsetY = " + offsetY
                        + " <:> " + "translation-Y = " + getTranslationY()
                        + " <:> " + "isScrolledToTop = " + isScrolledToTop
                        + " <:> " + "isScrolledToBottom = " + isScrolledToBottom);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onTouchEvent...");
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (isScrolledToTop || isScrolledToBottom) {
                    setTranslationY(offsetY);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if ((isScrolledToTop || isScrolledToBottom) && Math.abs(offsetY) > 0) {
                    if (animator != null && animator.isRunning()) {
                        animator.cancel();
                    }
                    animator = ObjectAnimator.ofFloat(this, TYPE_ANIMATION_TRANSLATION_Y, offsetY, 0).setDuration(mDuration);
                    //设置阻尼动画效果
                    animator.start();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
//        Log.e(TAG, "onOverScrolled...");
        if (scrollY == 0) {
//            Log.e(TAG, "onOverScrolled isScrolledToTop:" + clampedY);
        } else {
//            Log.e(TAG, "onOverScrolled isScrolledToBottom:" + clampedY);
        }
    }

    public static class DampInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float input) {
            //没看过源码，猜测是input是时间（0-1）,返回值应该是进度（0-1）
            //先快后慢，为了更快更慢的效果，多乘了几次，现在这个效果比较满意
            return 1 - (1 - input) * (1 - input) * (1 - input) * (1 - input) * (1 - input);
        }
    }
}
