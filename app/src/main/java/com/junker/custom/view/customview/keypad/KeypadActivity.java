package com.junker.custom.view.customview.keypad;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.junker.custom.view.R;
import com.junker.custom.view.utils.DensityUtil;

public class KeypadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keypad);

        TextView textView = findViewById(R.id.textView);

        StateListDrawable listDrawable = new StateListDrawable();
        //按下bg
        GradientDrawable pressDrawable = new GradientDrawable();
        pressDrawable.setColor(getResources().getColor(R.color.key_item_press_color));
        pressDrawable.setCornerRadius(DensityUtil.dip2px(5));
        listDrawable.addState(new int[]{android.R.attr.state_pressed}, pressDrawable);
        //普通状态bg
        GradientDrawable normalDrawable = new GradientDrawable();
        normalDrawable.setColor(getResources().getColor(R.color.key_item_color));
        normalDrawable.setCornerRadius(DensityUtil.dip2px(5));
        listDrawable.addState(new int[]{},normalDrawable);
        //设置背景
        textView.setBackground(listDrawable);
    }
}