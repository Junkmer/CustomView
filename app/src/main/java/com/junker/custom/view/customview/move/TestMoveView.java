package com.junker.custom.view.customview.move;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;

public class TestMoveView extends AppCompatButton {
    private static final String TAG = TestMoveView.class.getSimpleName();

    public final static int MOVE_VIEW_STYLE_LAYOUT = 0;//通过设置 layout 移动view
    public final static int MOVE_VIEW_STYLE_TRANSLATION = 1;//通过设置 translation 移动view
    public final static int MOVE_VIEW_STYLE_OFFSET = 2;//通过设置 offset 移动view
    public final static int MOVE_VIEW_STYLE_SCROLL_BY = 3;//通过设置 scroll_by 移动view
    public final static int MOVE_VIEW_STYLE_SCROLL = 4;//通过设置 scroll 移动view
    public final static int MOVE_VIEW_STYLE_MARGIN = 5;//通过设置 margin 移动view
    public final static int MOVE_VIEW_STYLE_ANIMATION = 6;//通过设置 animation 移动view

    private final static int TOUCH_INTERVAL_TIME = 300;//单位毫秒

    private int layoutX;
    private int layoutY;
    private int translationX;
    private int translationY;
    private int offsetX;
    private int offsetY;
    private int scrollByX;
    private int scrollByY;
    private int scrollX;
    private int scrollY;
    private int marginX;
    private int marginY;
    private int animationX;
    private int animationY;

    private int difX;
    private int difY;
    private float currentX;
    private float currentY;

    //用于控制设置 onClickListener 时间后，移动view 之后不再触发 点击事件
    private long mDownTouchTime;
    private long mUpTouchTime;

    /**
     * 设置移动view方式
     * 0：layout
     * 1：setTranslationX / setTranslationY
     * 2：offsetTopAndBottom / offsetLeftAndRight
     * 3：scrollBy
     * 4；setScrollX / setScrollY、scrollTo
     * 5：setMargins
     * 6：setAnimation
     */
    private int mMoveViewStyle = 0;

    public TestMoveView(Context context) {
        this(context, null);
    }

