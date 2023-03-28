package com.junker.custom.view.customview.loginpage;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.junker.custom.view.R;

public class LoginKeyboard extends LinearLayout {
    private final static String TAG = LoginKeyboard.class.getSimpleName();
    private OnKeyPressListener listener;

    public LoginKeyboard(Context context) {
        this(context, null);
    }

    public LoginKeyboard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoginKeyboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        setOnClickListener();
    }

    private void initView() {
        //TODO:以下三种方式相同
//        LayoutInflater.from(context).inflate(R.layout.num_key_pad,this);
//        LayoutInflater.from(context).inflate(R.layout.num_key_pad,this,true);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.num_key_pad, this, false);
        addView(view);
    }

    private void setOnClickListener() {
        //TODO:以上三种方式相同
        for (int i = 0; i < getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) getChildAt(i);
            for (int j = 0; j < linearLayout.getChildCount(); j++) {
                LinearLayout childLayout = (LinearLayout) linearLayout.getChildAt(j);
                for (int k = 0; k < childLayout.getChildCount(); k++) {
                    childLayout.getChildAt(k).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (v instanceof TextView) {
                                if (v.getId() == R.id.number_del){
                                    listener.onBackPress();
                                }else {
                                    CharSequence text = ((TextView) v).getText();
                                    Log.e(TAG, "click value is = > " + text);
                                    listener.onNumberPress(Integer.parseInt(text.toString()));
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    public void setOnKeyPressListener(OnKeyPressListener listener){
        this.listener = listener;
    }

    public interface OnKeyPressListener {
        void onNumberPress(int number);

        void onBackPress();
    }
}
