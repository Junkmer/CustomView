package com.junker.custom.view.customview.piechart;

/**
 * @Author Junker
 * @ClassName LineChartBean
 * @date 2023/7/3 15:26
 * @Version 1.0
 * @Description 图形线数据类
 */
public class LineChartBean {
    private float startX;
    private float startY;
    private float bendX;
    private float bendY;
    private float endX;
    private float endY;
    private float startAngle;

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

    public float getBendX() {
        return bendX;
    }

    public void setBendX(float bendX) {
        this.bendX = bendX;
    }

    public float getBendY() {
        return bendY;
    }

    public void setBendY(float bendY) {
        this.bendY = bendY;
    }

    public float getEndX() {
        return endX;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public float getEndY() {
        return endY;
    }

    public void setEndY(float endY) {
        this.endY = endY;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    @Override
    public String toString() {
        return "LineChartBean{" +
                "startX=" + startX +
                ", startY=" + startY +
                ", bendX=" + bendX +
                ", bendY=" + bendY +
                ", endX=" + endX +
                ", endY=" + endY +
                ", startAngle=" + startAngle +
                '}';
    }
}
