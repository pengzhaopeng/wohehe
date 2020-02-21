package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.ConfigValue;
import com.messoft.gaoqin.wanyiyuan.bean.ExistPayPassword;
import com.messoft.gaoqin.wanyiyuan.bean.MemberAvailableAmount;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.bean.TransferMemberInfo;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityZzBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.model.WalletModel;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.PayPassDialog.PayPassDialog;
import com.messoft.gaoqin.wanyiyuan.view.PayPassDialog.PayPassView;
import com.messoft.gaoqin.wanyiyuan.view.edittext.CashierInputFilter;


/**
 * 转赠金豆好友
 */
public class ZZActivity extends BaseActivity<ActivityZzBinding> {

    private TransferMemberInfo mPersonInfo;
    private WalletModel mWalletModel;
    private double mSfx;
    private LoginModel mLoginModel;

    private Double mAmount = 0.0; //后台计算的可提现额度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zz);
    }

    @Override
    protected void initSetting() {
        showContentView();
        setTitle("转赠");
        mLoginModel = new LoginModel();
        mWalletModel = new WalletModel();
        if (getIntent() != null) {
            mPersonInfo = (TransferMemberInfo) getIntent().getSerializableExtra("data");
            if (mPersonInfo != null) {
                ImgLoadUtil.displayEspImage(SysUtils.getImgURL(mPersonInfo.getImgName()), bindingView.iv, 0);
                bindingView.tvName.setText(mPersonInfo.getName());
                bindingView.tvPhone.setText(mPersonInfo.getAccount());
            }

        }
        //设置输入框只能输入整数
        InputFilter[] filters = {new CashierInputFilter()};
        bindingView.et.setFilters(filters);

        //手续费
        loadSxf();

        //余额
        loadYe();
    }

    @Override
    protected void initListener() {
        bindingView.tvNext.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                doNext();
            }
        });
    }

    private void loadYe() {
        mWalletModel.getMemberAvailableAmount(getActivity(), "1", new RequestImpl() {
            @SuppressLint("SetTextI18n")
            @Override
            public void loadSuccess(Object object) {
                MemberAvailableAmount bean = (MemberAvailableAmount) object;
                if (bean != null && StringUtils.isNoEmpty(bean.getAmount())) {
                    mAmount = Double.parseDouble(bean.getAmount());
                    bindingView.tvKtxje.setText("今日可转增金额" + mAmount);
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {

            }
        });
    }

    private void loadSxf() {
        mWalletModel.getConfigValue(getActivity(), "sys_config_wyy", "transfer_fee_rate", new RequestImpl() {
            @SuppressLint("SetTextI18n")
            @Override
            public void loadSuccess(Object object) {
                ConfigValue bean = (ConfigValue) object;
                if (bean != null && bean.getValue() != null) {
                    mSfx = Double.parseDouble(bean.getValue());
                    bindingView.tvSxf.setText("（每次扣除" + mSfx + "%手续费）");
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    private void doNext() {
        String money = bindingView.et.getText().toString().trim();
        if (!StringUtils.isNoEmpty(money)) {
            ToastUtil.showShortToast("输入金豆数量");
            return;
        }
        int num = Integer.parseInt(money);
        if (num < 100) {
            ToastUtil.showShortToast("金豆转赠最低限额100");
            return;
        }
        if (Double.parseDouble(money)>mAmount) {
            ToastUtil.showShortToast("转增金额不能超过最大转增额度");
            return;
        }
        //先验证下用户是否存在支付密码
        mLoginModel.existPayPassword(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                ExistPayPassword bean = (ExistPayPassword) object;
                String mIsExist = bean.getIsExist();
                if (mIsExist.equals("1")) {
                    //发起支付 弹出输入密码框
                    showPayPwdPop(money);
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
     * 密码输入框
     *
     * @param money
     */
    private void showPayPwdPop(String money) {
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
//                ToastUtil.showShortToast("输入完成回调");
                            mWalletModel.addMemberTransferForm(getActivity(), Double.parseDouble(money), passContent, mPersonInfo.getAccount(), new RequestImpl() {
                                @Override
                                public void loadSuccess(Object object) {
                                    ToastUtil.showLongToast("转账成功");
                                    double sfx = (Double.parseDouble(money) * mSfx) / 100;
                                    //提现成功页面
                                    DoSuccessActivity.goPage(getActivity(), 2, money, mPersonInfo.getAccount(), String.valueOf(sfx));
                                    //刷新钱包
                                    RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_WALLET, 0));
                                    finish();
                                }

                                @Override
                                public void loadFailed(int errorCode, String errorMessage) {
                                    ToastUtil.showShortToast(errorMessage);
                                }
                            });
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
//                ToastUtil.showShortToast("忘记密码回调");
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


    public static void goPage(Context context, TransferMemberInfo data) {
        Intent intent = new Intent(context, ZZActivity.class);
        intent.putExtra("data", data);
        context.startActivity(intent);
    }

}
