package com.messoft.gaoqin.wanyiyuan.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.alipay.MyALipayUtils;
import com.messoft.gaoqin.wanyiyuan.app.ActivityManage;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.ExistPayPassword;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.bean.PayOrderByWx;
import com.messoft.gaoqin.wanyiyuan.bean.PayOrderByZfb;
import com.messoft.gaoqin.wanyiyuan.bean.PaymentMethodList;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.model.PayModel;
import com.messoft.gaoqin.wanyiyuan.ui.my.InputLoginPwdActivity;
import com.messoft.gaoqin.wanyiyuan.ui.my.SetPayPwdOneActivity;
import com.messoft.gaoqin.wanyiyuan.ui.my.WalletActivity;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.NumberUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.PayPassDialog.PayPassDialog;
import com.messoft.gaoqin.wanyiyuan.view.PayPassDialog.PayPassView;
import com.messoft.gaoqin.wanyiyuan.wxapi.WXPay;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

import java.util.List;

/**
 * 统一订单付款页面
 * 透明
 */
public class CommonOrderPayActivity extends BaseActivity {

    private LoginModel mLoginModel;
    private PayModel mPayModel;
    private String mOrderId;

    //选中按钮
    boolean ckQb = true;
    boolean ckZfb = false;
    boolean ckWx = false;

    private double mCapital = 0.00;//个人余额
    private String mClassCode; //用来标志 零售区商品还是批发区
    private double mTotalMoney;//总金额
    private RxBus mRxBus;
    private String mPayType;

