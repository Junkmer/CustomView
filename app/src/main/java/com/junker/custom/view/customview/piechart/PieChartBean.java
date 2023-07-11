package com.junker.custom.view.customview.piechart;

import android.graphics.RectF;

/**
 * @Author Junker
 * @ClassName PieChartBean
 * @date 2023/6/30 11:14
 * @Version 1.0
 * @Description TODO：扇形图数据类
 */
public class PieChartBean {
    private float left;
    private float top;
    private float right;
    private float bottom;
    private float centerX;
    private float centerY;
    private float startAngle;       //设置圆弧是从哪个角度来顺时针绘画的
    private float middleAngle;      //起始角度与圆弧扫过的角度中间Angle
    private float sweepAngle;       //设置圆弧扫过的角度
    private int color;              //设置画笔的颜色

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    public float getMiddleAngle() {
        return middleAngle;
    }

    public void setMiddleAngle(float middleAngle) {
        this.middleAngle = middleAngle;
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "PieChartBean{" +
                "left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                ", centerX=" + centerX +
                ", centerY=" + centerY +
                ", startAngle=" + startAngle +
                ", middleAngle=" + middleAngle +
                ", sweepAngle=" + sweepAngle +
                ", color=" + color +
                '}';
    }
}
