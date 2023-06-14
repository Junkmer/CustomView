package com.junker.custom.view.customview.slidemenu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.junker.custom.view.R;

public class SlideMenuActivity extends AppCompatActivity {

    private static final String TAG = SlideMenuActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_menu);

        SlideMenuView menuView = findViewById(R.id.slide_item);
        menuView.setOnEditClickListener(new SlideMenuView.OnEditClickListener() {
            @Override
            public void onReadClick() {
                Log.e(TAG,"onReadClick...");
            }

            @Override
            public void onTopClick() {
                Log.e(TAG,"onTopClick...");
            }

            @Override
            public void onDeleteClick() {
                Log.e(TAG,"onDeleteClick...");
            }
        });
    }
}