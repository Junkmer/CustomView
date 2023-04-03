package com.junker.custom.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.junker.custom.view.customview.loginpage.LoginPageView;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        LoginPageView pageView = findViewById(R.id.login_page_view);
        pageView.setOnLoginPageActionListener(new LoginPageView.OnLoginPageActionListener() {
            @Override
            public void onGetVerifyCodeClick(String phoneNum) {
                Log.e(TAG, "phoneNum = > " + phoneNum);
            }

            @Override
            public void onOpenProtocolClick() {
                Log.e(TAG, "onOpenProtocolClick");
            }

            @Override
            public void onConfirmClick(String verifyCode, String phoneNum) {
                Log.e(TAG, "verifyCode = >"+verifyCode+"|phoneNum = > " + phoneNum);
            }
        });

    }
}