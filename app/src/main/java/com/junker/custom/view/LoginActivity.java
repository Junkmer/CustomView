package com.junker.custom.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView(){
//        LoginKeyboard keyboard = findViewById(R.id.login_key_board);
//        keyboard.setOnKeyPressListener(new LoginKeyboard.OnKeyPressListener() {
//            @Override
//            public void onNumberPress(int number) {
//                Log.e(TAG,"number is = > "+number);
//            }
//
//            @Override
//            public void onBackPress() {
//                Log.e(TAG,"back...");
//            }
//        });


    }
}