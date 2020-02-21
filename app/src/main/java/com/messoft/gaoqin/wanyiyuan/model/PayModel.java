package com.messoft.gaoqin.wanyiyuan.model;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.PayOrderByWx;
import com.messoft.gaoqin.wanyiyuan.bean.PayOrderByZfb;
import com.messoft.gaoqin.wanyiyuan.bean.PaymentMethodList;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;

import org.json.JSONObject;

import java.util.List;

public class PayModel {
    /**
     * 支付方式列表查询
     */
    public void getPaymentMethodList(BaseActivity context,
                                     final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getPaymentMethodList(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<PaymentMethodList>>(context, true, "支付列表查询中...") {
                    @Override
                    protected void onSuccess(List<PaymentMethodList> login) {
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
     * 钱包订单支付
     */
    @SuppressWarnings("unchecked")
    public void payOrderByQb(BaseActivity context,
                                     String orderIds,
                                     String payPassword,
                                     String paymentMethodId,
                                     String paymentMethodCode,
                                     String paymentMethodName,
                                     String paymentTypeId,
                                     String paymentTypeName,
                                     final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderIds", orderIds);
            jsonObject.put("payPassword", payPassword);
            jsonObject.put("paymentMethodId", paymentMethodId);
            jsonObject.put("paymentMethodCode", paymentMethodCode);
            jsonObject.put("paymentMethodName", paymentMethodName);
            jsonObject.put("paymentTypeId", paymentTypeId);
            jsonObject.put("paymentTypeName", paymentTypeName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().payOrderByQb(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, true, "支付中....") {
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
     * 支付宝订单支付
     */
    @SuppressWarnings("unchecked")
    public void payOrderByZfb(BaseActivity context,
                              String orderIds,
                              String paymentMethodId,
                              String paymentMethodCode,
                              String paymentMethodName,
                              String paymentTypeId,
                              String paymentTypeName,
                             final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderIds", orderIds);
            jsonObject.put("paymentMethodId", paymentMethodId);
            jsonObject.put("paymentMethodCode", paymentMethodCode);
            jsonObject.put("paymentMethodName", paymentMethodName);
            jsonObject.put("paymentTypeId", paymentTypeId);
            jsonObject.put("paymentTypeName", paymentTypeName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().payOrderByZfb(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<PayOrderByZfb>(context, true, "支付中....") {
                    @Override
                    protected void onSuccess(PayOrderByZfb login) {
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
     * 支付宝会员升级支付
     */
    @SuppressWarnings("unchecked")
    public void updateMemberLevelByZfb(BaseActivity context,
                              String paymentMethodId,
                              String paymentMethodCode,
                              String paymentMethodName,
                              String paymentTypeId,
                              String paymentTypeName,
                              final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("paymentMethodId", paymentMethodId);
            jsonObject.put("paymentMethodCode", paymentMethodCode);
            jsonObject.put("paymentMethodName", paymentMethodName);
            jsonObject.put("paymentTypeId", paymentTypeId);
            jsonObject.put("paymentTypeName", paymentTypeName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().updateMemberLevelByZfb(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<PayOrderByZfb>(context, true, "支付中....") {
                    @Override
                    protected void onSuccess(PayOrderByZfb login) {
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
     * 支付宝置换手续费支付
     */
    @SuppressWarnings("unchecked")
    public void payOrderFeeByZfb(BaseActivity context,
                                       String orderId,
                                       String paymentMethodId,
                                       String paymentMethodCode,
                                       String paymentMethodName,
                                       String paymentTypeId,
                                       String paymentTypeName,
                                       final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderId", orderId);
            jsonObject.put("paymentMethodId", paymentMethodId);
            jsonObject.put("paymentMethodCode", paymentMethodCode);
            jsonObject.put("paymentMethodName", paymentMethodName);
            jsonObject.put("paymentTypeId", paymentTypeId);
            jsonObject.put("paymentTypeName", paymentTypeName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().payOrderFeeByZfb(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<PayOrderByZfb>(context, true, "支付中....") {
                    @Override
                    protected void onSuccess(PayOrderByZfb login) {
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
     * 支付宝订单充值
     */
    @SuppressWarnings("unchecked")
    public void payRechargeByZfb(BaseActivity context,
                              String amount,
                              String paymentMethodId,
                              String paymentMethodCode,
                              String paymentMethodName,
                              String paymentTypeId,
                              String paymentTypeName,
                              final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("amount", amount);
            jsonObject.put("paymentMethodId", paymentMethodId);
            jsonObject.put("paymentMethodCode", paymentMethodCode);
            jsonObject.put("paymentMethodName", paymentMethodName);
            jsonObject.put("paymentTypeId", paymentTypeId);
            jsonObject.put("paymentTypeName", paymentTypeName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().payRechargeByZfb(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<PayOrderByZfb>(context, true, "支付中....") {
                    @Override
                    protected void onSuccess(PayOrderByZfb login) {
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
     * 支付宝生态分充值
     */
    @SuppressWarnings("unchecked")
    public void membeRecologyPointsRechargeByZfb(BaseActivity context,
                                 String amount,
                                 String paymentMethodId,
                                 String paymentMethodCode,
                                 String paymentMethodName,
                                 String paymentTypeId,
                                 String paymentTypeName,
                                 final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("amount", amount);
            jsonObject.put("paymentMethodId", paymentMethodId);
            jsonObject.put("paymentMethodCode", paymentMethodCode);
            jsonObject.put("paymentMethodName", paymentMethodName);
            jsonObject.put("paymentTypeId", paymentTypeId);
            jsonObject.put("paymentTypeName", paymentTypeName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().membeRecologyPointsRechargeByZfb(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<PayOrderByZfb>(context, true, "支付中....") {
                    @Override
                    protected void onSuccess(PayOrderByZfb login) {
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
     * 微信订单支付
     */
    @SuppressWarnings("unchecked")
    public void payOrderByWx(BaseActivity context,
                             String orderIds,
                             String paymentMethodId,
                             String paymentMethodCode,
                             String paymentMethodName,
                             String paymentTypeId,
                             String paymentTypeName,
                              final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderIds", orderIds);
            jsonObject.put("paymentMethodId", paymentMethodId);
            jsonObject.put("paymentMethodCode", paymentMethodCode);
            jsonObject.put("paymentMethodName", paymentMethodName);
            jsonObject.put("paymentTypeId", paymentTypeId);
            jsonObject.put("paymentTypeName", paymentTypeName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().payOrderByWx(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<PayOrderByWx>(context, true, "支付中....") {
                    @Override
                    protected void onSuccess(PayOrderByWx login) {
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
     * 微信订单充值
     */
    @SuppressWarnings("unchecked")
    public void payRechargeByWx(BaseActivity context,
                             String amount,
                             String paymentMethodId,
                             String paymentMethodCode,
                             String paymentMethodName,
                             String paymentTypeId,
                             String paymentTypeName,
                             final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("amount", amount);
            jsonObject.put("paymentMethodId", paymentMethodId);
            jsonObject.put("paymentMethodCode", paymentMethodCode);
            jsonObject.put("paymentMethodName", paymentMethodName);
            jsonObject.put("paymentTypeId", paymentTypeId);
            jsonObject.put("paymentTypeName", paymentTypeName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().payRechargeByWx(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<PayOrderByWx>(context, true, "支付中....") {
                    @Override
                    protected void onSuccess(PayOrderByWx login) {
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
     * 微信生态分充值
     */
    @SuppressWarnings("unchecked")
    public void membeRecologyPointsRechargeByWx(BaseActivity context,
                                String amount,
                                String paymentMethodId,
                                String paymentMethodCode,
                                String paymentMethodName,
                                String paymentTypeId,
                                String paymentTypeName,
                                final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("amount", amount);
            jsonObject.put("paymentMethodId", paymentMethodId);
            jsonObject.put("paymentMethodCode", paymentMethodCode);
            jsonObject.put("paymentMethodName", paymentMethodName);
            jsonObject.put("paymentTypeId", paymentTypeId);
            jsonObject.put("paymentTypeName", paymentTypeName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().membeRecologyPointsRechargeByWx(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<PayOrderByWx>(context, true, "支付中....") {
                    @Override
                    protected void onSuccess(PayOrderByWx login) {
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
     * 微信2.1.20 会员升级支付
     */
    @SuppressWarnings("unchecked")
    public void updateMemberLevelByWx(BaseActivity context,
                                String paymentMethodId,
                                String paymentMethodCode,
                                String paymentMethodName,
                                String paymentTypeId,
                                String paymentTypeName,
                                final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("paymentMethodId", paymentMethodId);
            jsonObject.put("paymentMethodCode", paymentMethodCode);
            jsonObject.put("paymentMethodName", paymentMethodName);
            jsonObject.put("paymentTypeId", paymentTypeId);
            jsonObject.put("paymentTypeName", paymentTypeName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().updateMemberLevelByWx(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<PayOrderByWx>(context, true, "支付中....") {
                    @Override
                    protected void onSuccess(PayOrderByWx login) {
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
     * 微信置换手续费支付
     */
    @SuppressWarnings("unchecked")
    public void payOrderFeeByWx(BaseActivity context,
                             String orderId,
                             String paymentMethodId,
                             String paymentMethodCode,
                             String paymentMethodName,
                             String paymentTypeId,
                             String paymentTypeName,
                             final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderId", orderId);
            jsonObject.put("paymentMethodId", paymentMethodId);
            jsonObject.put("paymentMethodCode", paymentMethodCode);
            jsonObject.put("paymentMethodName", paymentMethodName);
            jsonObject.put("paymentTypeId", paymentTypeId);
            jsonObject.put("paymentTypeName", paymentTypeName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().payOrderFeeByWx(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<PayOrderByWx>(context, true, "支付中....") {
                    @Override
                    protected void onSuccess(PayOrderByWx login) {
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
}
