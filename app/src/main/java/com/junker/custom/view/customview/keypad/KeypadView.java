package com.junker.custom.view.customview.keypad;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junker.custom.view.R;
import com.junker.custom.view.utils.DensityUtil;

public class KeypadView extends ViewGroup {

    private int mTextColor;
    private float mTextSize;
    private int mItemPressBg;
    private int mItemNormalBg;

    public KeypadView(Context context) {
        this(context, null);
    }

    public KeypadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeypadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化属性
        initAttrs(context,attrs);
        //添加子view
        setupItem();
    }

    private void setupItem() {
        for (int i = 0; i < 11; i++) {
            TextView item = new TextView(getContext());
            //内容
            item.setText(String.valueOf(i));
            //大小
            item.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
            //居中
            item.setGravity(Gravity.CENTER);
            //字体颜色
            item.setTextColor(mTextColor);
            //设置背景
            item.setBackground(providerItemBg());
        }
    }

    private Drawable providerItemBg(){
        StateListDrawable listDrawable = new StateListDrawable();
        //按下bg
        GradientDrawable pressDrawable = new GradientDrawable();
        pressDrawable.setColor(getResources().getColor(R.color.key_item_press_color));
        pressDrawable.setCornerRadius(DensityUtil.dip2px(5));
        listDrawable.addState(new int[]{android.R.attr.state_pressed}, pressDrawable);
        //普通状态bg
        GradientDrawable normalDrawable = new GradientDrawable();
        normalDrawable.setColor(getResources().getColor(R.color.key_item_color));
        normalDrawable.setCornerRadius(DensityUtil.dip2px(5));
        listDrawable.addState(new int[]{},normalDrawable);

        return listDrawable;
    }

    private void initAttrs(Context context,AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.KeypadView);
        mTextColor = a.getColor(R.styleable.KeypadView_textColor, context.getResources().getColor(R.color.key_item_color));
        mTextSize = a.getDimensionPixelSize(R.styleable.KeypadView_textSize, 16);
        mItemPressBg = a.getColor(R.styleable.KeypadView_itemPressBg, context.getResources().getColor(R.color.key_item_color));
        mItemNormalBg = a.getColor(R.styleable.KeypadView_itemNormalBg, context.getResources().getColor(R.color.key_item_color));
        //回收资源
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //
    }
}
