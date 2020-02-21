package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.BlankCard;
import com.messoft.gaoqin.wanyiyuan.bean.ConfigValue;
import com.messoft.gaoqin.wanyiyuan.bean.ExistPayPassword;
import com.messoft.gaoqin.wanyiyuan.bean.MemberAvailableAmount;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityWithdrawBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.BlankCardModel;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.model.WalletModel;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.RegexUtil;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.PayPassDialog.PayPassDialog;
import com.messoft.gaoqin.wanyiyuan.view.PayPassDialog.PayPassView;

import java.util.List;

/**
 * 提现
 */
public class WithdrawActivity extends BaseActivity<ActivityWithdrawBinding> {

    private static final int EDIT_OK = 0x001;
    private WalletModel mWalletModel;
    private BlankCardModel mBlankCardModel;
    private LoginModel mLoginModel;
    private BlankCard mDefaultBlankCard; //选中的银行卡  不选默认用默认银行卡
    private RxBus mRxBus;
    private double mSfx;
    private Double mAmount = 0.0; //后台计算的可提现额度

//    @SuppressLint("HandlerLeak")
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (EDIT_OK == msg.what) {
//
//            }
//        }
//    };
//
//    private Runnable mRunnable = new Runnable() {
//        @Override
//        public void run() {
//            mHandler.sendEmptyMessage(EDIT_OK);
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
    }

    @Override
    protected void initSetting() {
        showContentView();
        setTitle("提现");
        initRxBus();
        mWalletModel = new WalletModel();
        mBlankCardModel = new BlankCardModel();
        mLoginModel = new LoginModel();
        //余额
        loadYe();
        //手续费
        loadSxf();
        //银行卡
        loadYhk();
    }

    @Override
    protected void initListener() {

        bindingView.rlNoBlank.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                BlankCardActivity.goPage(getActivity(), 1);
            }
        });

        bindingView.rlHasBlank.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                BlankCardActivity.goPage(getActivity(), 1);
            }
        });
        bindingView.tvNext.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                doNext();
            }
        });

    }

    private void doNext() {
        if (mDefaultBlankCard == null) {
            ToastUtil.showShortToast("未选择银行卡");
            return;
        }
        String money = bindingView.et.getText().toString().trim();
        if (!StringUtils.isNoEmpty(money)) {
            ToastUtil.showShortToast("输入提现金额");
            return;
        }
        if (Double.parseDouble(money)>mAmount) {
            ToastUtil.showShortToast("提现金额不能超过最大提现额度");
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
     * 余额
     */
    private void loadYe() {
//        mLoginModel.getLoginMemberInfo(getActivity(), new RequestImpl() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void loadSuccess(Object object) {
//                LoginMemberInfo bean = (LoginMemberInfo) object;
//                bindingView.tvKtxje.setText("可提现余额" + bean.getCapital());
//            }
//
//            @Override
//            public void loadFailed(int errorCode, String errorMessage) {
//                ToastUtil.showShortToast(errorMessage);
//            }
//        });
        mWalletModel.getMemberAvailableAmount(getActivity(), "0", new RequestImpl() {
            @SuppressLint("SetTextI18n")
            @Override
            public void loadSuccess(Object object) {
                MemberAvailableAmount bean = (MemberAvailableAmount) object;
                if (bean != null && StringUtils.isNoEmpty(bean.getAmount())) {
                    mAmount = Double.parseDouble(bean.getAmount());
                    bindingView.tvKtxje.setText("今日可提现金额" + mAmount);
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {

            }
        });
    }

    /**
     * 手续费
     */
    private void loadSxf() {
        mWalletModel.getConfigValue(getActivity(), "sys_config_wyy", "withdraw_rate", new RequestImpl() {
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

    /**
     * 银行卡
     */
    private void loadYhk() {
        mBlankCardModel.getMemberBankList(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                List<BlankCard> cardList = (List<BlankCard>) object;
                if (cardList == null || cardList.size() < 1) {
                    //添加银行卡
                    bindingView.rlNoBlank.setVisibility(View.VISIBLE);
                } else {
                    bindingView.rlHasBlank.setVisibility(View.VISIBLE);
                    //取默认的银行卡
                    for (BlankCard blankCard : cardList) {
                        if (blankCard.getIsDefault().equals("1")) {
                            mDefaultBlankCard = blankCard;
                        }
                    }
                    if (mDefaultBlankCard == null) {
                        mDefaultBlankCard = cardList.get(0);
                    }
                    //设置
                    setBlankInfo();
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    public void setBlankInfo() {
        bindingView.tvBankName.setText(mDefaultBlankCard.getTypeName());
        bindingView.tvBankCode.setText(RegexUtil.hideBlankCardNo(mDefaultBlankCard.getCardNo()));
    }

    /**
     * 密码输入框
     *
     * @param money
     */
    private void showPayPwdPop(String money) {
        final PayPassDialog dialog = new PayPassDialog(this, R.style.dialog_pay_theme);
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
                mWalletModel.addMemberWithdrawForm(getActivity(), Double.parseDouble(money), passContent, mDefaultBlankCard.getId(), new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        ToastUtil.showLongToast("提现成功，预计2个工作日到账");
                        String blank = mDefaultBlankCard.getTypeName() + " 尾号" + mDefaultBlankCard.getCardNo().substring(mDefaultBlankCard.getCardNo().length() - 4);
                        double sfx = (Double.parseDouble(money) * mSfx) / 100;
                        //提现成功页面
                        DoSuccessActivity.goPage(getActivity(), 1, money, blank, String.valueOf(sfx));
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
    }

    /**
     * 注册RxBus
     */
    private void initRxBus() {
        mRxBus = RxBus.getInstanceBus();
        mRxBus.registerRxBus(mRxBus, RxBusMessage.class, rxBusMessage -> {
            //根据事件类型进行处理
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_WITHDRAW) {
                if (rxBusMessage.getData() != null) {
                    //更新位置

                    mDefaultBlankCard = (BlankCard) rxBusMessage.getData();
                    if (mDefaultBlankCard != null) {
                        bindingView.rlHasBlank.setVisibility(View.VISIBLE);
                        bindingView.rlNoBlank.setVisibility(View.GONE);
                        setBlankInfo();
                    }
                }
            }

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRxBus != null) {
            mRxBus.unSubscribe(this);
        }
//        if (mHandler != null) {
//            mHandler.removeCallbacksAndMessages(null);
//        }
    }
}
