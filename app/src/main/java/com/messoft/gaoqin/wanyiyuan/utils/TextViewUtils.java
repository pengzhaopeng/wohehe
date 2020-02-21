package com.messoft.gaoqin.wanyiyuan.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

public class TextViewUtils {

    /**
     * 设置textView左右图标 type 0-左 1-右
     *
     * @param imgId
     * @param textView
     */
    public static void setTvImg(Context context, int imgId, TextView textView, int type) {
        Drawable img = UIUtils.getResource().getDrawable(imgId);
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        switch (type) {
            case 0://左
                textView.setCompoundDrawables(img, null, null, null); //设置右图标
                break;
            case 1://右
                textView.setCompoundDrawables(null, null, img, null); //设置右图标
                break;
        }
    }

    public static void setTvImg(String msg,int imgId, TextView textView, int type) {
        textView.setText(msg);
        Drawable img = UIUtils.getResource().getDrawable(imgId);
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        switch (type) {
            case 0://左
                textView.setCompoundDrawables(img, null, null, null); //设置右图标
                break;
            case 1://右
                textView.setCompoundDrawables(null, null, img, null); //设置右图标
                break;
        }
    }
}
