package com.junker.custom.view.customview.piechart;

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
    private PieChartView view;
    private List<SectorsData> viewBeanList;
    private float total;//总金额，需要传递过来

    public CustomAnimation(PieChartView view, float total) {
        this.view = view;
        this.total = total;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        if (interpolatedTime <= 1.0f) {
            view.mTotalPieChartAngle = total * interpolatedTime;
            //重新绘制
            view.postInvalidate();
        }
    }
}