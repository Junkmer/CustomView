package com.junker.custom.view.customview.rebound;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.junker.custom.view.R;

/**
 * 实现类似微信回弹布局
 */
public class ReboundLayout extends ScrollView {
    private static final String TAG = ReboundLayout.class.getSimpleName();
    private int mDownX;
    private int mDownY;
    private int mOrientation;
    private View innerView;
    private float mResistance;
    private Integer mDuration;
    private boolean isNeedReset;
    private Interpolator mInterpolator;
    private final int mTouchSlop;
    private int mResetDistance;
    private boolean isIntercept;
    private OnBounceDistanceChangeListener onBounceDistanceChangeListener;

    public ReboundLayout(@NonNull Context context) {
        this(context, null);
    }

    public ReboundLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReboundLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttributes(context, attrs);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mInterpolator = new AccelerateDecelerateInterpolator();
        mResetDistance = Integer.MAX_VALUE;
    }

    /**
     * 初始化自定义属性
     */
    private void initAttributes(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ReboundLayout);
        mOrientation = array.getInt(R.styleable.ReboundLayout_reBoundOrientation, LinearLayout.HORIZONTAL);
        mResistance = array.getFloat(R.styleable.ReboundLayout_resistance, 3f);
        mDuration = array.getInteger(R.styleable.ReboundLayout_reBoundDuration, 300);
        if (mResistance < 1) {
            mResistance = 1f;
        }
        array.recycle();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent...");
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "onInterceptTouchEvent..., MotionEvent.ACTION_DOWN");
                //记录坐标
                if (innerView != null) {
                    innerView.clearAnimation();
                }
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onInterceptTouchEvent..., MotionEvent.ACTION_MOVE");
                int difX = (int) ((ev.getX() - mDownX) / mResistance);
                int difY = (int) ((ev.getY() - mDownY) / mResistance);
                if (mOrientation == LinearLayout.HORIZONTAL) { //水平滑动
                    if (Math.abs(difX) > mTouchSlop && Math.abs(difX) > Math.abs(difY)) {
                        ViewParent parent = getParent();
                        while (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                            parent = parent.getParent();
                            isIntercept = true;
                        }

                        if (!innerView.canScrollHorizontally(-1) && difX > 0) {
                            Log.e(TAG, "右拉到边界");
                            return true;
                        }
                        if (!innerView.canScrollHorizontally(1) && difX < 0) {
                            Log.e(TAG, "左拉到边界");
                            return true;
                        }
                    }
                } else {
                    if (Math.abs(difY) > mTouchSlop && Math.abs(difY) > Math.abs(difX)) {
                        ViewParent parent = getParent();
                        while (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                            parent = parent.getParent();
                            isIntercept = true;
                        }

                        if (!innerView.canScrollVertically(-1) && difY > 0) {
                            Log.e(TAG, "下拉到边界");
                            return true;
                        }
                        if (!innerView.canScrollVertically(1) && difY < 0) {
                            Log.e(TAG, "上拉到边界");
                            return true;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "onInterceptTouchEvent..., MotionEvent.ACTION_CANCEL");
                //重置变量
                if (isIntercept) {
                    ViewParent parent = getParent();
                    while (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(false);
                        parent = parent.getParent();
                    }
                }
                isIntercept = false;
                mDownX = 0;
                mDownY = 0;
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent...");
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onTouchEvent..., MotionEvent.ACTION_MOVE");
                if (mOrientation == LinearLayout.HORIZONTAL) {
                    int difX = (int) ((event.getX() - mDownX) / mResistance);
                    boolean isRebound = false;
                    if (!innerView.canScrollHorizontally(-1) && difX > 0) {
                        //右拉到边界
                        Log.e(TAG, "右拉到边界");
                        isRebound = true;
                    } else if (!innerView.canScrollHorizontally(1) && difX < 0) {
                        //左拉倒边界
                        Log.e(TAG, "左拉倒边界");
                        isRebound = true;
                    }
                    if (isRebound) {
                        innerView.setTranslationX(difX);
                        if (onBounceDistanceChangeListener != null) {
                            onBounceDistanceChangeListener.onDistanceChange(Math.abs(difX), difX > 0 ?
                                    OnBounceDistanceChangeListener.DIRECTION_RIGHT : OnBounceDistanceChangeListener.DIRECTION_LEFT);
                        }
                        return true;
                    }
                } else {
                    int difY = (int) ((event.getY() - mDownY) / mResistance);
                    boolean isRebound = false;
                    if (!innerView.canScrollVertically(-1) && difY > 0) {
                        //下拉到边界
                        Log.e(TAG, "下拉到边界");
                        isRebound = true;
                    } else if (!innerView.canScrollVertically(1) && difY < 0) {
                        //上拉到边界
                        Log.e(TAG, "上拉到边界");
                        isRebound = true;
                    }
                    if (isRebound) {
                        innerView.setTranslationY(difY);
                        if (onBounceDistanceChangeListener != null) {
                            onBounceDistanceChangeListener.onDistanceChange(Math.abs(difY), difY > 0 ?
                                    OnBounceDistanceChangeListener.DIRECTION_DOWN : OnBounceDistanceChangeListener.DIRECTION_UP);
                        }
                        return true;
                    }
                }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "onTouchEvent..., MotionEvent.ACTION_UP");
                if (mOrientation == LinearLayout.HORIZONTAL) {
                    int difX = (int) innerView.getTranslationX();
                    if (difX != 0) {
                        if (Math.abs(difX) <= mResetDistance || isNeedReset) {
                            innerView.animate().translationX(0).setDuration(mDuration).setInterpolator(mInterpolator);
                        }
                        if (onBounceDistanceChangeListener != null) {
                            onBounceDistanceChangeListener.onFingerUp(Math.abs(difX), difX > 0 ?
                                    OnBounceDistanceChangeListener.DIRECTION_RIGHT : OnBounceDistanceChangeListener.DIRECTION_LEFT);
                        }
                    }
                } else {
                    int difY = (int) innerView.getTranslationY();
                    if (difY != 0) {
                        if (Math.abs(difY) <= mResetDistance || isNeedReset) {
                            innerView.animate().translationY(0).setDuration(mDuration).setInterpolator(mInterpolator);
                        }
                        if (onBounceDistanceChangeListener != null) {
                            onBounceDistanceChangeListener.onFingerUp(Math.abs(difY), difY > 0 ?
                                    OnBounceDistanceChangeListener.DIRECTION_DOWN : OnBounceDistanceChangeListener.DIRECTION_UP);
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() >0){
            innerView = getChildAt(0);
        }else {
            throw new IllegalArgumentException("it must have innerView");
        }
    }

    public void setOrientation(int mOrientation) {
        this.mOrientation = mOrientation;
    }

    public void setInnerView(View innerView) {
        this.innerView = innerView;
    }

    public void setResistance(float mResistance) {
        this.mResistance = mResistance;
    }

    public void setDuration(Integer mDuration) {
        this.mDuration = mDuration;
    }

    public void setNeedReset(boolean needReset) {
        isNeedReset = needReset;
    }

    public void setInterpolator(Interpolator mInterpolator) {
        this.mInterpolator = mInterpolator;
    }

    public void setResetDistance(int mResetDistance) {
        this.mResetDistance = mResetDistance;
    }

    public void setOnBounceDistanceChangeListener(OnBounceDistanceChangeListener onBounceDistanceChangeListener) {
        this.onBounceDistanceChangeListener = onBounceDistanceChangeListener;
    }
}
