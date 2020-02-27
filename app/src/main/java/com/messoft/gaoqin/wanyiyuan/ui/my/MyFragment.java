package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.ActivityManage;
import com.messoft.gaoqin.wanyiyuan.base.BaseFragment;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.bean.OrderStatusCount;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.FragmentMyBinding;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

import org.json.JSONObject;

import q.rorbin.badgeview.QBadgeView;

/**
 * 13316559585
 */
public class MyFragment extends BaseFragment<FragmentMyBinding> {

    // 初始化完成后加载数据
    private boolean mIsPrepared = false;
    // 第一次显示时加载数据，第二次不显示
    private boolean mIsFirst = true;

    private LoginModel mLoginModel;
    private RxBus mRxBus;
    private String mUnliquidatedCapital;//待入账额度
    private double mWholesaleCapital;//批发额度

    private QBadgeView mDfkBadge, mDfhBadge, mShBadge; //消息红点
    private double mDisabledWholesaleCapital;

    @Override
    protected int setContent() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initSetting() {
        showContentView();
        initRxBus();
        //初始化消息红点
        initBadge();
        mLoginModel = new LoginModel();
        mIsPrepared = true;
        loadData();
    }

    @Override
    protected void loadData() {
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }

        synchronized (this) {
            requestData();
        }
    }

    /**
     * 消息红点init
     */
    private void initBadge() {

        //待付款
        mDfkBadge = new QBadgeView(getActivity());
        mDfkBadge.bindTarget(bindingView.badgeDfk)
                .stroke(getResources().getColor(R.color.red), 1, true)
                .setBadgeGravity(Gravity.END | Gravity.TOP)
                .setGravityOffset(0, 0, true);
        mDfkBadge.setVisibility(View.GONE);

        //待发货
        mDfhBadge = new QBadgeView(getActivity());
        mDfhBadge.bindTarget(bindingView.badgeDfh)
                .stroke(getResources().getColor(R.color.red), 1, true)
                .setBadgeGravity(Gravity.END | Gravity.TOP)
                .setGravityOffset(0, 0, true);
        mDfhBadge.setVisibility(View.GONE);

        //待收货
        mShBadge = new QBadgeView(getActivity());
        mShBadge.bindTarget(bindingView.badgeDsh)
                .stroke(getResources().getColor(R.color.red), 1, true)
                .setBadgeGravity(Gravity.END | Gravity.TOP)
                .setGravityOffset(0, 0, true);
        mShBadge.setVisibility(View.GONE);
    }

    public void requestData() {
        synchronized (this) {
            if (bindingView != null && bindingView.refreshLayout != null) {
                bindingView.refreshLayout.postDelayed(this::requestData1, 500);
            }
        }
    }

    public void requestData1() {
        //个人信息
        requestPersonInfo();
        //订单红点数字
        requestOrderNumber();
    }

    /**
     * 订单数字
     */
    private void requestOrderNumber() {
        if (mDfkBadge == null || mDfhBadge==null || mShBadge==null) {
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("memberId", BusinessUtils.getBindAccountId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getOrderStatusCount(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(this.bindToLifecycle())
                .subscribe(new BaseObserver<OrderStatusCount>(getActivity(), false, "加载中...") {
                    @Override
                    protected void onSuccess(OrderStatusCount login) {
                        if (login == null) {
                            return;
                        }
                        //待付款
                        String waitPayOrderCount = login.getWaitPayCount();
                        if (StringUtils.isNoEmpty(waitPayOrderCount)) {
                            int dfk = Integer.parseInt(waitPayOrderCount);
                            if (dfk > 0) {
                                if (mDfkBadge.getVisibility() != View.VISIBLE) {
                                    mDfkBadge.setVisibility(View.VISIBLE);
                                }
                                mDfkBadge.setBadgeNumber(dfk);
                            } else {
                                if (mDfkBadge.getVisibility() != View.GONE) {
                                    mDfkBadge.setVisibility(View.GONE);
                                }
                            }
                        }
                        //待发货
                        String unProcessedOrderCount = login.getWaitDeliveryCount();
                        if (StringUtils.isNoEmpty(unProcessedOrderCount)) {
                            int dfh = Integer.parseInt(unProcessedOrderCount);

                            if (dfh > 0) {
                                if (mDfhBadge.getVisibility() != View.VISIBLE) {
                                    mDfhBadge.setVisibility(View.VISIBLE);
                                }
                                mDfhBadge.setBadgeNumber(dfh);
                            } else {
                                if (mDfhBadge.getVisibility() != View.GONE) {
                                    mDfhBadge.setVisibility(View.GONE);
                                }
                            }
                            //待收货
                            String waitqdOrderCount = login.getWaitReceiptCount();
                            if (StringUtils.isNoEmpty(waitPayOrderCount)) {
                                int dsh = Integer.parseInt(waitqdOrderCount);
                                if (dsh > 0) {
                                    if (mShBadge.getVisibility() != View.VISIBLE) {
                                        mShBadge.setVisibility(View.VISIBLE);
                                    }
                                    mShBadge.setBadgeNumber(dsh);
                                } else {
                                    if (mShBadge.getVisibility() != View.GONE) {
                                        mShBadge.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
//                        listener.loadFailed(errorCode, msg);
                    }
                });

    }

    /**
     * 个人信息
     */
    public void requestPersonInfo() {
        if (mLoginModel == null) return;
        mLoginModel.getLoginMemberInfo(getBaseActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                mIsFirst = false;
                LoginMemberInfo bean = (LoginMemberInfo) object;
                if (bean != null) {
                    BusinessUtils.saveUserData(bean);
                    ImgLoadUtil.displayCircle(bindingView.ivHeader, SysUtils.getImgURL(bean.getImgName()));
                    bindingView.tvName.setText(BusinessUtils.getMobile());
                    bindingView.tvXypf.setText("信用评分：" + bean.getCreditScore());

                    //是否付费 isVip
                    String isVip = bean.getIsVip();
                    if (isVip.equals("0")) {
                        bindingView.role0.setVisibility(View.VISIBLE);
                        bindingView.role1.setVisibility(View.GONE);
                    } else {
                        bindingView.role0.setVisibility(View.GONE);
                        bindingView.role1.setVisibility(View.VISIBLE);
                    }
                    //会员等级
                    String roleId = bean.getRoleId();
                    //代理专区显示
                    if (roleId.equals("2") || roleId.equals("3") || roleId.equals("4") || roleId.equals("5")) {
                        bindingView.cwDlzq.setVisibility(View.VISIBLE);
                        bindingView.view1.setVisibility(View.VISIBLE);
                    }

                    //余额
                    bindingView.tvYe.setText(bean.getCapital());
                    mWholesaleCapital = bean.getWholesaleCapital();
                    mDisabledWholesaleCapital = bean.getDisabledWholesaleCapital();
                    bindingView.tvPfed.setText(String.valueOf(mWholesaleCapital + mDisabledWholesaleCapital));
                    mUnliquidatedCapital = bean.getUnliquidatedCapital();
                    bindingView.tvDrz.setText(mUnliquidatedCapital);
                }

            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    @Override
    protected void initListener() {
        bindingView.refreshLayout.setLoadmoreFinished(false);
        bindingView.refreshLayout.setOnRefreshListener(refreshlayout -> refreshlayout.getLayout().postDelayed(() -> {
            requestData();
            refreshlayout.finishRefresh();
        }, 500));
        bindingView.ivSetting.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), SettingActivity.class);
            }
        });
        //升级会员
        bindingView.role0.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), SjhyActivity.class);
            }
        });
        //余额 钱包
        bindingView.llYe.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                WalletActivity.goPage(getActivity(), 0);
            }
        });
        //余额 钱包
        bindingView.cwWdqb.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                WalletActivity.goPage(getActivity(), 0);
            }
        });
        //预约抢购
        bindingView.cwYyqg.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), MyQgActivity.class);
            }
        });
        //批发额度
        bindingView.llPfed.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
