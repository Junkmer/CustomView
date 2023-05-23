package com.junker.custom.view.customview.rebound;

import android.animation.Animator;
import android.animation.AnimatorSet;
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
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.ScrollView;

import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;

/**
 * 关于 ScrollView 中 onOverScrolled、dispatchTouchEvent、onTouchEvent 调用顺序
 * dispatchTouchEvent -> onTouchEvent -> onOverScrolled
 */
public class ScrollReboundLayout extends ScrollView {
    private static final String TAG = ScrollReboundLayout.class.getSimpleName();
    private final static String TYPE_ANIMATION_TRANSLATION_Y = "translationY";

    private boolean isCurrentTouchToTop = false;        //当前手指按下屏幕是否处于顶部
    private boolean isCurrentTouchToBottom = false;     //当前手指按下屏幕是否处于底部

    private int downX;                                  //首次触摸屏幕时的X坐标
    private int downY;                                  //首次触摸屏幕时的Y坐标

    private int difX;                                   //停止滑动触摸屏幕前，最新一次的X坐标
    private int difY;                                   //停止滑动触摸屏幕前，最新一次的X坐标

    private int moveX;                                  //手指在屏幕上按下移动的 X坐标位移
    private int moveY;                                  //手指在屏幕上按下移动的 Y坐标位移

    private int scrollX;                                //触摸屏幕滑动时 scrollView 滑动的 X坐标位移
    private int scrollY;                                //触摸屏幕滑动时 scrollView 滑动的 Y坐标位移

    private int offsetX;                                //手指按下屏幕滑动View的X坐标偏移量
    private int offsetY;                                //手指按下屏幕滑动View的Y坐标偏移量

    private int beforeOffsetX;                          //下一次触摸屏幕前的剩余X坐标偏移量
    private int beforeOffsetY;                          //下一次触摸屏幕前的剩余Y坐标偏移量

    private boolean isCanScroll;                        //是否可以滚动

    private boolean isClamped = true;                   //ScrollView滑动是否被阻止

    private boolean isTouchState;                       //是否处于触摸屏幕状态

    private int currentTouchScrollY;                    //按下屏幕时的，scrollY 位置

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

