package com.messoft.gaoqin.wanyiyuan.model;


import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.ExistPayPassword;
import com.messoft.gaoqin.wanyiyuan.bean.LoginBean;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.bean.SendSms;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;

import org.json.JSONObject;

public class LoginModel {

    /**
     * 注册
     */
    @SuppressWarnings("unchecked")
    public void register(BaseActivity context,
                         String mobile,
                         String password,
                         String code,
                         String recommenderMobile,
                         final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", mobile);
            jsonObject.put("password", password);
            jsonObject.put("code", code);
            if (recommenderMobile != null) {
                jsonObject.put("recommenderMobile", recommenderMobile);
            }
            jsonObject.put("projectType", 10);//固定值:10

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getMemberServer().register(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<LoginBean>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(LoginBean login) {
                        if (login != null) {
                            listener.loadSuccess(login);
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 登录
     */
    public void signIn(BaseActivity context, String account, String password, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("account", account);
            jsonObject.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getMemberServer().signIn(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<LoginBean>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(LoginBean login) {
                        if (login != null) {
                            listener.loadSuccess(login);
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 注销
     */
    @SuppressWarnings("unchecked")
    public void cancelAccount(BaseActivity context, final RequestImpl listener) {

        HttpClient.Builder.getWYYServer().cancelAccount("")
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, true, "注销中...") {
                    @Override
                    protected void onSuccess(Object login) {
                        listener.loadSuccess(login);
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 获取登录人信息
     */
    public void getLoginMemberInfo(BaseActivity context, final RequestImpl listener) {
        HttpClient.Builder.getWYYServer().getLoginMemberInfo("")
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<LoginMemberInfo>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(LoginMemberInfo login) {
                        if (login != null) {
                            listener.loadSuccess(login);
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 修改登录人信息
     */
    @SuppressWarnings("unchecked")
    public void updateLoginMemberInfo(BaseActivity context,
                                      String name,
                                      String imgName,
                                      String wechat,
                                      String wxpayCode,
                                      String alipayCode,
                                      final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (name != null) {
                jsonObject.put("name", name);
            }
            if (imgName != null) {
                jsonObject.put("imgName", imgName);
            }
            if (wechat != null) {
                jsonObject.put("wechat", wechat);
            }
            if (wxpayCode != null) {
                jsonObject.put("wxpayCode", wxpayCode);
            }
            if (alipayCode != null) {
                jsonObject.put("alipayCode", alipayCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().updateLoginMemberInfo(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, true, "信息保存中.") {
                    @Override
                    protected void onSuccess(Object data) {
                        listener.loadSuccess(data);
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 3.2.1 发送短信验证码
     */
    @SuppressWarnings("unchecked")
    public void sendSms(BaseActivity context, String mobile, int type, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", mobile);
            jsonObject.put("type", type);//验证码：0.微信注册绑定、1.修改密码 2.普通注册 3.修改手机号码 4.忘记密码
            jsonObject.put("signCode", "wyy");
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getMemberServer().sendSms(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<SendSms>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(SendSms login) {
                        if (login != null) {
                            listener.loadSuccess(login);
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 3.2.1 是否存在支付密码
     */
    @SuppressWarnings("unchecked")
    public void existPayPassword(BaseActivity context, final RequestImpl listener) {
        HttpClient.Builder.getWYYServer().existPayPassword("")
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<ExistPayPassword>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(ExistPayPassword login) {
                        if (login != null) {
                            listener.loadSuccess(login);
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 登录密码验证
     */
    @SuppressWarnings("unchecked")
    public void validatePassword(BaseActivity context, String password, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().validatePassword(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(Object login) {
                        listener.loadSuccess(login);
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 支付密码验证
     */
    @SuppressWarnings("unchecked")
    public void validatePayPassword(BaseActivity context, String payPassword, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("payPassword", payPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().validatePayPassword(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(Object login) {
                        listener.loadSuccess(login);
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 2.1.14 支付密码设置/修改
     */
    @SuppressWarnings("unchecked")
    public void modifyPayPassword(BaseActivity context, String oldPayPassword, String payPassword, String rePayPassword, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (oldPayPassword != null) {
                jsonObject.put("oldPayPassword", oldPayPassword); //原始密码（第一次设置不需要传递）
            }
            jsonObject.put("payPassword", payPassword);
            jsonObject.put("rePayPassword", rePayPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().modifyPayPassword(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(Object login) {
                        listener.loadSuccess(login);
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 2.1.15 支付密码重置
     */
    @SuppressWarnings("unchecked")
    public void resetPayPassword(BaseActivity context, String password, String payPassword, String rePayPassword, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", password); //登录密码
            jsonObject.put("payPassword", payPassword);
            jsonObject.put("rePayPassword", rePayPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().resetPayPassword(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(Object login) {
                        listener.loadSuccess(login);
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 账号密码修改
     */
    @SuppressWarnings("unchecked")
    public void updatePassword(BaseActivity context, String oldPassword, String password, String rePassword, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oldPassword", oldPassword);
            jsonObject.put("password", password);
            jsonObject.put("rePassword", rePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getMemberServer().updatePassword(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(Object login) {
                        listener.loadSuccess(login);
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 账号忘记密码重置密码
     */
    @SuppressWarnings("unchecked")
    public void resetPassword(BaseActivity context, String mobile, String code, String password, String rePassword, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", mobile);
            jsonObject.put("code", code);
            jsonObject.put("password", password);
            jsonObject.put("rePassword", rePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getMemberServer().resetPassword(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(Object login) {
                        listener.loadSuccess(login);
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }


}
