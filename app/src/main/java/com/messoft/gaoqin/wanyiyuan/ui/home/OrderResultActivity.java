package com.messoft.gaoqin.wanyiyuan.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.ActivityManage;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityOrderResultBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.ui.MainActivity;
import com.messoft.gaoqin.wanyiyuan.ui.my.OrderBuyListActivity;
import com.messoft.gaoqin.wanyiyuan.ui.my.OrderInfoActivity;
import com.messoft.gaoqin.wanyiyuan.utils.DebugUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

/**
 * 下单结果页面
 */
public class OrderResultActivity extends BaseActivity<ActivityOrderResultBinding> {

    private LoginModel mLoginModel;
    private String mClassCode; // 标志零售区还是批发区商品等 pfq xrlsq bpq

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_result);
    }

    @Override
    protected void initSetting() {
        setTitle("下单结果");
        showContentView();
        mLoginModel = new LoginModel();
        if (getIntent() != null) {
            mClassCode = getIntent().getStringExtra("classCode");
            DebugUtil.error("OrderResultActivity", "---classCode:" + mClassCode);
        }
        if (mClassCode.equals("pfq")) { //批发区
            bindingView.tvLsq.setVisibility(View.VISIBLE);
            bindingView.tvGoProduct.setText("进入互销零售区");
            bindingView.tvLsq.setVisibility(View.VISIBLE);
        } else if (mClassCode.contains("lsq")) { //零售区
            bindingView.llPfq.setVisibility(View.VISIBLE);
            bindingView.tvGoProduct.setText("进入批发区");
        } else {
            bindingView.tvGoProduct.setVisibility(View.GONE);
            bindingView.tvGoHome.setVisibility(View.VISIBLE);
        }

        loadData();
    }

    @Override
    protected void initListener() {
        bindingView.tvGoOrder.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), OrderBuyListActivity.class);
                finish();
            }
        });
        //进入批发区或者零售区
        bindingView.tvGoProduct.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (mClassCode.equals("pfq")){
                    //跳转首页随意一个零售区
                    RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_HOME_TAB, 1, mClassCode));
                }else if (mClassCode.contains("lsq")){
                    //跳转首页唯一个批发区
//                    RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_HOME_TAB, 0, mClassCode));
                    RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_HOME_TAB, 0, "pfq"));
                }
                goHome();
            }
        });
        //进入首页
        bindingView.tvGoHome.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), MainActivity.class);
                finish();
            }
        });
    }

    private void goHome() {
        ActivityManage.finishActivity(ProductInfoActivity.class);
        ActivityManage.finishActivity(OrderActivity.class);
        ActivityManage.finishActivity(CommonOrderPayActivity.class);
        ActivityManage.finishActivity(OrderBuyListActivity.class);
        ActivityManage.finishActivity(OrderInfoActivity.class);
        finish();
    }

    private void loadData() {
        if (!mClassCode.equals("pfq") && !mClassCode.contains("lsq")) {
            return;
        }
        mLoginModel.getLoginMemberInfo(getActivity(), new RequestImpl() {
            @SuppressLint("SetTextI18n")
            @Override
            public void loadSuccess(Object object) {
                LoginMemberInfo data = (LoginMemberInfo) object;
                if (data == null) return;
                if (mClassCode.contains("lsq")) { //零售区显示相关信息
                    //批发额度和余额比较
//                    String wholesaleCapital = data.getWholesaleCapital()+"";
                    String disabledWholesaleCapital = data.getDisabledWholesaleCapital()+"";
                    bindingView.tvPfed.setText(disabledWholesaleCapital + "元");
//                    String capital = data.getCapital();
//                    if (StringUtils.isNoEmpty(disabledWholesaleCapital) && StringUtils.isNoEmpty(capital)) {
//                        if (Double.parseDouble(disabledWholesaleCapital) > Double.parseDouble(capital)) {
//                            //余额不够
//                            bindingView.llPfqNo.setVisibility(View.VISIBLE);
//                            bindingView.tvGoPay.setOnClickListener(new PerfectClickListener() {
//                                @Override
//                                protected void onNoDoubleClick(View v) {
//                                    //去充值
//                                    WalletActivity.goPage(getActivity(), 2);
//                                }
//                            });
//                        } else {
//                            bindingView.llPfqHas.setVisibility(View.VISIBLE);
//                        }
//                    }
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    public static void goPage(Context context, String classCode) {
        Intent intent = new Intent(context, OrderResultActivity.class);
        intent.putExtra("classCode", classCode);
        context.startActivity(intent);
    }
}
