package com.junker.custom.view.utils;


import static com.junker.custom.view.MyApp.getAppContext;

import android.content.Context;

public class DensityUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        try {
            final float scale = getAppContext().getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        try {
            final float scale = getAppContext().getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }catch (Exception e){
            return 0;
        }

    }
}