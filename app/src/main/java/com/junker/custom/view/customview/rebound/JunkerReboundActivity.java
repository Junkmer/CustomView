package com.junker.custom.view.customview.rebound;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.junker.custom.view.R;

import java.util.Iterator;
import java.util.Set;

public class JunkerReboundActivity extends AppCompatActivity {

    private static final String TAG = JunkerReboundActivity.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_junker_rebound);

        findViewById(R.id.junker_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(JunkerReboundActivity.this,"aaaaa",Toast.LENGTH_SHORT).show();
            }
        });
    }
}