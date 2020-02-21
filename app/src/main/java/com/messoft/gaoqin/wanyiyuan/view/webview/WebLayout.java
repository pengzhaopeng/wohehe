package com.messoft.gaoqin.wanyiyuan.view.webview;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.just.agentweb.IWebLayout;
import com.messoft.gaoqin.wanyiyuan.R;

/**
 * Created by cenxiaozhong on 2017/7/1.
 * source code  https://github.com/Justson/AgentWeb
 */

public class WebLayout implements IWebLayout {

    private Activity mActivity;
    private final FrameLayout mTwinklingRefreshLayout;
    private WebView mWebView = null;

    public WebLayout(Activity activity) {
        this.mActivity = activity;
        mTwinklingRefreshLayout = (FrameLayout) LayoutInflater.from(activity).inflate(R.layout.fragment_twk_web, null);
//        mTwinklingRefreshLayout.setPureScrollModeOn();
//        mTwinklingRefreshLayout.setEnableLoadmore(false);
//        mTwinklingRefreshLayout.setEnableRefresh(false);
        mWebView = mTwinklingRefreshLayout.findViewById(R.id.webView);
    }

    @NonNull
    @Override
    public ViewGroup getLayout() {
        return mTwinklingRefreshLayout;
    }

    @Nullable
    @Override
    public WebView getWebView() {
        return mWebView;
    }

}
