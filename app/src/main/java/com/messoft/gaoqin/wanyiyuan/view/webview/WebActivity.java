package com.messoft.gaoqin.wanyiyuan.view.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.ActivityManage;
import com.messoft.gaoqin.wanyiyuan.base.BaseWebActivity;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;


/**
 * Created by Administrator on 2018/3/21 0021.
 */

public class WebActivity extends BaseWebActivity implements AndroidInterface.CYBNavigateResultListener {

    private String mUrl;

    private String mTitile;
    public static AndroidInterface.CYBNavigateResultListener mCYBNavigateResultListener = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getIntentData();
        super.onCreate(savedInstanceState);
        mCYBNavigateResultListener = this;

        if (mAgentWeb != null) {
            //注入对象
            mAgentWeb.getJsInterfaceHolder().addJavaObject("czb", new AndroidInterface(mAgentWeb, getActivity()));
        }
    }

    @Override
    public String getUrl() {
        if (mUrl != null) {
            return mUrl;
        }
        return "https://www.jd.com/";
    }

    @Override
    public String getTvTitle() {
        if (StringUtils.isNoEmpty(mTitile)) {
            return mTitile;
        }
        return getResources().getString(R.string.app_name);
    }

    private void getIntentData() {
        if (getIntent() != null) {
            mUrl = getIntent().getStringExtra("mUrl");
            mTitile = getIntent().getStringExtra("title");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //测试Cookies
        /*try {
            String targetUrl="";
            Log.i("Info","cookies:"+ AgentWebConfig.getCookiesByUrl(targetUrl="http://www.jd.com"));
            AgentWebConfig.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                    Log.i("Info","onResume():"+value);
                }
            });

            String tagInfo=AgentWebConfig.getCookiesByUrl(targetUrl);
            Log.i("Info","tag:"+tagInfo);
            AgentWebConfig.syncCookie("http://www.jd.com","ID=IDHl3NVU0N3ltZm9OWHhubHVQZW1BRThLdGhLaFc5TnVtQWd1S2g1REcwNVhTS3RXQVFBQEBFDA984906B62C444931EA0");
            String tag=AgentWebConfig.getCookiesByUrl(targetUrl);
            Log.i("Info","tag:"+tag);
            AgentWebConfig.removeSessionCookies();
            Log.i("Info","removeSessionCookies:"+AgentWebConfig.getCookiesByUrl(targetUrl));
        }catch (Exception e){
            e.printStackTrace();
        }*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManage.finishActivity(this);
    }


    public static void loadUrl(Context mContext, String mUrl) {
        if (mUrl.startsWith("http") || mUrl.startsWith("https")) {
            Intent intent = new Intent(mContext, WebActivity.class);
            intent.putExtra("mUrl", mUrl);
            mContext.startActivity(intent);
        }
    }

    public static void loadUrl(Context mContext, String title, String mUrl) {
        if (mUrl.startsWith("http") || mUrl.startsWith("https")) {
            Intent intent = new Intent(mContext, WebActivity.class);
            intent.putExtra("mUrl", mUrl);
            intent.putExtra("title", title);
            mContext.startActivity(intent);
        }
    }


    /**
     * 车友帮导航结果
     *
     * @param startLat
     * @param startLng
     * @param endLat
     * @param endLng
     */
    @Override
    public void cybNavigateResult(String startLat, String startLng, String endLat, String endLng) {
        shopPopSelectWays(startLat, startLng, endLat, endLng);
    }

    /**
     * 车友帮设置参数
     *
     * @param key
     * @param value
     */
    @Override
    public void cybArgs(String key, String value) {
//        ToastUtil.showShortToast("key:" + key + "  value:" + value);
        setExtraHeadersKeys(key, value);
    }

    private void shopPopSelectWays(String startLat, String startLng, String endLat, String endLng) {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_selecte_address_way)
                .setConvertListener((ViewConvertListener) (holder, dialog) -> {
                    //取消
                    holder.setOnClickListener(R.id.cancel, v -> dialog.dismiss());
                    //百度
                    holder.setOnClickListener(R.id.baidu, v -> {
                        dialog.dismiss();
                        openBaiduMap(startLat, startLng, endLat, endLng);
                    });
                    //高德
                    holder.setOnClickListener(R.id.gaode, v -> {
                                dialog.dismiss();
                                openGaoDeMap(startLat, startLng, endLat, endLng);
                            }
                    );
                })
//                .setDimAmount(0.3f)
                .setShowBottom(true)
                .setOutCancel(true)
                .show(getSupportFragmentManager());
    }


    /**
     * 打开百度地图
     *
     * @param startLat
     * @param startLng
     * @param endLat
     * @param endLng
     */
    private void openBaiduMap(String startLat, String startLng, String endLat, String endLng) {
        if (!SysUtils.isAvilible(getActivity(), "com.baidu.BaiduMap")) {
            ToastUtil.showShortToast("您手机未安装百度地图，请去应用市场下载");
            return;
        }
        try {
            // 驾车导航  location="+mEndLat+","+mEndLng
            Intent i1 = new Intent();
            i1.setAction(Intent.ACTION_VIEW);
            i1.addCategory(Intent.CATEGORY_DEFAULT);
            i1.setData(Uri.parse("baidumap://map/navi?location=" + endLat + "," + endLng +
                    "&src=com.messoft.gzmy.intelligentcommunit"));
            getActivity().startActivity(i1);
        } catch (Exception e) {
            ToastUtil.showShortToast("百度地图版本过低");
            e.printStackTrace();
        }
    }

    /**
     * 打开高德
     *
     * @param startLat
     * @param startLng
     * @param endLat
     * @param endLng
     */
    private void openGaoDeMap(String startLat, String startLng, String endLat, String endLng) {
        if (!SysUtils.isAvilible(getActivity(), "com.autonavi.minimap")) {
            ToastUtil.showShortToast("您手机未安装高德地图，请去应用市场下载");
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
//            androidamap://navi?sourceApplication=appname&poiname=fangheng&lat=36.547901&lon=104.258354&dev=1&style=2
            String url = "androidamap://navi?sourceApplication=com.messoft.gzmy.intelligentcommunit&lat=" + endLat + "&lon=" + endLng + "&dev=1&style=2";
            Uri uri = Uri.parse(url);
            //将功能Scheme以URI的方式传入data
            intent.setData(uri);

            getActivity().startActivity(intent);
        } catch (Exception e) {
            ToastUtil.showShortToast("高德地图版本过低");
            e.printStackTrace();
        }
    }


}