    //                Log.e(TAG,
//                        "isCurrentTouchToTop = " + isCurrentTouchToTop
//                                + " <:> " + "isCurrentTouchToBottom = " + isCurrentTouchToBottom
//                                + " <:> " + "isScrolledToBottom = " + isScrolledToBottom
//                                + " <:> " + "isScrolledToTop = " + isScrolledToTop);


//                Log.e(TAG,
//                        "difY = " + difY
//                                + " <:> " + "downY = " + downY
//                                + " <:> " + "scrollY = " + scrollY
//                                + " <:> " + "offsetY = " + offsetY
//                                + " <:> " + "beforeOffsetY = " + beforeOffsetY
//                                + " <:> " + "translation-Y = " + getTranslationY()
//                                + " <:> " + "isScrolledToTop = " + isCurrentTouchToTop
//                                + " <:> " + "isScrolledToBottom = " + isCurrentTouchToBottom
//                                + " <:> " + "getScrollY() = " + getScrollY()
//                                + " <:> " + "isCanScroll = " + isCanScroll);

//                    Log.e(TAG,
//            "difY = " + difY
//                                + " <:> " + "downY = " + downY
//                                + " <:> " + "scrollY = " + scrollY
//                                + " <:> " + "offsetY = " + offsetY
//                                + " <:> " + "beforeOffsetY = " + beforeOffsetY
//                                + " <:> " + "translation-Y = " + getTranslationY()
//                                + " <:> " + "isScrolledToTop = " + isCurrentTouchToTop
//                                + " <:> " + "isScrolledToBottom = " + isCurrentTouchToBottom
//                                + " <:> " + "currentTouchScrollY = " + currentTouchScrollY
//                                + " <:> " + "currentHeight = " + currentHeight
//                                + " <:> " + "measuredHeight = " + measuredHeight
//                                + " <:> " + "isCanScroll = " + isCanScroll
//                                + " <:> " + "isScrollMiddle = " + isScrollMiddle
//                );

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        difY = (int) ev.getRawY();
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downY = difY;
                scrollY = 0;
                isCurrentTouchToTop = isScrolledToTop();
                isCurrentTouchToBottom = isScrolledToBottom();
                isTouchState = true;
                if (animator != null && animator.isRunning()) {
                    animator.pause();
                    float animatedFraction = animator.getAnimatedFraction();//获取上一次动画已经播放的百分比
                    beforeOffsetY = (int) (offsetY - offsetY * animatedFraction);//获取动画还未执行完时，又再次触摸屏幕时的剩余偏移量，便于再次基于当前位置继续滑动
                    animator.cancel();
                } else {
                    isCanScroll = true;
                    beforeOffsetY = 0;
                }
                offsetY = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                currentTouchScrollY = getScrollY();

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void stopNestedScroll() {
        super.stopNestedScroll();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = difY - downY;

                int currentHeight = getHeight() + currentTouchScrollY;
                int measuredHeight = getChildAt(0).getMeasuredHeight();

                if (isCurrentTouchToTop) {
                    offsetY = moveY / 2 + beforeOffsetY;
                    if (offsetY > 0) {
                        isCanScroll = false;
                    } else {
                        offsetY = 0;
                        isCanScroll = true;
                    }
                } else if (isCurrentTouchToBottom) {
                    offsetY = moveY / 2 + beforeOffsetY;
                    if (offsetY < 0) {
                        isCanScroll = false;
                    } else {
                        offsetY = 0;
                        isCanScroll = true;
                    }
                } else {
                    if (isCanScroll && 0 < currentTouchScrollY && currentHeight < measuredHeight) {
                        scrollY = moveY;
                        offsetY = 0;
                    } else {
                        offsetY = (moveY - scrollY) / 2 + beforeOffsetY;
                        if (offsetY > 0 && isScrolledToTop()) {
                            isCanScroll = false;
                        } else if (offsetY < 0 && isScrolledToBottom()) {
                            isCanScroll = false;
                        } else {
                            offsetY = 0;
                            isCanScroll = true;
                        }
                    }
                }

                setTranslationY(offsetY);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (Math.abs(offsetY) > 0) {
                    if (animator != null && animator.isRunning()) {
                        animator.cancel();
                    }
                    animator = ObjectAnimator.ofFloat(this, TYPE_ANIMATION_TRANSLATION_Y, offsetY, 0).setDuration(mDuration);
                    //设置阻尼动画效果
                    animator.start();
                }
                isTouchState = false;
                break;
        }
        if (isCanScroll) { //自身消费，阻止向下传递，实现ScrollView不滚动。
            return super.onTouchEvent(ev);
        } else {
            return true;
        }
    }

    private int lastTouchScrollY;
    private long lastTouchTime;
    private final static double DEFAULT_DAMPED_REBOUND_VALUE = 7.5;

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if (isCanScroll) {
            if (!isClamped) {
                super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
            }
            if (isTouchState) {
                lastTouchScrollY = scrollY;
                lastTouchTime = System.currentTimeMillis();
            }
            isClamped = clampedY;
            if (clampedY && !isTouchState) {
                double scrollTime = System.currentTimeMillis() - lastTouchTime;
                double scrollOffsetY = Math.abs(scrollY - lastTouchScrollY);
                float offsetYY = (float) ((scrollOffsetY / scrollTime) * DEFAULT_DAMPED_REBOUND_VALUE);
                if (offsetYY < 0) {
                    offsetYY = 0;
                }
                float translationY = scrollY == 0 ? offsetYY : -offsetYY;
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(this, TYPE_ANIMATION_TRANSLATION_Y, 0, translationY, 0).setDuration(300);
                animator1.setInterpolator(new DecelerateInterpolator());
                animator1.start();
            }
        }
    }

    public boolean isScrolledToTop() {
        return getScrollY() == 0;
    }

    public boolean isScrolledToBottom() {
        return getChildAt(0) == null || getChildAt(0).getMeasuredHeight() == (getScrollY() + getHeight());
    }
}
