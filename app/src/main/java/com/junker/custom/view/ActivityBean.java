package com.junker.custom.view;

import java.io.Serializable;

public class ActivityBean implements Serializable {
    private int position;
    private String title;
    private Class<?> activity;

    public ActivityBean(int position, String title, Class<?> activity) {
        this.position = position;
        this.title = title;
        this.activity = activity;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class<?> getActivity() {
        return activity;
    }

    public void setActivity(Class<?> activity) {
        this.activity = activity;
    }
}