    private int extra = -1;//拓展字段 0-置换抢购订单

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_order_pay);
        showContentView();

        setBackgroudTranslucent();
    }

    @Override
    protected void initSetting() {
        initRxBus();
        if (getIntent() != null) {
            mOrderId = getIntent().getStringExtra("orderId");
            mClassCode = getIntent().getStringExtra("classCode");
            mPayType = getIntent().getStringExtra("payType");
            extra = getIntent().getIntExtra("extra", -1);
            mTotalMoney = getIntent().getDoubleExtra("totalMoney", 0.00);
            if (mOrderId == null) {
                //直接结束
                finish();
            }
        }
        mPayModel = new PayModel();
        mLoginModel = new LoginModel();

        checkQianBao();
    }

    @Override
    protected void initListener() {

    }

    /**
     * 先查询钱包余额(通过查询登录人信息)
     */
    private void checkQianBao() {
        mLoginModel.getLoginMemberInfo(getActivity(), new RequestImpl() {

            @Override
            public void loadSuccess(Object object) {
                LoginMemberInfo bean = (LoginMemberInfo) object;
                if (bean != null && bean.getCapital() != null) {
                    mCapital = Double.parseDouble(bean.getCapital());
                    showOrderPayPop();
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
                finish();
            }
        });
    }

    private void showOrderPayPop() {
        ckQb = true;
        ckZfb = false;
        ckWx = false;
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_pay_money_sure)
                .setConvertListener(new ViewConvertListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        ImageView ivClose = holder.getView(R.id.iv_close);
                        ivClose.setVisibility(View.VISIBLE);
                        TextView tvMoney = holder.getView(R.id.tv_money);
                        TextView qianBaoMoney = holder.getView(R.id.tv_qianbao_money);
                        TextView qbye = holder.getView(R.id.tv_qbye);
                        TextView tvSure = holder.getView(R.id.tv_next);
                        tvMoney.setText("确认支付" + NumberUtils.formatNumber(mTotalMoney) + "元");
                        qianBaoMoney.setText("钱包余额：" + mCapital);
                        qbye.setText("钱包余额：" + mCapital);
                        tvSure.setText("确认支付" + NumberUtils.formatNumber(mTotalMoney) + "元");
                        CheckBox cbQb = holder.getView(R.id.cb_qb);
                        CheckBox cbZfb = holder.getView(R.id.cb_zfb);
                        CheckBox cbWx = holder.getView(R.id.cb_wx);
                        ivClose.setOnClickListener(new PerfectClickListener() {
                            @Override
                            protected void onNoDoubleClick(View v) {
                                dialog.dismiss();
                                getActivity().finish();
                            }
                        });

                        RelativeLayout rlQb = holder.getView(R.id.rl_lq);
                        RelativeLayout rlZfb = holder.getView(R.id.rl_zfb);
                        RelativeLayout rlWx = holder.getView(R.id.rl_wx);
                        switch (mPayType) {
                            case "1": //任意,1.钱包 2.现金
                                rlZfb.setVisibility(View.GONE);
                                rlWx.setVisibility(View.GONE);
                                break;
                            case "2":
                                rlQb.setVisibility(View.GONE);
                                ckQb = false;
                                cbZfb.setChecked(false);
                                ckZfb = true;
                                cbZfb.setChecked(true);
                                ckWx = false;
                                cbWx.setChecked(false);
                                break;

                        }

                        //钱包
                        cbQb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                ckQb = b;
                                cbZfb.setChecked(false);
                                cbWx.setChecked(false);
                                if (b) {
                                    cbQb.setChecked(true);
                                }
                            }
                        });
                        //支付宝
                        cbZfb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                ckZfb = b;
                                cbQb.setChecked(false);
                                cbWx.setChecked(false);
                                if (b) {
                                    cbZfb.setChecked(true);
                                }
                            }
                        });
                        //微信
                        cbWx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                ckWx = b;
                                cbQb.setChecked(false);
                                cbZfb.setChecked(false);
                                if (b) {
                                    cbWx.setChecked(true);
                                }
                            }
                        });
                        //发起支付
                        tvSure.setOnClickListener(new PerfectClickListener() {
                            @Override
                            protected void onNoDoubleClick(View v) {
                                if (!ckQb && !ckZfb && !ckWx) {
                                    ToastUtil.showShortToast("请选择支付方式");
                                    return;
                                }
                                //钱包支付
                                if (ckQb && !ckZfb && !ckWx) {
//                                    dialog.dismiss();
                                    if (mCapital >= mTotalMoney) {
                                        //先验证下用户是否存在支付密码
                                        mLoginModel.existPayPassword(getActivity(), new RequestImpl() {
                                            @Override
                                            public void loadSuccess(Object object) {
                                                ExistPayPassword bean = (ExistPayPassword) object;
                                                String mIsExist = bean.getIsExist();
                                                if (mIsExist.equals("1")) {
                                                    //发起支付 弹出输入密码框
                                                    showPayPwdPop();
                                                } else {
                                                    DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(), null, "您未设置支付密码，前往设置吗？", new DialogWithYesOrNoUtils.DialogCallBack() {
                                                        @Override
                                                        public void exectEvent(DialogInterface dialog) {
                                                            //设置新的
                                                            SetPayPwdOneActivity.goPage(getActivity(), 0, null, null);
                                                        }

                                                        @Override
                                                        public void exectCancel(DialogInterface dialog) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void loadFailed(int errorCode, String errorMessage) {
                                                ToastUtil.showShortToast(errorMessage);
                                            }
                                        });
                                    } else {
                                        //提示充值
                                        DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(),
                                                "温馨提示",
                                                "钱包余额不足，请前往充值",
                                                "前往充值",
                                                "取消",
                                                new DialogWithYesOrNoUtils.DialogCallBack() {
                                                    @Override
                                                    public void exectEvent(DialogInterface dialog1) {
                                                        WalletActivity.goPage(getActivity(), 1);
                                                        dialog1.dismiss();
                                                    }

                                                    @Override
                                                    public void exectCancel(DialogInterface dialog) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                    }
                                }
                                //支付宝支付
                                if (!ckQb && ckZfb && !ckWx) {
                                    dialog.dismiss();
                                    getPaymentMethodList(2, "");
                                }
                                //微信支付
                                if (!ckQb && !ckZfb && ckWx) {
                                    dialog.dismiss();
                                    getPaymentMethodList(3, "");
                                }
                            }
                        });
                    }
                })
                .setDimAmount(0.7f)
                .setShowBottom(true)
                .setOutCancel(false)
                .show(getActivity().getSupportFragmentManager());
    }


    /**
     * 密码输入框
     */
    private void showPayPwdPop() {
        //先验证下用户是否存在支付密码
        mLoginModel.existPayPassword(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                ExistPayPassword bean = (ExistPayPassword) object;
                String mIsExist = bean.getIsExist();
                if (mIsExist.equals("1")) {
                    final PayPassDialog dialog = new PayPassDialog(getActivity(), R.style.dialog_pay_theme);
                    //弹框自定义配置
                    dialog.setAlertDialog(false)
                            .setWindowSize(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f)
                            .setOutColse(false)
                            .setGravity(R.style.dialogOpenAnimation, Gravity.BOTTOM);
                    //组合控件自定义配置
                    PayPassView payView = dialog.getPayViewPass();
                    payView.setForgetText("忘记支付密码?");
                    payView.setForgetColor(getResources().getColor(R.color.colorAccent));
                    payView.setForgetSize(16);
                    payView.setPayClickListener(new PayPassView.OnPayClickListener() {
                        @Override
                        public void onPassFinish(String passContent) {
                            //6位输入完成回调
                            getPaymentMethodList(1, passContent);
                            dialog.dismiss();
                        }

                        @Override
                        public void onPayClose() {
                            dialog.dismiss();
                            //关闭回调

                        }

                        @Override
                        public void onPayForget() {
                            dialog.dismiss();
                            //忘记密码回调
                            SysUtils.startActivity(getActivity(), InputLoginPwdActivity.class);
                        }
                    });
                } else {
                    DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(), null, "您未设置支付密码，前往设置吗？", new DialogWithYesOrNoUtils.DialogCallBack() {
                        @Override
                        public void exectEvent(DialogInterface dialog) {
                            //设置新的
                            SetPayPwdOneActivity.goPage(getActivity(), 0, null, null);
                        }

                        @Override
                        public void exectCancel(DialogInterface dialog) {
                            dialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });


    }

    /**
     * 先查询支付方式列表
     * 1-钱包 2-支付宝 3-微信
     */
    private void getPaymentMethodList(int payType, String password) {
        mPayModel.getPaymentMethodList(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                List<PaymentMethodList> payTypeList = (List<PaymentMethodList>) object;
                if (payTypeList != null && payTypeList.size() > 0) {
                    //发起订单支付
                    switch (payType) {
                        case 1:
                            PaymentMethodList bean = null;
                            for (PaymentMethodList paymentMethodList : payTypeList) {
                                if (paymentMethodList.getCode().equals(Constants.PAY_TYPE_QB)) {
                                    bean = paymentMethodList;
                                    break;
                                }
                            }
                            payOrder(payType, bean, password);
                            break;
                        case 2:
                            PaymentMethodList bean1 = null;
                            for (PaymentMethodList paymentMethodList : payTypeList) {
                                if (paymentMethodList.getCode().equals(Constants.PAY_TYPE_ZFB)) {
                                    bean1 = paymentMethodList;
                                    break;
                                }
                            }
                            payOrder(payType, bean1, password);
                            break;
                        case 3:
                            PaymentMethodList bean2 = null;
                            for (PaymentMethodList paymentMethodList : payTypeList) {
                                if (paymentMethodList.getCode().equals(Constants.PAY_TYPE_WX)) {
                                    bean2 = paymentMethodList;
                                    break;
                                }
                            }
                            payOrder(payType, bean2, password);
                            break;
                    }
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
                finish();
            }
        });
    }

    /**
     * 发起支付
     *
     * @param payType  1-钱包 2-支付宝 3-微信
     * @param bean
     * @param password
     */
    private void payOrder(int payType, PaymentMethodList bean, String password) {
        if (mOrderId == null) {
            ToastUtil.showShortToast("订单id不能为空");
            return;
        }
        switch (payType) {
            case 1:
                mPayModel.payOrderByQb(getActivity(),
                        mOrderId,
                        password,
                        bean.getId(),
                        bean.getCode(),
                        bean.getName(),
                        bean.getPayTypeId(),
                        bean.getPayTypeName(), new RequestImpl() {
                            @Override
                            public void loadSuccess(Object object) {
                                ToastUtil.showShortToast("支付成功");
                                if (mClassCode.equals("pfq")) { //批发区
                                    paySucess(0);
                                } else if (mClassCode.contains("lsq")) {
                                    paySucess(1);
                                }

                            }

                            @Override
                            public void loadFailed(int errorCode, String errorMessage) {
                                ToastUtil.showShortToast(errorMessage);
                                finish();
                            }
                        });
                break;
            case 2:
                if (extra == 0) {
                    //置换手续费
                    mPayModel.payOrderFeeByZfb(getActivity(),
                            mOrderId,
                            bean.getId(),
                            bean.getCode(),
                            bean.getName(),
                            bean.getPayTypeId(),
                            bean.getPayTypeName(), new RequestImpl() {
                                @Override
                                public void loadSuccess(Object object) {
                                    PayOrderByZfb zfb = (PayOrderByZfb) object;
                                    if (zfb != null) {
                                        //共用充值通知
                                        Constants.ZFB_PAY_CODE = Constants.ZFB_PAY_RECHARGE;

                                        ToastUtil.showShortToast("支付宝下单成功");
                                        MyALipayUtils.ALiPayBuilder builder = new MyALipayUtils.ALiPayBuilder();
                                        builder.build().toALiPay(getActivity(), zfb.getOrderInfo());
                                    }
                                }

                                @Override
                                public void loadFailed(int errorCode, String errorMessage) {
                                    ToastUtil.showShortToast(errorMessage);
                                    finish();
                                }
                            });
                } else {
                    mPayModel.payOrderByZfb(getActivity(),
                            mOrderId,
                            bean.getId(),
                            bean.getCode(),
                            bean.getName(),
                            bean.getPayTypeId(),
                            bean.getPayTypeName(), new RequestImpl() {
                                @Override
                                public void loadSuccess(Object object) {
                                    PayOrderByZfb zfb = (PayOrderByZfb) object;
                                    if (zfb != null) {
                                        if (mClassCode.equals("pfq")) { //批发区
                                            Constants.ZFB_PAY_CODE = Constants.ZFB_PAY_ORDER_PFQ;
                                        } else if (mClassCode.contains("lsq")) {
                                            Constants.ZFB_PAY_CODE = Constants.ZFB_PAY_ORDER_LSQ;
                                        }
                                        ToastUtil.showShortToast("支付宝下单成功");
                                        MyALipayUtils.ALiPayBuilder builder = new MyALipayUtils.ALiPayBuilder();
                                        builder.build().toALiPay(getActivity(), zfb.getOrderInfo());
                                    }
                                }

                                @Override
                                public void loadFailed(int errorCode, String errorMessage) {
                                    ToastUtil.showShortToast(errorMessage);
                                    finish();
                                }
                            });
                }

                break;
            case 3:
                if (extra == 0) {
                    mPayModel.payOrderFeeByWx(getActivity(),
                            mOrderId,
                            bean.getId(),
                            bean.getCode(),
                            bean.getName(),
                            bean.getPayTypeId(),
                            bean.getPayTypeName(), new RequestImpl() {
                                @Override
                                public void loadSuccess(Object object) {
                                    PayOrderByWx wx = (PayOrderByWx) object;
                                    if (wx != null) {
                                        //共用充值通知
                                        ToastUtil.showShortToast("微信下单成功");
                                        Constants.WX_PAY_CODE = Constants.WX_PAY_RECHARGE;
                                        //跳转微信付钱
                                        WXPay.getInstance().Pay(wx);
                                    }
                                }

                                @Override
                                public void loadFailed(int errorCode, String errorMessage) {
                                    ToastUtil.showShortToast(errorMessage);
                                    finish();
                                }
                            });
                } else {
                    mPayModel.payOrderByWx(getActivity(),
                            mOrderId,
                            bean.getId(),
                            bean.getCode(),
                            bean.getName(),
                            bean.getPayTypeId(),
                            bean.getPayTypeName(), new RequestImpl() {
                                @Override
                                public void loadSuccess(Object object) {
                                    PayOrderByWx wx = (PayOrderByWx) object;
                                    if (wx != null) {
                                        ToastUtil.showShortToast("微信下单成功");
                                        if (mClassCode.equals("pfq")) { //批发区
                                            Constants.WX_PAY_CODE = Constants.WX_PAY_ORDER_PFQ;
                                        } else if (mClassCode.contains("lsq")) {
                                            Constants.WX_PAY_CODE = Constants.WX_PAY_ORDER_LSQ;
                                        }
                                        //跳转微信付钱
                                        WXPay.getInstance().Pay(wx);
                                    }
                                }

                                @Override
                                public void loadFailed(int errorCode, String errorMessage) {
                                    ToastUtil.showShortToast(errorMessage);
                                    finish();
                                }
                            });
                }

                break;
        }
    }

    /**
     * 支付成功后操作
     *
     * @param i
     */
    private void paySucess(int i) {
        switch (i) {
            case 0: //零售区
                OrderResultActivity.goPage(getActivity(), mClassCode);
                ActivityManage.findActivity(ProductInfoActivity.class);
                ActivityManage.findActivity(OrderActivity.class);
                finish();
                break;
            case 1://批发区
                OrderResultActivity.goPage(getActivity(), mClassCode);
                ActivityManage.findActivity(ProductInfoActivity.class);
                ActivityManage.findActivity(OrderActivity.class);
                finish();
                break;
            case 2://充值(会员升级，置换手续费等共用)
                finish();
                break;
        }
    }

    /**
     * 注册RxBus
     */
    private void initRxBus() {
        mRxBus = RxBus.getInstanceBus();
        mRxBus.registerRxBus(mRxBus, RxBusMessage.class, rxBusMessage -> {
            //根据事件类型进行处理
            //微信支付成功
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.PAY_SUCESS_WX) {
                int i = rxBusMessage.getI();
                paySucess(i);
            }

            //支付宝支付成功
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.PAY_SUCESS_ZFB) {
                int i = rxBusMessage.getI();
                paySucess(i);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRxBus != null) {
            mRxBus.unSubscribe(this);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void goPage(Context context, String orderId, String classCode, double totalMoney, String payType) {
        Intent intent = new Intent(context, CommonOrderPayActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("classCode", classCode);
        intent.putExtra("totalMoney", totalMoney);
        intent.putExtra("payType", payType);
        context.startActivity(intent);
    }

    public static void goPage(Context context, String orderId, String classCode, double totalMoney, String payType, int extra) {
        Intent intent = new Intent(context, CommonOrderPayActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("classCode", classCode);
        intent.putExtra("totalMoney", totalMoney);
        intent.putExtra("payType", payType);
        intent.putExtra("extra", extra);
        context.startActivity(intent);
    }

}
