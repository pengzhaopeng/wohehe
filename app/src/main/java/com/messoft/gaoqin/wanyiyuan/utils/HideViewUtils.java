package com.messoft.gaoqin.wanyiyuan.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by Administrator on 2018/1/31 0031.
 */

public class HideViewUtils {

    private View hideView, down;//需要展开隐藏的布局，开关控件

    private RotateAnimation animation;//旋转动画

    public static HideViewUtils newInstance(Context context, View hideView, View down) {
        return new HideViewUtils(context, hideView, down);
    }

    public HideViewUtils(Context context, View hideView, View down) {
        this.hideView = hideView;
        this.down = down;
    }

    /**
     * 开关用自带android:animateLayoutChanges做
     */
    public void toggle() {
        startAnimation();
        if (View.VISIBLE == hideView.getVisibility()) {
            hideView.setVisibility(View.GONE);//布局隐藏
        } else {
            hideView.setVisibility(View.VISIBLE);//布局铺开
        }
    }

    /**
     * 开关旋转动画
     */
    private void startAnimation() {
        if (View.VISIBLE == hideView.getVisibility()) {
            animation = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            animation = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        animation.setDuration(150);//设置动画持续时间
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatMode(Animation.REVERSE);//设置反方向执行
        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        down.startAnimation(animation);
    }
}
