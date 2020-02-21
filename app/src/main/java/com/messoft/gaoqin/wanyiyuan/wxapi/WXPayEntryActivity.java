package com.messoft.gaoqin.wanyiyuan.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.messoft.gaoqin.wanyiyuan.app.ActivityManage;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.app.MyApplication;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.ui.home.CommonOrderPayActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 支付完成回调页面
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pay_ways_select);
        MyApplication.mWxapi = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_APP_ID);
        MyApplication.mWxapi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        MyApplication.mWxapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        Log.d(TAG, "onPayFinish, errCode = " + resp.errStr);
        /*
          0 支付成功
         -1 发生错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
         -2 用户取消 发生场景：用户不支付了，点击取消，返回APP。
         */
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            // 根据返回码
            int code = resp.errCode;
            switch (code) {
                case 0:
                    // 去本地确认支付结果
//                    EventBus.getDefault().post("0");
                    goPayOrder();
                    finish();
                    break;
                case -1:
                    Toast.makeText(this, "支付异常", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case -2:
                    Toast.makeText(this, "支付已取消", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    }

    //1-订单支付完成（零售区）  2-订单支付完成（批发区） 3-充值完成
    private void goPayOrder() {
        ActivityManage.finishActivity(CommonOrderPayActivity.class);
        if (Constants.WX_PAY_ORDER_LSQ == Constants.WX_PAY_CODE) {
            RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.PAY_SUCESS_WX, 0));
        } else if (Constants.WX_PAY_ORDER_PFQ == Constants.WX_PAY_CODE) {
            RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.PAY_SUCESS_WX, 1));
        } else if (Constants.WX_PAY_RECHARGE == Constants.WX_PAY_CODE) {
            RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.PAY_SUCESS_WX, 2));
        }

    }


}