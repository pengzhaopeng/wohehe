package com.messoft.gaoqin.wanyiyuan.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.GetNewsList;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonWebContentBinding;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.utils.HtmlFormat;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.utils.WebViewUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * @作者 Administrator
 * 业委会公告详情
 * @创建日期 2018/4/4 0004 18:31
 */

public class CommonWebContentActivity extends BaseActivity<CommonWebContentBinding> {

    private String mId;
    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_web_content);
    }

    @Override
    protected void initSetting() {
        setTitle("文章详情");
        showContentView();
        if (getIntent() != null) {
            mId = getIntent().getStringExtra("id");
            //0-创建车队数据共享协议
            mType = getIntent().getIntExtra("type", -1);

            switch (mType) {
                case 0:
                    setTitle("协议内容");
                    setRightText("同意协议");
                    break;
                case 1:
                    setTitle("协议内容");
                    break;
            }

            WebViewUtils.initWeb(getActivity(), bindingView.webView);
            bindingView.webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    bindingView.refreshLayout.finishRefresh();
                    view.loadUrl(String.format(Locale.CHINA, "javascript:document.body.style.paddingTop='%fpx'; void 0", DensityUtil.px2dp(bindingView.webView.getPaddingTop())));
                }
            });
            if (mId != null) {
                loadData();
//                bindingView.refreshLayout.autoRefresh();
            }
        }
    }

    @Override
    protected void initListener() {
        bindingView.refreshLayout.setEnableLoadmore(false);
        bindingView.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadData();
            }
        });

        getRightTv().setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (mType == 0) {
                    //表示用户同意
                    RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_AGREE, 0));
                    onBackPressed();
                }
            }
        });
    }

    private void loadData() {
        //0-注册用户协议
        if (mType == 0 || mType == 1) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("searchId", "s_privacy");
            } catch (Exception e) {
                e.printStackTrace();
            }
            HttpClient.Builder.getJavaCommonServer().getNewsList(jsonObject.toString())
                    .compose(RxSchedulers.compose())
                    .compose(this.bindToLifecycle())
                    .subscribe(new BaseObserver<List<GetNewsList>>(getActivity(), false, "加载中...") {
                        @Override
                        protected void onSuccess(List<GetNewsList> login) {
                            List<GetNewsList> data = login;
                            if (data != null && data.size() > 0) {
                                GetNewsList getNewsList = data.get(0);
                                if (StringUtils.isNoEmpty(getNewsList.getTitle())) {
                                    bindingView.tvTitle.setText(getNewsList.getTitle());
                                }
                                if (StringUtils.isNoEmpty(getNewsList.getContent())) {
//                            bindingView.webView.loadDataWithBaseURL(null, HtmlFormat.getNewContent(data.getContent()),"text/html","utf-8",null);
                                    bindingView.webView.loadData(HtmlFormat.getNewContent(getNewsList.getContent()), "text/html; charset=UTF-8", null); // 加载定义的代码，并设定编码格式和字符集。
                                }
                            }

                        }

                        @Override
                        protected void onFailure(int errorCode, String msg) {
                            ToastUtil.showShortToast(msg);
                        }
                    });
        }
    }

    private void noHeader() {
        bindingView.tvTitle.setVisibility(View.GONE);
        bindingView.tvTime.setVisibility(View.GONE);
    }

    public static void goPage(Context context, String id, int type) {
        Intent intent = new Intent(context, CommonWebContentActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }
}
