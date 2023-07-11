package com.junker.custom.view.customview.piechart;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.util.List;

/**
 * @Author Junker
 * @ClassName CustomAnimation
 * @date 2023/7/7 11:29
 * @Version 1.0
 * @Description TODO
 */
public class CustomAnimation extends Animation {
    private static final String TAG = CustomAnimation.class.getSimpleName();
    private final View view;
    private final float total;

    public CustomAnimation(PieChartView view, float total) {
        this.view = view;
        this.total = total;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        if (interpolatedTime <= 1.0f) {
            if (view instanceof PieChartView) {
                ((PieChartView) view).mTotalPieChartAngle = total * interpolatedTime;
            }
            //重新绘制
            view.invalidate();
        }
    }
}