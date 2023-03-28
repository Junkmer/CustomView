package com.junker.custom.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.junker.custom.view.customview.loginpage.LoginKeyboard;
import com.junker.custom.view.customview.numberinput.InputNumberView;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    private InputNumberView mInputNumberView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        mInputNumberView = this.findViewById(R.id.input_number_view);
        mInputNumberView.setOnNumberChangeListener(new InputNumberView.OnNumberChangeListener() {
            @Override
            public void onNumberChange(int value) {
                Log.e(TAG,"current value is == > "+value);
            }

            @Override
            public void onMaxValue(int value) {
                Log.e(TAG,"max value is == > "+value);
            }

            @Override
            public void onMinValue(int value) {
                Log.e(TAG,"min value is == > "+value);
            }
        });
    }

    public void loginBtn(View view){
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}