package com.junker.custom.view.customview.piechart;

import java.io.Serializable;

/**
 * @Author Junker
 * @ClassName SectorsData
 * @date 2023/6/30 11:29
 * @Version 1.0
 * @Description TODO；提供给外部传入的数据格式
 */
public class SectorsData implements Serializable,Comparable<SectorsData> {
    private String title;
    private float proportion;

    public SectorsData(String title, float proportion) {
        this.title = title;
        this.proportion = proportion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getProportion() {
        return proportion;
    }

    public void setProportion(float proportion) {
        this.proportion = proportion;
    }

    @Override
    public int compareTo(SectorsData other) {
        if (this.getProportion() > other.getProportion()){
            return -1;
        }else if (this.getProportion() == other.getProportion()){
            return 0;
        }else {
            return 1;
        }
    }
}
