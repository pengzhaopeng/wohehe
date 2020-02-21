package com.messoft.gaoqin.wanyiyuan.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.messoft.gaoqin.wanyiyuan.app.MyApplication;
import com.messoft.gaoqin.wanyiyuan.utils.DebugUtil;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;


/**
 * Created by Administrator on 2017/8/28 0028.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //如果没回调onResp，八成是这句没有写
        MyApplication.mWxapi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        MyApplication.mWxapi.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {
        DebugUtil.debug("WXEntryActivity" + resp.errStr);
        DebugUtil.debug("WXEntryActivity" + "错误码 : " + resp.errCode + "");
        switch (resp.errCode) {

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                ToastUtil.showShortToast("微信分享认证失败");
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                ToastUtil.showShortToast("取消分享");
                finish();
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                //发送失败
                ToastUtil.showShortToast("发送失败");
                finish();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                //不支持错误
                ToastUtil.showShortToast("不支持错误");
                finish();
                break;
            case BaseResp.ErrCode.ERR_COMM:
                //一般错误
                ToastUtil.showShortToast("出错了");
                finish();
                break;

            case BaseResp.ErrCode.ERR_OK: //ok
                DebugUtil.debug("WXEntryActivity" + "成功回掉："+resp.getType());
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:

                        //以下耗时操作最后不要在本Activity进行
                        //如果里面做耗时操作的话（比如根据code请求access_token等），
                        // 该activity在部分手机不会被finish,并且code has been used....根据调试最好不要在这个activity做耗时操作。

                        //拿到了微信返回的code,立马再去请求access_token
                        String code = ((SendAuth.Resp) resp).code;
//                        ToastUtil.showToast("WXEntryActivity" + "code = " + code);

                        //判断access_token是否存在和过期
//                        String accessToken = SPUtils.getString("wx_access_token", null);
//                        String openid = SPUtils.getString("wx_openid", null);
//                        if (StringUtils.isNoEmpty(accessToken) &&
//                                StringUtils.isNoEmpty(openid)) {
//                            // 有access_token，判断是否过期有效
//                            isExpireAccessToken(accessToken, openid);
//                        } else {
//                            // 没有access_token
//                            getAccessToken(code);
//                        }
                        break;

                    case RETURN_MSG_TYPE_SHARE:
                        finish();
                        DebugUtil.debug("WXEntryActivity" + "分享成功");
                        break;
                }
                break;
        }
    }



//    /**
//     * 判断accesstoken是否过期
//     *
//     * @param access_token
//     * @param openid
//     */
//    private void isExpireAccessToken(final String access_token, final String openid) {
//        String url = "https://api.weixin.qq.com/sns/auth?" +
//                "access_token=" + access_token +
//                "&openid=" + openid;
//        HttpClient.Builder.getServer().isInvalidWxToken(url)
//                .compose(RxSchedulers.<IsRefreshWxToken>compose())
////                .compose(this.<IsRefreshWxToken>bindToLifecycle())
//                .subscribe(new Observer<IsRefreshWxToken>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(IsRefreshWxToken wxTokenInfo) {
//                        if (wxTokenInfo != null && wxTokenInfo.getErrcode() == 0) {
//                            // accessToken没有过期，获取用户信息
//                            getUserInfo(access_token, openid);
//                        } else {
//                            // 过期了，使用refresh_token来刷新accesstoken
//                            refreshAccessToken();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtil.showShortToast("token失效");
//                        // 过期了，使用refresh_token来刷新accesstoken
//                        refreshAccessToken();
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }
//
//    /**
//     * 刷新获取新的access_token
//     *
//     * @param
//     */
//    private void refreshAccessToken() {
//        // 从本地获取以存储的refresh_token
//        String refresh_token = SPUtils.getString("wx_refresh_token", null);
//        if (!StringUtils.isNoEmpty(refresh_token)) {
//            return;
//        }
//        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?" +
//                "appid=" + Constants.WEIXIN_APP_ID +
//                "&grant_type=refresh_token" +
//                "&refresh_token=" + refresh_token;
//        HttpClient.Builder.getServer().refreshWxToken(url)
//                .compose(RxSchedulers.<RefreshWxToken>compose())
////                .compose(this.<RefreshWxToken>bindToLifecycle())
//                .subscribe(new Observer<RefreshWxToken>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(RefreshWxToken wxTokenInfo) {
//                        if (wxTokenInfo != null) {
//                            //刷新成功 --去获取用户信息
//                            //保存信息到本地
//                            SPUtils.putString("wx_access_token ", wxTokenInfo.getAccess_token());
//                            SPUtils.putString("wx_openid", wxTokenInfo.getOpenid());
//                            SPUtils.putString("wx_refresh_token", wxTokenInfo.getRefresh_token());
//                            //获取用户信息
//                            getUserInfo(wxTokenInfo.getAccess_token(), wxTokenInfo.getOpenid());
//                        } else {
////                            ToastUtil.showToast("登录失败");
//                            // 重新请求授权
//                            if (!MyApplication.mWxapi.isWXAppInstalled()) {
//                                ToastUtil.showShortToast("您还未安装微信客户端");
//                                return;
//                            }
//                            final SendAuth.Req req = new SendAuth.Req();
//                            req.scope = "snsapi_userinfo";
//                            req.state = "diandi_wx_login";
//                            MyApplication.mWxapi.sendReq(req);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        // 重新请求授权
//                        if (!MyApplication.mWxapi.isWXAppInstalled()) {
//                            ToastUtil.showShortToast("您还未安装微信客户端");
//                            return;
//                        }
//                        final SendAuth.Req req = new SendAuth.Req();
//                        req.scope = "snsapi_userinfo";
//                        req.state = "diandi_wx_login";
//                        MyApplication.mWxapi.sendReq(req);
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }
//
//    /**
//     * 获取授权口令
//     */
//    private void getAccessToken(String code) {
//        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
//                "appid=" + Constants.WEIXIN_APP_ID +
//                "&secret=" + Constants.WEIXIN_APP_SECRET +
//                "&code=" + code +
//                "&grant_type=authorization_code";
//
//        // 网络请求获取access_token
//        HttpClient.Builder.getServer().getWxToken(url)
//                .compose(RxSchedulers.<WxTokenInfo>compose())
////                .compose(this.<WxTokenInfo>bindToLifecycle())
//                .subscribe(new Observer<WxTokenInfo>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(WxTokenInfo wxTokenInfo) {
////                        ToastUtil.showShortToast("获取token成功");
//                        //保存信息到本地
//                        SPUtils.putString("wx_access_token", wxTokenInfo.getAccess_token());
//                        SPUtils.putString("wx_openid", wxTokenInfo.getOpenid());
//                        //保存refresh_token用来刷新token
//                        SPUtils.putString("wx_refresh_token", wxTokenInfo.getRefresh_token());
//                        //获取用户信息
//                        getUserInfo(wxTokenInfo.getAccess_token(), wxTokenInfo.getOpenid());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtil.showShortToast("获取token失败，请重新授权");
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }
//
//    /**
//     * 获取用户信息
//     *
//     * @param access_token
//     * @param openid
//     */
//    private void getUserInfo(String access_token, String openid) {
//        String url = "https://api.weixin.qq.com/sns/userinfo?" +
//                "access_token=" + access_token +
//                "&openid=" + openid;
//        HttpClient.Builder.getServer().getWxUserInfo(url)
//                .compose(RxSchedulers.<WxUserInfo>compose())
////                .compose(this.<WxUserInfo>bindToLifecycle())
//                .subscribe(new Observer<WxUserInfo>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(WxUserInfo wxTokenInfo) {
//                        DebugUtil.debug(TAG, "获取用户信息成功" + wxTokenInfo.toString());
////                        ToastUtil.showToast("获取用户信息失败成功" + wxTokenInfo);
//                        //微信登录
//                        wxLogin(wxTokenInfo);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        DebugUtil.debug(TAG, "获取用户信息失败");
//                        ToastUtil.showShortToast("获取用户信息失败 请重试");
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }
//
//    /**
//     * 微信登录
//     *
//     * @param wxTokenInfo
//     */
//    private void wxLogin(final WxUserInfo wxTokenInfo) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("wa_open_id", wxTokenInfo.getOpenid());
//            jsonObject.put("nickname", wxTokenInfo.getNickname());
//            jsonObject.put("gender", wxTokenInfo.getSex());
//            jsonObject.put("avatar", wxTokenInfo.getHeadimgurl());
//            jsonObject.put("wc_union_id", wxTokenInfo.getUnionid());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        HttpClient.Builder.getServer().weChatSignIn(jsonObject.toString())
//                .compose(RxSchedulers.<BaseBean<MobileSignUp>>compose())
////                .compose(this.<BaseBean<MobileSignUp>>bindToLifecycle())
//                .subscribe(new BaseObserver<MobileSignUp>(this, true, "加载中...") {
//                    @Override
//                    protected void onSuccess(MobileSignUp login) {
//                        if (login != null) {
//                            //通知登录页去操作
//                            RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.SEND_INFO_TO_LOGININ_PAGE, login));
//                        }
//                        finish();
//                    }
//
//                    @Override
//                    protected void onFailure(int errorCode, String msg) {
//                        if (errorCode == 100) {
//                            //未绑定
//                            ForgetPswActivity.goPage(WXEntryActivity.this, 0, wxTokenInfo.getOpenid(), wxTokenInfo.getUnionid());
//                        }
////                        ToastUtil.showShortToast(msg);
//                        finish();
//                    }
//                });
//    }

}