//                PfedActivity.goPage(getActivity(), mWholesaleCapital,mDisabledWholesaleCapital);
                SysUtils.startActivity(getActivity(), PfedActivity.class);
            }
        });
        //待入账
        bindingView.llDyz.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                DaiRuZhangActivity1.goPage(getActivity(), mUnliquidatedCapital);
            }
        });
        //全部订单
        bindingView.allOrder.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), OrderBuyListActivity.class);
            }
        });
        bindingView.rlDfk.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                OrderBuyListActivity.goPage(getActivity(), 0);
            }
        });
        bindingView.rlDfh.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                OrderBuyListActivity.goPage(getActivity(), 1);
            }
        });
        bindingView.rlDsh.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                OrderBuyListActivity.goPage(getActivity(), 2);
            }
        });
        bindingView.rlYwc.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                OrderBuyListActivity.goPage(getActivity(), 3);
            }
        });
        //售后
        bindingView.rlSh.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), OrderShActivity.class);
            }
        });
        //个人账单
        bindingView.cwWdzd.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), MyBillActivity.class);
            }
        });
        //邀请好友
        bindingView.cwYyhy.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), YqhyActivity.class);
            }
        });
        //代理专区
        bindingView.cwDlzq.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), DaiLiZhuanQuActivity.class);
            }
        });
        //我的团队
        bindingView.cwWdtd.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                MenberInfoActivity.goPage(getActivity(), BusinessUtils.getMobile(), "我的", "0");
            }
        });
        //联系客服
        bindingView.cwLlkf.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(), null, "联系客服 13316559585", new DialogWithYesOrNoUtils.DialogCallBack() {
                    @Override
                    public void exectEvent(DialogInterface dialog) {
                        callPhone("13316559585");
                    }

                    @Override
                    public void exectCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    private void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }


    /**
     * 注册RxBus
     */
    private void initRxBus() {
        mRxBus = RxBus.getInstanceBus();
        mRxBus.registerRxBus(mRxBus, RxBusMessage.class, rxBusMessage -> {
            //根据事件类型进行处理
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_MY_INFO) {
                if (rxBusMessage.getI() == 0) {
                    //刷新个人信息 + 数字
                    requestData();
                }
            }
            //批发额度
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_PFED) {
                if (rxBusMessage.getI() == 0) {
                    requestPersonInfo();
                }
            }
            //微信升级会员成功
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.PAY_SUCESS_WX) {
                int i = rxBusMessage.getI();
                if (i == 2) {
                    ActivityManage.findActivity(RechagerActivity.class);
                    requestPersonInfo();
                }
            }
            //支付宝升级会员成功
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.PAY_SUCESS_ZFB) {
                int i = rxBusMessage.getI();
                if (i == 2) {
                    ActivityManage.findActivity(RechagerActivity.class);
                    requestPersonInfo();
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
}
