package com.messoft.gaoqin.wanyiyuan.view.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.messoft.gaoqin.wanyiyuan.view.viewbigimage.ViewBigImageActivity;

import java.util.ArrayList;

public class MJavascriptInterface {
    private Context context;

    public MJavascriptInterface(Context context) {
        this.context = context;
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img) {
        Bundle bundle = new Bundle();
        bundle.putInt("selet", 2);// 2,大图显示当前页数，1,头像，不显示页数
        bundle.putInt("code", 0);//第几张
        ArrayList arrayList = new ArrayList();
        arrayList.add(img);
        bundle.putStringArrayList("imageuri", arrayList);
        Intent intent = new Intent(context, ViewBigImageActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
