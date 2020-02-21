package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.alipay.MyALipayUtils;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.MemberUplevelAmount;
import com.messoft.gaoqin.wanyiyuan.bean.PayOrderByWx;
import com.messoft.gaoqin.wanyiyuan.bean.PayOrderByZfb;
import com.messoft.gaoqin.wanyiyuan.bean.PaymentMethodList;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivitySjhyBinding;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.model.PayModel;
import com.messoft.gaoqin.wanyiyuan.utils.NumberUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.wxapi.WXPay;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

import java.util.List;

/**
 * 升级会员
 */
public class SjhyActivity extends BaseActivity<ActivitySjhyBinding> {

    private Double mAmount;
    private PayModel mPayModel;
    //选中按钮
    boolean ckQb = false;
    boolean ckZfb = true;
    boolean ckWx = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjhy);
    }

    @Override
    protected void initSetting() {
        showContentView();
        setTitle("成为正式会员");
        mPayModel = new PayModel();
        loadData();
    }

    @Override
    protected void initListener() {
        bindingView.tvDo.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (mAmount <= 0) {
                    ToastUtil.showShortToast("金额错误");
                    return;
                }
                showOrderPayPop();
            }
        });
    }

    private void showOrderPayPop() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_pay_money_sure)
                .setConvertListener(new ViewConvertListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        RelativeLayout rlLq = holder.getView(R.id.rl_lq);
                        rlLq.setVisibility(View.GONE);
                        TextView tvMoney = holder.getView(R.id.tv_money);
                        TextView qianBaoMoney = holder.getView(R.id.tv_qianbao_money);
                        TextView qbye = holder.getView(R.id.tv_qbye);
                        TextView tvSure = holder.getView(R.id.tv_next);
                        tvMoney.setText("确认支付" + NumberUtils.formatNumber(mAmount) + "元");
                        qianBaoMoney.setText("钱包余额：" + mAmount);
                        qbye.setText("钱包余额：" + mAmount);
                        tvSure.setText("确认支付" + NumberUtils.formatNumber(mAmount) + "元");
                        CheckBox cbQb = holder.getView(R.id.cb_qb);
                        CheckBox cbZfb = holder.getView(R.id.cb_zfb);
                        CheckBox cbWx = holder.getView(R.id.cb_wx);
                        cbQb.setChecked(false);
                        cbZfb.setChecked(true);
                        cbWx.setChecked(false);
                        //钱包
                        cbQb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                ckQb = b;
                                if (b) {
                                    cbZfb.setChecked(false);
                                    cbWx.setChecked(false);
                                }
                            }
                        });
                        //支付宝
                        cbZfb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                ckZfb = b;
                                if (b) {
                                    cbQb.setChecked(false);
                                    cbWx.setChecked(false);
                                }
                            }
                        });
                        //微信
                        cbWx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                ckWx = b;
                                if (b) {
                                    cbQb.setChecked(false);
                                    cbZfb.setChecked(false);
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
//                .setOutCancel(false)
                .show(getActivity().getSupportFragmentManager());
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
        switch (payType) {
            case 2:
                mPayModel.updateMemberLevelByZfb(getActivity(),
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
//                                    ToastUtil.showShortToast("支付宝下单成功");
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
            case 3:
                mPayModel.updateMemberLevelByWx(getActivity(),
                        bean.getId(),
                        bean.getCode(),
                        bean.getName(),
                        bean.getPayTypeId(),
                        bean.getPayTypeName(), new RequestImpl() {
                            @Override
                            public void loadSuccess(Object object) {
                                PayOrderByWx wx = (PayOrderByWx) object;
                                if (wx != null) {
//                                    ToastUtil.showShortToast("微信下单成功");
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
    }

    private void loadData() {
        HttpClient.Builder.getWYYServer().getMemberUplevelAmount("")
                .compose(RxSchedulers.compose())
                .compose(this.bindToLifecycle())
                .subscribe(new BaseObserver<MemberUplevelAmount>(this, true, "支付中....") {
                    @Override
                    protected void onSuccess(MemberUplevelAmount login) {
                        if (!StringUtils.isNoEmpty(login.getAmount())) {
                            ToastUtil.showShortToast("金额错误");
                            return;
                        }
                        bindingView.tvMoney.setText(login.getAmount());
                        mAmount = Double.parseDouble(login.getAmount());

                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {

                    }
                });
    }
}
