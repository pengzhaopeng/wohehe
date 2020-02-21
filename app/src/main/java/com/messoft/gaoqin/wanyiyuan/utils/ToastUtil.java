package com.messoft.gaoqin.wanyiyuan.utils;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.messoft.gaoqin.wanyiyuan.app.MyApplication;


/**
 * Created by peng on 2017/3/14.
 * 单例Toast
 */

public class ToastUtil {

    private static Toast mToast;

    public static void showShortToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getContext(), text, Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.show();
    }

    public static void showLongToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getContext(), text, Toast.LENGTH_LONG);
        }
        mToast.setText(text);
        mToast.show();
    }

    /**
     * 用于在线程中执行弹土司操作
     */
    public static void showToastSafely(final String msg) {
        UIUtils.getMainThreadHandler().post(new Runnable() {

            @SuppressLint("ShowToast")
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(UIUtils.getContext(), "", Toast.LENGTH_SHORT);
                }
                mToast.setText(msg);
                mToast.show();
            }
        });
    }
}
