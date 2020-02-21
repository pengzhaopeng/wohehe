package com.messoft.gaoqin.wanyiyuan.view.webview;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.just.agentweb.AgentWeb;
import com.messoft.gaoqin.wanyiyuan.utils.ClipUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DebugUtil;
import com.messoft.gaoqin.wanyiyuan.utils.ShareUtils;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.utils.UIUtils;
import com.messoft.gaoqin.wanyiyuan.R;

import java.io.Serializable;

/**
 * Created by cenxiaozhong on 2017/5/14.
 * source code  https://github.com/Justson/AgentWeb
 */

public class AndroidInterface implements Serializable {

    private AgentWeb agent = null;
    private Activity context;

    private CYBNavigateResultListener mCYBNavigateResultListener = WebActivity.mCYBNavigateResultListener;

    public AndroidInterface(AgentWeb agent, Activity context) {
        this.agent = agent;
        this.context = context;
    }

    public AndroidInterface() {

    }


    /**
     * 微信支付结果回调
     *
     * @param msg
     */
    @JavascriptInterface
    public void paySuccessCallAndroid(final String msg) {
        UIUtils.getMainThreadHandler().post(() -> {
            DebugUtil.debug("Info", "main Thread:" + Thread.currentThread());
            if (StringUtils.isNoEmpty(msg)) {
//                if (msg.equals(Constants.POINT_ORDER_SUCCESS_PAY)) {
//                    //积分商城支付完成js触发完成按钮
//                    ToastUtil.showShortToast("支付完成，跳转中...");
//                }
            }
        });

        DebugUtil.debug("Info", "Thread:" + Thread.currentThread());

    }

    /**
     * 分享
     *
     * @param msg
     */
    @JavascriptInterface
    public void shareWXCallAndroid(final String titile, final String msg, String imgUrl, String linkUrl) {
        UIUtils.getMainThreadHandler().post(() -> {
            DebugUtil.debug("Info", "main Thread:" + Thread.currentThread());
            ShareUtils.shareDefault((AppCompatActivity) context,
                    titile,
                    msg,
                    linkUrl,
                    R.mipmap.logo,
                    url -> ClipUtils.copyText(UIUtils.getContext(),url,"地址已复制到粘贴板"));
        });

        DebugUtil.debug("Info", "Thread:" + Thread.currentThread());

    }


    /**
     * 车友邦导航回调
     *
     * @param startLat
     * @param startLng
     * @param endLat
     * @param endLng
     */
    @JavascriptInterface
    public void startNavigate(String startLat, String startLng, String endLat, String endLng) {
        //去做想做的事情。比如导航，直接带着开始和结束的经纬度Intent到导航activity就可以

        if (TextUtils.isEmpty(startLat) || TextUtils.isEmpty(startLng) || TextUtils.isEmpty(endLat)
                || TextUtils.isEmpty(endLng)) {//如果接收的数据不正确，给予提示
            ToastUtil.showShortToast("有不正确的数据");
            return;
        }

        if (mCYBNavigateResultListener != null) {
            mCYBNavigateResultListener.cybNavigateResult(startLat, startLng, endLat, endLng);
        }

    }

    //拿到设置webView的属性
    @JavascriptInterface
    public void setExtraInfoHead(String key, String value) {
        if (mCYBNavigateResultListener != null) {
            mCYBNavigateResultListener.cybArgs(key, value);
        }
    }

    /**
     * 车友帮导航
     */
    public interface CYBNavigateResultListener {
        void cybNavigateResult(String startLat, String startLng, String endLat, String endLng);

        void cybArgs(String key, String value);
    }
}
