package com.junker.custom.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.view.menu.ActionMenuItemView;

import com.junker.custom.view.R;

public class InputNumberView extends RelativeLayout {
    private final static String TAG = InputNumberView.class.getSimpleName();

    private int mCurrentNumber = 0;
    private View mMinusBtn;
    private EditText mValueEdt;
    private View mPlusBtn;
    private OnNumberChangeListener mOnNumberChangeListener = null;
    private int mMax;
    private int mMin;
    private int mDefaultValue;
    private int mStep;
    private boolean mDisable;
    private int mBtnBgRes;

    public InputNumberView(Context context) {
        this(context, null);
    }

    public InputNumberView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
        //初始化属性
        initAttrs(context, attrs);
        //初始化view
        initView(context);
        //设置事件
        setUpEvent();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        //获取相关属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.InputNumberView);
        mMax = typedArray.getInt(R.styleable.InputNumberView_max,0);
        mMin = typedArray.getInt(R.styleable.InputNumberView_min,0);
        mStep = typedArray.getInt(R.styleable.InputNumberView_step,1);
        mDefaultValue = typedArray.getInt(R.styleable.InputNumberView_defaultValue,0);
        this.mCurrentNumber = mDefaultValue;
        mDisable = typedArray.getBoolean(R.styleable.InputNumberView_disable,false);
        mBtnBgRes = typedArray.getResourceId(R.styleable.InputNumberView_btnBackground,-1);
        Log.e(TAG,"mMax == >"+mMax);
        Log.e(TAG,"mMin == >"+mMin);
        Log.e(TAG,"mDefaultValue == >"+mDefaultValue);
        Log.e(TAG,"mStep == >"+mStep);
        Log.e(TAG,"mDisable == >"+mDisable);
        Log.e(TAG,"mBtnBgRes == >"+mBtnBgRes);
        //
        typedArray.recycle();
    }

    private void setUpEvent() {
        mMinusBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentNumber -= mStep;
                mPlusBtn.setEnabled(true);
                if (mMin != 0 && mCurrentNumber <= mMin){
                    mCurrentNumber = mMin;
                    v.setEnabled(false);
                    if (mOnNumberChangeListener != null){
                        mOnNumberChangeListener.onMinValue(mCurrentNumber);
                    }
                    Log.e(TAG,"current number is min value...");
                }
                updataText();
            }
        });

        mPlusBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentNumber += mStep;
                mMinusBtn.setEnabled(true);
                if (mMax != 0 && mCurrentNumber >= mMax){
                    mCurrentNumber = mMax;
                    v.setEnabled(false);
                    if (mOnNumberChangeListener != null){
                        mOnNumberChangeListener.onMaxValue(mCurrentNumber);
                    }
                    Log.e(TAG,"current number is max value...");
                }
                updataText();
            }
        });
    }

    private void initView(Context context) {
        //以下代码功能一样 (attachToRoot 参数表示：是否自动绑定布局，如果为 false 则需要再调用 addView()函数手动绑定)
        //View view = LayoutInflater.from(context).inflate(R.layout.input_number_view, this,true);
        //
        //View view = LayoutInflater.from(context).inflate(R.layout.input_number_view, this);
        //
        View view = LayoutInflater.from(context).inflate(R.layout.input_number_view, this, false);
        addView(view);
        //以上代码功能一样，都是把view添加到当前容器里
        mMinusBtn = this.findViewById(R.id.minus_btn);
        mValueEdt = this.findViewById(R.id.value_edt);
        mPlusBtn = this.findViewById(R.id.plus_btn);
        //初始化控件值
        updataText();
        mMinusBtn.setEnabled(!mDisable);
        mPlusBtn.setEnabled(!mDisable);
    }

    public int getNumber() {
        return mCurrentNumber;
    }

    public void setNumber(int value) {
        this.mCurrentNumber = value;
        this.updataText();
    }

    public View getmMinusBtn() {
        return mMinusBtn;
    }

    public void setmMinusBtn(View mMinusBtn) {
        this.mMinusBtn = mMinusBtn;
    }

    public int getmMax() {
        return mMax;
    }

    public void setmMax(int mMax) {
        this.mMax = mMax;
    }

    public int getmMin() {
        return mMin;
    }

    public void setmMin(int mMin) {
        this.mMin = mMin;
    }

    public int getmDefaultValue() {
        return mDefaultValue;
    }

    public void setmDefaultValue(int mDefaultValue) {
        this.mDefaultValue = mDefaultValue;
        this.mCurrentNumber = mDefaultValue;
        updataText();
    }

    public int getmStep() {
        return mStep;
    }

    public void setmStep(int mStep) {
        this.mStep = mStep;
    }

    public boolean ismDisable() {
        return mDisable;
    }

    public void setmDisable(boolean mDisable) {
        this.mDisable = mDisable;
    }

    public int getmBtnBgRes() {
        return mBtnBgRes;
    }

    public void setmBtnBgRes(int mBtnBgRes) {
        this.mBtnBgRes = mBtnBgRes;
    }

    /**
     * 更新数据
     */
    private void updataText() {
        mValueEdt.setText(String.valueOf(mCurrentNumber));
        if (mOnNumberChangeListener != null){
            mOnNumberChangeListener.onNumberChange(this.mCurrentNumber);
        }
    }

    public void setOnNumberChangeListener(OnNumberChangeListener listener){
        this.mOnNumberChangeListener = listener;
    }

    public interface OnNumberChangeListener {
        void onNumberChange(int value);
        void onMaxValue(int value);
        void onMinValue(int value);
    }
}
