package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.alipay.MyALipayUtils;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.PayOrderByWx;
import com.messoft.gaoqin.wanyiyuan.bean.PayOrderByZfb;
import com.messoft.gaoqin.wanyiyuan.bean.PaymentMethodList;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityRechagerBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.PayModel;
import com.messoft.gaoqin.wanyiyuan.utils.EditInputFilter;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.wxapi.WXPay;

import java.util.List;

/**
 * 充值
 */
public class RechagerActivity extends BaseActivity<ActivityRechagerBinding> {

    boolean ckZfb = false;
    boolean ckWx = false;
    private PayModel mPayModel;
    private int mType; //0-余额充值 1-生态分充值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechager);
    }

    @Override
    protected void initSetting() {
        setTitle("充值");
        showContentView();
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
            switch (mType) {
                case 0: //余额
                    bindingView.tvType.setText("充值余额");
                    break;
                case 1: //生态分
                    bindingView.et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    bindingView.tvType.setText("充值权益值");
                    break;
            }
        }
        mPayModel = new PayModel();

        InputFilter[] filters = {new EditInputFilter()};
        bindingView.et.setFilters(filters);
    }

    @Override
    protected void initListener() {
        bindingView.tvNext.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                recharge();
            }
        });
        //支付宝
        bindingView.cbZfb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ckZfb = b;
                if (b) {
                    bindingView.cbWx.setChecked(false);
                }
            }
        });
        //微信
        bindingView.cbWx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ckWx = b;
                if (b) {
                    bindingView.cbZfb.setChecked(false);
                }
            }
        });
    }

    private void recharge() {
        String et = bindingView.et.getText().toString().trim();
        if (!StringUtils.isNoEmpty(et)) {
            ToastUtil.showShortToast("请输入充值金额");
            return;
        }
        double money = Double.parseDouble(et);
        if (money <= 0) {
            ToastUtil.showShortToast("请输入正确的金额");
            return;
        }
        if (!ckZfb && !ckWx) {
            ToastUtil.showShortToast("请选择支付方式");
            return;
        }
        if (ckZfb && !ckWx) { //支付宝
            getPaymentMethodList(2, money);
        }
        if (!ckZfb && ckWx) { //微信
            getPaymentMethodList(3, money);
        }
    }

    /**
     * 先查询支付方式列表
     * 1-钱包 2-支付宝 3-微信
     */
    private void getPaymentMethodList(int payType, double money) {
        mPayModel.getPaymentMethodList(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                List<PaymentMethodList> payTypeList = (List<PaymentMethodList>) object;
                if (payTypeList != null && payTypeList.size() > 0) {
                    //发起订单支付
                    switch (payType) {
                        case 2:
                            PaymentMethodList bean1 = null;
                            for (PaymentMethodList paymentMethodList : payTypeList) {
                                if (paymentMethodList.getCode().equals(Constants.PAY_TYPE_ZFB)) {
                                    bean1 = paymentMethodList;
                                    break;
                                }
                            }
                            payOrder(payType, money, bean1);
                            break;
                        case 3:
                            PaymentMethodList bean2 = null;
                            for (PaymentMethodList paymentMethodList : payTypeList) {
                                if (paymentMethodList.getCode().equals(Constants.PAY_TYPE_WX)) {
                                    bean2 = paymentMethodList;
                                    break;
                                }
                            }
                            payOrder(payType, money, bean2);
                            break;
                    }
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    /**
     * 发起支付
     *
     * @param payType 1-钱包 2-支付宝 3-微信
     * @param money
     * @param bean
     */
    private void payOrder(int payType, double money, PaymentMethodList bean) {
        switch (payType) {
            case 2:
                switch (mType) {
                    case 0: //余额
                        mPayModel.payRechargeByZfb(getActivity(),
                                money + "",
                                bean.getId(),
                                bean.getCode(),
                                bean.getName(),
                                bean.getPayTypeId(),
                                bean.getPayTypeName(), new RequestImpl() {
                                    @Override
                                    public void loadSuccess(Object object) {
                                        PayOrderByZfb zfb = (PayOrderByZfb) object;
                                        if (zfb != null) {
                                            Constants.ZFB_PAY_CODE = Constants.ZFB_PAY_RECHARGE;
                                            ToastUtil.showShortToast("支付宝下单成功");
                                            MyALipayUtils.ALiPayBuilder builder = new MyALipayUtils.ALiPayBuilder();
                                            builder.build().toALiPay(getActivity(), zfb.getOrderInfo());
                                        }
                                    }

                                    @Override
                                    public void loadFailed(int errorCode, String errorMessage) {
                                        ToastUtil.showShortToast(errorMessage);
                                    }
                                });
                        break;
                    case 1: //生态分
                        mPayModel.membeRecologyPointsRechargeByZfb(getActivity(),
                                money + "",
                                bean.getId(),
                                bean.getCode(),
                                bean.getName(),
                                bean.getPayTypeId(),
                                bean.getPayTypeName(), new RequestImpl() {
                                    @Override
                                    public void loadSuccess(Object object) {
                                        PayOrderByZfb zfb = (PayOrderByZfb) object;
                                        if (zfb != null) {
                                            Constants.ZFB_PAY_CODE = Constants.ZFB_PAY_RECHARGE;
                                            ToastUtil.showShortToast("支付宝下单成功");
                                            MyALipayUtils.ALiPayBuilder builder = new MyALipayUtils.ALiPayBuilder();
                                            builder.build().toALiPay(getActivity(), zfb.getOrderInfo());
                                        }
                                    }

                                    @Override
                                    public void loadFailed(int errorCode, String errorMessage) {
                                        ToastUtil.showShortToast(errorMessage);
                                    }
                                });
                        break;
                }
                break;
            case 3:
                switch (mType) {
                    case 0:
                        mPayModel.payRechargeByWx(getActivity(),
                                money + "",
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
                                            Constants.WX_PAY_CODE = Constants.WX_PAY_RECHARGE;
                                            //跳转微信付钱
                                            WXPay.getInstance().Pay(wx);
                                        }
                                    }

                                    @Override
                                    public void loadFailed(int errorCode, String errorMessage) {
                                        ToastUtil.showShortToast(errorMessage);
                                    }
                                });
                        break;
                    case 1:
                        mPayModel.membeRecologyPointsRechargeByWx(getActivity(),
                                money + "",
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
                                            Constants.WX_PAY_CODE = Constants.WX_PAY_RECHARGE;
                                            //跳转微信付钱
                                            WXPay.getInstance().Pay(wx);
                                        }
                                    }

                                    @Override
                                    public void loadFailed(int errorCode, String errorMessage) {
                                        ToastUtil.showShortToast(errorMessage);
                                    }
                                });
                        break;
                }

                break;
        }
    }

    public static void goPage(Context context, int type) {
        Intent intent = new Intent(context, RechagerActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }
}
