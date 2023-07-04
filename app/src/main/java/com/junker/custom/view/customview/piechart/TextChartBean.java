package com.junker.custom.view.customview.piechart;

import android.graphics.Paint;

/**
 * @Author Junker
 * @ClassName TextChartBean
 * @date 2023/7/3 15:26
 * @Version 1.0
 * @Description 图形title数据类
 */
public class TextChartBean {
    private float startX;
    private float startY;
    private String text;
    private Paint.Align paintAlign;

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Paint.Align getPaintAlign() {
        return paintAlign;
    }

    public void setPaintAlign(Paint.Align paintAlign) {
        this.paintAlign = paintAlign;
    }
}