    public TestMoveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestMoveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr();
    }

    private void initAttr() {
        mMoveViewStyle = MOVE_VIEW_STYLE_LAYOUT;//通过设置 layout 移动view
    }

    /**
     * View.getLeft():      表示：子View的左边界到父View的左边界的距离
     * View.getTop():       表示：子View的顶部到父View顶部的距离
     * View.getRight():     表示：子View的右边界到父View的左边界的距离
     * View.getBottom():    表示：子View的底部到父View的顶部的距离
     * <p>
     * View.getWidth():     表示：当前控件的宽度，即getRight()-getLeft()
     * View.getHeight():    表示：当前控件的高度，即getBottom()-getTop()
     * <p>
     * event.getX():        表示：触摸点相对于自身view的 左上角X坐标
     * event.getY():        表示：触摸点相对于自身view的 左上角Y坐标
     * <p>
     * event.getRawX:       表示：触摸点距离屏幕左边界的距离
     * event.getRawY:       表示：触摸点距离屏幕上边界的距离
     * <p>
     * View.getTranslationX() 表示：该View在X轴的偏移量。初始值为0，向左偏移值为负，向右偏移值为正。
     * View.getTranslationY() 表示：该View在Y轴的偏移量。初始值为0，向上偏移为负，向下偏移为正。
     * <p>
     * getScrollX()         表示：该View的内容在View自身X轴的偏移量。初始值为0，向左偏移值为负，向右偏移值为正。
     * getScrollY()         表示：该View的内容在View自身Y轴的偏移量。初始值为0，向上偏移为负，向下偏移为正。
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        logcat(event);
        dispatchTouchEventT(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "dispatchTouchEvent, ACTION_DOWN");
                dispatchTouchEvent2ACTION_DOWN();
                mDownTouchTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "dispatchTouchEvent, ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mUpTouchTime = System.currentTimeMillis();
                Log.e(TAG, "dispatchTouchEvent -> MotionEvent.ACTION_CANCEL/MotionEvent.ACTION_UP");
                break;
        }
        boolean b = super.dispatchTouchEvent(event);
        Log.i(TAG, "dispatchTouchEvent -> super.dispatchTouchEvent(event) is " + b);
        return b;
    }

    private void logcat(MotionEvent event){
        Log.e(TAG, "dispatchTouchEvent \nleft = " + getLeft() + "    top = " + getTop() + "    right = " + getRight() + "    bottom = " + getBottom() + "\n"
                + "getX = " + event.getX() + "\n"
                + "getY = " + event.getY() + "\n"
                + "getRawX = " + event.getRawX() + "\n"
                + "getRawY = " + event.getRawY() + "\n"
                + "getScrollX = " + getScrollX() + "\n"
                + "getScrollY = " + getScrollY() + "\n"
                + "getTranslationX = " + getTranslationX() + "\n"
                + "getTranslationY = " + getTranslationY() + "\n"
        );
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "触发 onTouchEvent 事件");
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent, ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent, ACTION_MOVE");
                onTouchEvent2ACTION_MOVE();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "onTouchEvent, ACTION_MOVE/ACTION_POINTER_UP");
                onTouchEvent2ACTION_CANCEL();
                if (mUpTouchTime - mDownTouchTime > TOUCH_INTERVAL_TIME){
                    return true;
                }
                break;
        }
        boolean b = super.onTouchEvent(event);
        Log.i(TAG, "onTouchEvent -> super.onTouchEvent(event) is " + b);
        return b;
    }

    public int getMoveViewStyle() {
        return mMoveViewStyle;
    }

    public void setMoveViewStyle(int mMoveViewStyle) {
        this.mMoveViewStyle = mMoveViewStyle;
    }

    private void dispatchTouchEventT(MotionEvent event) {
        if (mMoveViewStyle == MOVE_VIEW_STYLE_LAYOUT) {
            difX = (int) event.getX();
            difY = (int) event.getY();
        } else if (mMoveViewStyle == MOVE_VIEW_STYLE_TRANSLATION) {
            difX = (int) event.getRawX();
            difY = (int) event.getRawY();
        } else if (mMoveViewStyle == MOVE_VIEW_STYLE_OFFSET) {
            difX = (int) event.getX();
            difY = (int) event.getY();
        } else if (mMoveViewStyle == MOVE_VIEW_STYLE_SCROLL_BY) {
            difX = (int) event.getX();
            difY = (int) event.getY();
        } else if (mMoveViewStyle == MOVE_VIEW_STYLE_SCROLL) {
            difX = (int) event.getX();
            difY = (int) event.getY();
        } else if (mMoveViewStyle == MOVE_VIEW_STYLE_MARGIN) {
            difX = (int) event.getX();
            difY = (int) event.getY();
        } else if (mMoveViewStyle == MOVE_VIEW_STYLE_ANIMATION) {
            difX = (int) event.getX();
            difY = (int) event.getY();
        }
    }

    private void dispatchTouchEvent2ACTION_DOWN() {
        if (mMoveViewStyle == MOVE_VIEW_STYLE_LAYOUT) {
            layoutX = difX;
            layoutY = difY;
        } else if (mMoveViewStyle == MOVE_VIEW_STYLE_TRANSLATION) {
            translationX = difX;
            translationY = difY;
        } else if (mMoveViewStyle == MOVE_VIEW_STYLE_OFFSET) {
            offsetX = difX;
            offsetY = difY;
        } else if (mMoveViewStyle == MOVE_VIEW_STYLE_SCROLL_BY) {
            scrollByX = difX;
            scrollByY = difY;
        } else if (mMoveViewStyle == MOVE_VIEW_STYLE_SCROLL) {
            scrollX = difX;
            scrollY = difY;
        } else if (mMoveViewStyle == MOVE_VIEW_STYLE_MARGIN) {
            marginX = difX;
            marginY = difY;
        } else if (mMoveViewStyle == MOVE_VIEW_STYLE_ANIMATION) {
            animationX = difX;
            animationY = difY;
        }
    }

    private void onTouchEvent2ACTION_MOVE() {
        int changeX;
        int changeY;
        if (mMoveViewStyle == MOVE_VIEW_STYLE_LAYOUT) {
            // 计算偏移量
            changeX = difX - layoutX;
            changeY = difY - layoutY;
            Log.e(TAG, "onTouchEvent, ACTION_MOVE - LAYOUT -> changeX=" + changeX + "|changeY=" + changeY);
            // 在当前 left、top、right、bottom的基础上加上偏移量
            layout(getLeft() + changeX, getTop() + changeY, getRight() + changeX, getBottom() + changeY);
        } else if (mMoveViewStyle == MOVE_VIEW_STYLE_TRANSLATION) {
            // 计算偏移量
            changeX = difX - translationX;
            changeY = difY - translationY;
            Log.e(TAG, "onTouchEvent, ACTION_MOVE - TRANSLATION -> changeX=" + changeX + "|changeY=" + changeY);
            setTranslationX(currentX + changeX);
            setTranslationY(currentY + changeY);
        } else if (mMoveViewStyle == MOVE_VIEW_STYLE_OFFSET) {
            changeX = difX - offsetX;
            changeY = difY - offsetY;
            Log.e(TAG, "onTouchEvent, ACTION_MOVE - OFFSET -> changeX=" + changeX + "|changeY=" + changeY);
            offsetLeftAndRight(changeX);//对应X坐标
            offsetTopAndBottom(changeY);//对应Y坐标
        } else if (mMoveViewStyle == MOVE_VIEW_STYLE_SCROLL_BY) {
            // 计算偏移量
            int offsetX = scrollByX - difX;
            int offsetY = scrollByY - difY;
            Log.e(TAG, "onTouchEvent, ACTION_MOVE - SCROLL_BY -> offsetX=" + offsetX + "|offsetY=" + offsetY);
            scrollBy(offsetX, offsetY);
            scrollByX = difX;
            scrollByY = difY;
        } else if (mMoveViewStyle == MOVE_VIEW_STYLE_SCROLL) {
            // 计算偏移量
            int offsetX = scrollX - difX + getScrollX();//使用scrollTo设置控件移动，偏移的距离=之前已经偏移的距离+本次手势滑动了多大距离
            int offsetY = scrollY - difY + getScrollY();
            Log.e(TAG, "onTouchEvent, ACTION_MOVE - SCROLL -> offsetX=" + offsetX + "|offsetY=" + offsetY);
            //设置方式一、设置方式二：scrollTo(offsetX, offsetY);
            setScrollX(offsetX);
            setScrollY(offsetY);
            scrollX = difX;
            scrollY = difY;
        } else if (mMoveViewStyle == MOVE_VIEW_STYLE_MARGIN) {
            // 计算偏移量
            changeX = difX - marginX;
            changeY = difY - marginY;
            Log.e(TAG, "onTouchEvent, ACTION_MOVE - MARGIN -> changeX=" + changeX + "|changeY=" + changeY);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) getLayoutParams();
            p.setMargins(getLeft() + changeX, getTop() + changeY, 0, 0);
            requestLayout();
        } else if (mMoveViewStyle == MOVE_VIEW_STYLE_ANIMATION) {
            // 计算偏移量
            changeX = difX - animationX;
            changeY = difY - animationY;
            Log.e(TAG, "onTouchEvent, ACTION_MOVE - ANIMATION -> changeX=" + changeX + "|changeY=" + changeY);
            float x = getTranslationX();
            float y = getTranslationY();
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "translationX", x, x + changeX);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "translationY", y, y + changeY);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(animatorX).with(animatorY);
            animatorSet.setDuration(0);
            animatorSet.start();
        }
    }

    private void onTouchEvent2ACTION_CANCEL() {
        switch (mMoveViewStyle) {
            case MOVE_VIEW_STYLE_TRANSLATION:
                currentX = getTranslationX();
                currentY = getTranslationY();
                break;
            case MOVE_VIEW_STYLE_LAYOUT:
            case MOVE_VIEW_STYLE_OFFSET:
            case MOVE_VIEW_STYLE_SCROLL_BY:
            case MOVE_VIEW_STYLE_SCROLL:
            case MOVE_VIEW_STYLE_MARGIN:
            case MOVE_VIEW_STYLE_ANIMATION:
                //TODO:不做处理
                break;
        }
    }
}
