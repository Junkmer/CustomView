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

    private boolean isScrolledToTop = false;    //判断ScrollView是否滑动到顶部
    private boolean isScrolledToBottom = false; //判断ScrollView是否滑动到底部

    private boolean isCurrentTouchToTop = false;      //当前手指按下屏幕是否处于顶部
    private boolean isCurrentTouchToBottom = false;   //当前手指按下屏幕是否处于底部

    private float downX;                        //首次触摸屏幕时的X坐标
    private float downY;                        //首次触摸屏幕时的Y坐标

    private float difX;                         //停止滑动触摸屏幕前，最新一次的X坐标
    private float difY;                         //停止滑动触摸屏幕前，最新一次的X坐标

//    private float moveX;                        //手指在屏幕上按下移动的 X坐标位移
//    private float moveY;                        //手指在屏幕上按下移动的 Y坐标位移

    private float scrollX;                      //触摸屏幕滑动时 scrollView 滑动的 X坐标位移
    private float scrollY;                      //触摸屏幕滑动时 scrollView 滑动的 Y坐标位移

    private float offsetX;                      //手指按下屏幕滑动View的X坐标偏移量
    private float offsetY;                      //手指按下屏幕滑动View的Y坐标偏移量

    private float beforeOffsetX;                //下一次触摸屏幕前的剩余X坐标偏移量
    private float beforeOffsetY;                //下一次触摸屏幕前的剩余Y坐标偏移量

    private boolean isOffset;                   //是否发生了偏移

    private boolean isCanScroll;                //是否可以滚动

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
        difY = ev.getRawY();
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downY = difY;
                isCurrentTouchToTop = getScrollY() == 0;
                isCurrentTouchToBottom = getChildAt(0) == null || getChildAt(0).getMeasuredHeight() == (getScrollY() + getHeight());
//                if (animator != null && animator.isRunning()) {
//                    animator.pause();
//                    float animatedFraction = animator.getAnimatedFraction();//获取上一次动画已经播放的百分比
//                    beforeOffsetY = offsetY - offsetY * animatedFraction;//获取动画还未执行完时，又再次触摸屏幕时的剩余偏移量，便于再次基于当前位置继续滑动
//                    animator.cancel();
//                } else {
//                    beforeOffsetY = 0;
//                }
                break;
            case MotionEvent.ACTION_MOVE:
                isScrolledToTop = getScrollY() == 0;
                isScrolledToBottom = getChildAt(0) == null || getChildAt(0).getMeasuredHeight() == (getScrollY() + getHeight());

                if (isCurrentTouchToTop || isCurrentTouchToBottom) {
                    scrollY = 0;
                    isCurrentTouchToTop = false;
                    isCurrentTouchToBottom = false;
                    Log.e(TAG, "0000000000000000000000000000000000000");
                } else {
                    if (!isScrolledToBottom && offsetY < 0) {
                        scrollY = difY - downY;
                        Log.e(TAG, "1111111111111111111111111111111111");
                        isCanScroll = true;
                    } else if (!isScrolledToTop && offsetY > 0) {
                        isCanScroll = true;
                        scrollY = difY - downY;
                        Log.e(TAG, "2222222222222222222222222222222222");
                    }else {
                        isCanScroll = false;
                        Log.e(TAG, "3333333333333333333333333333333333");
                    }
                }

                offsetY = (difY - downY - scrollY) / 2 + beforeOffsetY;

//                Log.e(TAG, "offsetY = " + offsetY
//                        + " <:> " + "translation-Y = " + getTranslationY()
//                        + " <:> " + "isScrolledToTop = " + isScrolledToTop
//                        + " <:> " + "isScrolledToBottom = " + isScrolledToBottom
//                        + " <:> " + "scrollY = " + scrollY
//                        + " <:> " + "isCanScroll = " + isCanScroll);
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
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (isScrolledToTop || isScrolledToBottom || Math.abs(offsetY) > 0) {
                    setTranslationY(offsetY);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if ((isScrolledToTop || isScrolledToBottom) || Math.abs(offsetY) > 0) {

                    if (animator != null && animator.isRunning()) {
                        animator.cancel();
                    }
                    animator = ObjectAnimator.ofFloat(this, TYPE_ANIMATION_TRANSLATION_Y, offsetY, 0).setDuration(mDuration);
                    //设置阻尼动画效果
                    animator.start();
                }
                break;
        }
        if (isCanScroll) { //自身消费，阻止向下传递，实现ScrollView不滚动。
            return super.onTouchEvent(ev);
        } else {
            return true;
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if (isCanScroll) {
            super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
            Log.e(TAG, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        } else {
            Log.e(TAG, "bbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        }
//        Log.e(TAG, "onOverScrolled..."
//                + " scrollX = " + scrollX
//                + " <:> " + "scrollY = " + scrollY
//                + " <:> " + "clampedX = " + clampedX
//                + " <:> " + "clampedY = " + clampedY);

    }

    @Override
    protected void onScrollChanged(int left, int top, int oldLeft, int oldTop) {
//        if (top != 0 || top != (getMeasuredHeight() - getHeight())){
//            isCanScroll = true;
//        }
    }

    /**
     * 是否需要Y移动布局
     */
    public boolean isNeedTranslate() {
        int offset = getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        // 顶部或者底部
        return scrollY == 0 || scrollY == offset;
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
