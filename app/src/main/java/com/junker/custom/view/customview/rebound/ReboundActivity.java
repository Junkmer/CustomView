package com.junker.custom.view.customview.rebound;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.junker.custom.view.R;

public class ReboundActivity extends AppCompatActivity {

    private static final String TAG = ReboundActivity.class.getSimpleName();
    private ReboundLayout reboundLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rebound);

        initView();
    }

    private void initView() {
        reboundLayout = findViewById(R.id.rebound_layout);
        Log.e(TAG,"reboundLayout = "+reboundLayout);
        reboundLayout.setOnBounceDistanceChangeListener(new OnBounceDistanceChangeListener() {
            @Override
            public void onDistanceChange(int distance, int direction) {
                Log.e(TAG," onDistanceChange, distance="+distance+"|direction="+distance);
            }

            @Override
            public void onFingerUp(int distance, int direction) {
                Log.e(TAG," onFingerUp, distance="+distance+"|direction="+distance);
            }
        });
    }


}