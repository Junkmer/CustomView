package com.junker.custom.view.customview.keypad;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junker.custom.view.R;
import com.junker.custom.view.utils.DensityUtil;

import java.time.temporal.ValueRange;

public class KeypadView extends ViewGroup {

    public static final int DEFAULT_ROW = 4;
    public static final int DEFAULT_COLUMN = 3;
    public static final int DEFAULT_TEXTVIEW_SIZE = -1;
    public static final int DEFAULT_MARGIN = DensityUtil.dip2px(6);

    private static final String TAG = KeypadView.class.getSimpleName();
    private int mTextColor;
    private float mTextSize;
    private int mItemPressBg;
    private int mItemNormalBg;
    private int row = DEFAULT_ROW;
    private int column = DEFAULT_COLUMN;
    private int mItemMargin;
    private OnNumberClickListener mOnNumberClickListener;

    public KeypadView(Context context) {
        this(context, null);
    }

    public KeypadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeypadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化属性
        initAttrs(context, attrs);
        //添加子view
        setupItem();
    }

    private void setupItem() {
        removeAllViews();

        for (int i = 0; i < 11; i++) {
            TextView item = new TextView(getContext());
            //内容
            if (i == 10) {
                item.setTag(true);
                item.setText("删除");
            } else {
                item.setTag(false);
                item.setText(String.valueOf(i));
            }
            //大小
            if (mTextSize != -1) {
                item.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            }
            //居中
            item.setGravity(Gravity.CENTER);
            //字体颜色
            item.setTextColor(mTextColor);
            //设置背景
            item.setBackground(providerItemBg());
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnNumberClickListener != null) {
                        if (!(boolean) v.getTag()) {
                            if (v instanceof TextView) {
                                String valueText = ((TextView) v).getText().toString();
                                mOnNumberClickListener.onNumberClick(Integer.parseInt(valueText));
                            }
                        } else {
                            mOnNumberClickListener.onDeleteClick();
                        }
                    }
                }
            });

            //添加子view
            addView(item);
        }
    }

    private Drawable providerItemBg() {
        StateListDrawable listDrawable = new StateListDrawable();
        //按下bg
        GradientDrawable pressDrawable = new GradientDrawable();
        pressDrawable.setColor(mItemPressBg);
        pressDrawable.setCornerRadius(DensityUtil.dip2px(5));
        listDrawable.addState(new int[]{android.R.attr.state_pressed}, pressDrawable);
        //普通状态bg
        GradientDrawable normalDrawable = new GradientDrawable();
        normalDrawable.setColor(mItemNormalBg);
        normalDrawable.setCornerRadius(DensityUtil.dip2px(5));
        listDrawable.addState(new int[]{}, normalDrawable);

        return listDrawable;
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.KeypadView);
        mTextColor = a.getColor(R.styleable.KeypadView_keyTextColor, context.getResources().getColor(R.color.white));
        mTextSize = a.getDimensionPixelSize(R.styleable.KeypadView_textSize, DEFAULT_TEXTVIEW_SIZE);
        mItemMargin = a.getDimensionPixelSize(R.styleable.KeypadView_itemMargin, DEFAULT_MARGIN);
        mItemPressBg = a.getColor(R.styleable.KeypadView_itemPressBg, context.getResources().getColor(R.color.key_item_press_color));
        mItemNormalBg = a.getColor(R.styleable.KeypadView_itemNormalBg, context.getResources().getColor(R.color.key_item_color));
        //回收资源
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int verticalPadding = getPaddingTop() + getPaddingBottom();
        int horizontalPadding = getPaddingLeft() + getPaddingRight();

        //测量孩子
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        Log.e(TAG, "widthSize = " + widthSize);
//        Log.e(TAG, "widthModeType = " + getModeType(widthMode));

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        Log.e(TAG, "heightSize = " + heightSize);
//        Log.e(TAG, "heightModeType = " + getModeType(heightMode));

        int perItemWidth = (widthSize - (column + 1) * mItemMargin - verticalPadding) / column;
        int perItemHeight = (heightSize - (row + 1) * mItemMargin - horizontalPadding) / row;

        int normalWidthSpec = MeasureSpec.makeMeasureSpec(perItemWidth, MeasureSpec.EXACTLY);
        int deleteWidthSpec = MeasureSpec.makeMeasureSpec(perItemWidth * 2 + mItemMargin, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(perItemHeight, MeasureSpec.EXACTLY);
        for (int i = 0; i < getChildCount(); i++) {
            View item = getChildAt(i);
            boolean isDelete = (boolean) item.getTag();
            item.measure(isDelete ? deleteWidthSpec : normalWidthSpec, heightSpec);
        }
        //测量自己
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int leftPadding = getPaddingLeft();
        int topPadding = getPaddingTop();
        int left = mItemMargin + leftPadding, top, right, bottom;

        for (int i = 0; i < childCount; i++) {
            //求出当前元素在第几行
            int rowIndex = i / column;
            int columnIndex = i % column;
            if (columnIndex == 0) {
                left = mItemMargin + leftPadding;
            }
            View item = getChildAt(i);
            top = rowIndex * item.getMeasuredHeight() + mItemMargin * (rowIndex + 1) + topPadding;
            right = left + item.getMeasuredWidth();
            bottom = top + item.getMeasuredHeight();
            item.layout(left, top, right, bottom);
            left += item.getMeasuredWidth() + mItemMargin;
        }
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public int getItemPressBg() {
        return mItemPressBg;
    }

    public void setItemPressBg(int mItemPressBg) {
        this.mItemPressBg = mItemPressBg;
    }

    public int getItemNormalBg() {
        return mItemNormalBg;
    }

    public void setItemNormalBg(int mItemNormalBg) {
        this.mItemNormalBg = mItemNormalBg;
    }

    public int getItemMargin() {
        return mItemMargin;
    }

    public void setItemMargin(int mItemMargin) {
        this.mItemMargin = mItemMargin;
    }

    private String getModeType(int mode) {
        String modeType = "";
        if (mode == MeasureSpec.AT_MOST) {
            modeType = "AT_MOST";
        } else if (mode == MeasureSpec.UNSPECIFIED) {
            modeType = "UNSPECIFIED";
        } else if (mode == MeasureSpec.EXACTLY) {
            modeType = "EXACTLY";
        }
        return modeType;
    }

    public void setOnNumberClickListener(OnNumberClickListener listener) {
        this.mOnNumberClickListener = listener;
    }

    public interface OnNumberClickListener {
        void onNumberClick(int value);

        void onDeleteClick();
    }
}
