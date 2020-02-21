package com.messoft.gaoqin.wanyiyuan.wxapi;


import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.app.MyApplication;
import com.messoft.gaoqin.wanyiyuan.bean.PayOrderByWx;
import com.messoft.gaoqin.wanyiyuan.utils.DebugUtil;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;

/**
 * 微信支付主要逻辑类
 * Created by Administrator on 2017/2/10 0010.
 */
public class WXPay {
    private static WXPay instance;
    //微信支付相关参数
    private PayReq req;

    public static WXPay getInstance() {
        if (instance == null) {
            synchronized (WXPay.class) {
                if (instance == null) {
                    instance = new WXPay();
                }
            }
        }
        return instance;
    }

    public void Pay(PayOrderByWx bean) {
        if (bean == null) {
            ToastUtil.showShortToast("生成的订单有误，请重新发起支付");
            return;
        }
        req = new PayReq();
        //生成支付参数
        genPayReq(bean);
        //调起支付
        sendPayReq();
    }


    private void genPayReq(PayOrderByWx bean) {

        req.appId = bean.getAppid();
        req.partnerId = bean.getPartnerid();
        req.prepayId = bean.getPrepayid();
        req.packageValue = bean.getPackageX();
        req.nonceStr = bean.getNoncestr();
        req.timeStamp = bean.getTimestamp();
//        req.signType = "HMAC-SHA256";
        req.sign = bean.getSign();
//        mSb.append("sign\n" + req.sign + "\n\n");
    }

    /**
     * 最后提交订单(微信)
     */
    private void sendPayReq() {
        DebugUtil.debug("WXPayEntryActivity", "调起支付");
        MyApplication.mWxapi.registerApp(Constants.WEIXIN_APP_ID);
        MyApplication.mWxapi.sendReq(req);
    }

}
