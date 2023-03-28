package com.junker.custom.view.customview.loginpage;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.junker.custom.view.R;

import java.util.zip.Inflater;

public class LoginPageView extends FrameLayout {

    private int mColor;
    private int mVerifyCodeLength;
    private String mProtocolUrl;

    public LoginPageView(@NonNull Context context) {
        this(context,null);
    }

    public LoginPageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoginPageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //TODO:初始化自定义属性
        initAttrs(context, attrs);
        //TODO:初始化控件
        initView();
    }

    private void initView() {

    }

    private void initAttrs(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.LoginPageView);
        mColor = attributes.getColor(R.styleable.LoginPageView_mainColor,-1);
        mVerifyCodeLength = attributes.getInt(R.styleable.LoginPageView_verifyCodeLength,4);
        mProtocolUrl = attributes.getString(R.styleable.LoginPageView_protoclUrl);
        attributes.recycle();
    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    public int getmVerifyCodeLength() {
        return mVerifyCodeLength;
    }

    public void setmVerifyCodeLength(int mVerifyCodeLength) {
        this.mVerifyCodeLength = mVerifyCodeLength;
    }

    public String getmProtocolUrl() {
        return mProtocolUrl;
    }

    public void setmProtocolUrl(String mProtocolUrl) {
        this.mProtocolUrl = mProtocolUrl;
    }
}
