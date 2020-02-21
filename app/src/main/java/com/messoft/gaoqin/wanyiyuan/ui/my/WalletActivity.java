package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.ActivityManage;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.BeanOrderInfoList;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityWalletBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.GoldModel;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.ui.market.MyGuaShouListActivity;
import com.messoft.gaoqin.wanyiyuan.ui.market.WoYaoShouMaiActivity;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

import java.util.List;


/**
 * 钱包
 */
public class WalletActivity extends BaseActivity<ActivityWalletBinding> {

    private LoginModel mLoginModel;
    private GoldModel mGoldModel;
    private RxBus mRxBus;

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
    }

    @Override
    protected void initSetting() {
        if (getActivity() != null && getActivity().getWindow() != null) {
            StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.theme_color), 38);
        }
        showContentView();
        initRxBus();
        mGoldModel = new GoldModel();
        mLoginModel = new LoginModel();
        loadData();
    }

    @Override
    protected void initListener() {
        bindingView.ivBack.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
        //明细
        bindingView.tvMx.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), WalletDetailActivity.class);
            }
        });
        //充值
        bindingView.tvCz.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                RechagerActivity.goPage(getActivity(),0);
            }
        });
        //转账
        bindingView.tvZz.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                FindFriendActivity.goPage(getActivity(),0,null);
            }
        });
        //提现
        bindingView.tvTx.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), WithdrawActivity.class);
            }
        });
//        //挂售转让
//        bindingView.tvGs.setOnClickListener(new PerfectClickListener() {
//            @Override
//            protected void onNoDoubleClick(View v) {
//                checkWwcOrder();
//            }
//        });
        bindingView.rlJf.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                JfDetailActivity.goPage(getActivity(), 0);
            }
        });
        bindingView.rlDjq.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                JfDetailActivity.goPage(getActivity(), 1);
            }
        });
        bindingView.rlStf.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), StfActivity.class);
            }
        });
    }

    /**
     * 查询本人未完成的订单
     */
    private void checkWwcOrder() {
        mGoldModel.getMySaleBeanOrderList(getActivity(),
                BusinessUtils.getMobile(), null, "0,1,2", null, null,
                0, HttpUtils.per_page_20, new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        List<BeanOrderInfoList> data = (List<BeanOrderInfoList>) object;
                        if (data != null && data.size() > 0) {
                            DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(),
                                    "", "您当前有未完成的订单，不能新增挂售，是否前往操作？",
                                    "前往操作", "取消", new DialogWithYesOrNoUtils.DialogCallBack() {
                                        @Override
                                        public void exectEvent(DialogInterface dialog) {
                                            SysUtils.startActivity(getActivity(), MyGuaShouListActivity.class);
                                        }

                                        @Override
                                        public void exectCancel(DialogInterface dialog) {
                                            dialog.dismiss();
                                        }
                                    });
                        } else {
                            SysUtils.startActivity(getActivity(), WoYaoShouMaiActivity.class);
                        }
                    }

                    @Override
                    public void loadFailed(int errorCode, String errorMessage) {
                        ToastUtil.showShortToast(errorMessage);

                    }
                });
    }

    private void loadData() {
        mLoginModel.getLoginMemberInfo(getActivity(), new RequestImpl() {
            @SuppressLint("SetTextI18n")
            @Override
            public void loadSuccess(Object object) {
                LoginMemberInfo bean = (LoginMemberInfo) object;
                if (bean != null) {
                    bindingView.tvYe.setText(bean.getCapital());
                    bindingView.tvJf.setText(bean.getShoppingPoints() + "");
                    bindingView.tvDjq.setText(bean.getCashPoints() + "");
                    bindingView.tvStf.setText(bean.getEcologyPoints() + "");

                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
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
            //微信充值成功
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.PAY_SUCESS_WX) {
                int i = rxBusMessage.getI();
                if (i == 2) {
                    ActivityManage.finishActivity(RechagerActivity.class);
                    loadData();
                }
            }

            //支付宝充值成功
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.PAY_SUCESS_ZFB) {
                int i = rxBusMessage.getI();
                if (i == 2) {
                    ActivityManage.finishActivity(RechagerActivity.class);
                    loadData();
                }
            }

            //刷新钱包
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_WALLET) {
                if (rxBusMessage.getI() == 0) {
                    loadData();
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
    }

    public static void goPage(Context context, int type) {
        Intent intent = new Intent(context, WalletActivity.class);
        intent.putExtra("type", type); // 0-xxx  1-下单界面过来充值 2-下单结果页面过来充值
        context.startActivity(intent);
    }
}
