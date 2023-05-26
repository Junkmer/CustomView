package com.junker.custom.view.customview.flow;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.junker.custom.view.R;
import com.junker.custom.view.utils.DensityUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    private final static int DEFAULT_LINE = -1;
    private final static int DEFAULT_HORIZONTAL_MARGIN = DensityUtil.dip2px(5f);
    private final static int DEFAULT_VERTICAL_MARGIN = DensityUtil.dip2px(5f);
    private final static int DEFAULT_BORDER_RADIUS = DensityUtil.dip2px(5f);
    private final static int DEFAULT_TEXT_MAX_LENGTH = -1;
    private int mMaxLine;
    private int mHorizontalMargin;
    private int mVerticalMargin;
    private int mTextMaxLength;
    private int mTextColor;
    private int mBorderColor;
    private float mBorderRadius;

    private List<String> mDatas = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //TODO：获取属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        mMaxLine = a.getInt(R.styleable.FlowLayout_maxLines, DEFAULT_LINE);
        if (mMaxLine != DEFAULT_LINE && mMaxLine < 1){
            throw new IllegalArgumentException("mMaxLine can not less then 1.");
        }
        mHorizontalMargin = (int) a.getDimension(R.styleable.FlowLayout_itemHorizontalMargin, DEFAULT_HORIZONTAL_MARGIN);
        mVerticalMargin = (int) a.getDimension(R.styleable.FlowLayout_itemVerticalMargin, DEFAULT_VERTICAL_MARGIN);
        mTextMaxLength = a.getInt(R.styleable.FlowLayout_textMaxLength, DEFAULT_TEXT_MAX_LENGTH);
        if (mTextMaxLength != DEFAULT_TEXT_MAX_LENGTH && mTextMaxLength < 0){
            throw new IllegalArgumentException("text length must be max then 0");
        }
        mTextColor = a.getColor(R.styleable.FlowLayout_textColor, getResources().getColor(R.color.text_grey));
        mBorderColor = a.getColor(R.styleable.FlowLayout_borderColor, getResources().getColor(R.color.text_grey));
        mBorderRadius = a.getDimension(R.styleable.FlowLayout_borderRadius, DEFAULT_BORDER_RADIUS);

        a.recycle();//回收
    }

    public void setTextList(List<String> datas) {
        this.mDatas.clear();
        this.mDatas.addAll(datas);
        //根据数据创建子View，并且添加进来
        setUpChildren();
    }

    private void setUpChildren() {
        //先清空原来的内容
        removeAllViews();
        //添加子View进来
        for (String item : mDatas) {
//            TextView textView = new TextView(getContext());
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_flow_text, this, false);
            if (mTextMaxLength != DEFAULT_TEXT_MAX_LENGTH){
                textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mTextMaxLength)});//需要在 setText 方法前设置
            }
            textView.setText(item);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onClick(view, item);
                    }
                }
            });
            //设置TextView的相关属性：边距、颜色、border...
            addView(textView);
        }
    }

    private List<List<View>> mLines = new ArrayList<>();

    /**
     * 这两个值来自父控件，包含值和模式
     * int类型 ==> 4字节 > 4*8 bit ==> 32位
     * 0 ==> 00
     * 1 ==> 01
     * 2 ==> 10
     * 3 ==> 11
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeightSize = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }
        //先清空
        mLines.clear();
        //添加默认行
        List<View> line = new ArrayList<>();
        mLines.add(line);
        int childWidthSpace = MeasureSpec.makeMeasureSpec(parentWidthSize, MeasureSpec.AT_MOST);
        int childHeightSpace = MeasureSpec.makeMeasureSpec(parentHeightSize, MeasureSpec.AT_MOST);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != VISIBLE) {
                continue;
            }
            //测量 child view
            measureChild(child, childWidthSpace, childHeightSpace);
            if (line.size() == 0) {
                //可以添加
                line.add(child);
            } else {
                //判断是否可以添加到当前行
                boolean canBeAdd = checkChildCanBeAdd(line, child, parentWidthSize);
                if (!canBeAdd) {
                    if (mMaxLine != -1 && mLines.size() >= mMaxLine){
                        break;
                    }
                    line = new ArrayList<>();
                    mLines.add(line);
                }
                line.add(child);
            }
        }
        //根据尺寸计算所有行高
        View child = getChildAt(0);
        int childHeight = child.getMeasuredHeight();
        int parentHeightTargetSize = childHeight * mLines.size() + (mLines.size() + 1) * mVerticalMargin + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(parentWidthSize, parentHeightTargetSize);
    }

    private boolean checkChildCanBeAdd(List<View> line, View child, int parentWidthSize) {
        int measureWidth = child.getMeasuredWidth();
        int totalWidth = mHorizontalMargin + getPaddingLeft() + getPaddingRight();
        for (View view : line) {
            totalWidth += view.getMeasuredWidth() + mHorizontalMargin;
        }
        totalWidth += measureWidth + mHorizontalMargin;
        //如果超出限制宽度，则不可以再添加
        //否则可以添加
        return totalWidth <= parentWidthSize;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View firstChild = getChildAt(0);
        int currentLeft = mHorizontalMargin + getPaddingLeft();
        int currentTop = mVerticalMargin + getPaddingTop();
        int currentRight = mHorizontalMargin + getPaddingLeft();
        int currentBottom = firstChild.getMeasuredHeight() + mVerticalMargin + getPaddingTop();
        for (List<View> line : mLines) {
            for (View view : line) {
                //布局每一行
                int width = view.getMeasuredWidth();
                currentRight += width;
                if (currentRight > getMeasuredWidth() - mHorizontalMargin) {
                    currentRight = getMeasuredWidth() - mHorizontalMargin;
                }
                view.layout(currentLeft, currentTop, currentRight, currentBottom);
                currentLeft = currentRight + mHorizontalMargin;
                currentRight += mHorizontalMargin;
            }
            currentLeft = mHorizontalMargin + getPaddingLeft();
            currentTop += firstChild.getMeasuredHeight() + mVerticalMargin;
            currentRight = mHorizontalMargin + getPaddingLeft();
            currentBottom += firstChild.getMeasuredHeight() + mVerticalMargin;
        }
    }

    public int getMaxLine() {
        return mMaxLine;
    }

    public void setMaxLine(int mMaxLine) {
        this.mMaxLine = mMaxLine;
    }

    public float getHorizontalMargin() {
        return mHorizontalMargin;
    }

    public void setHorizontalMargin(int mHorizontalMargin) {
        this.mHorizontalMargin = DensityUtil.dip2px(mHorizontalMargin);
    }

    public float getVerticalMargin() {
        return mVerticalMargin;
    }

    public void setVerticalMargin(int mVerticalMargin) {
        this.mVerticalMargin = DensityUtil.dip2px(mVerticalMargin);
    }

    public int getTextMaxLength() {
        return mTextMaxLength;
    }

    public void setTextMaxLength(int mTextMaxLength) {
        this.mTextMaxLength = mTextMaxLength;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int mBorderColor) {
        this.mBorderColor = mBorderColor;
    }

    public float getBorderRadius() {
        return mBorderRadius;
    }

    public void setBorderRadius(float mBorderRadius) {
        this.mBorderRadius = mBorderRadius;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onClick(View v, String text);
    }
}
