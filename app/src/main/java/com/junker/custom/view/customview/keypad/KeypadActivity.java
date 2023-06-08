package com.junker.custom.view.customview.keypad;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.junker.custom.view.R;
import com.junker.custom.view.utils.DensityUtil;

public class KeypadActivity extends AppCompatActivity {

    private static final String TAG = KeypadActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keypad);
        initView();
    }

    private void initView(){
        KeypadView keypadView = findViewById(R.id.keypad_view);
        keypadView.setOnNumberClickListener(new KeypadView.OnNumberClickListener() {
            @Override
            public void onNumberClick(int value) {
                Log.e(TAG,"on number click ==> "+value);
            }

            @Override
            public void onDeleteClick() {
                Log.e(TAG,"on delete click");
            }
        });
    }
}