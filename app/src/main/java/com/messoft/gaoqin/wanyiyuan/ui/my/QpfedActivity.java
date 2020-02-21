package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.DsqAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.GetMemberWholesaleRushListByCache;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityQpfedBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.GoldModel;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

import java.util.List;

/**
 * 抢批发额度
 */
public class QpfedActivity extends BaseActivity<ActivityQpfedBinding> {

    private GoldModel mGoldModel;
    private DsqAdapter mAdapter;
    private LoginModel mLoginModel;
    private RxBus mRxBus;
//    private int mPage = HttpUtils.start_page_java;

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qpfed);
    }

    @Override
    protected void initSetting() {
        if (getActivity() != null && getActivity().getWindow() != null) {
            StatusBarUtil.setColor(getActivity(), Color.parseColor("#FF3E37"), 0);
        }
        initRxBus();
        mLoginModel = new LoginModel();
        showContentView();
        mGoldModel = new GoldModel();
        mAdapter = new DsqAdapter(getActivity());
        initOnRefresh(getActivity(), bindingView.rc);
        bindingView.rc.setAdapter(mAdapter);

        requestPersonInfo();
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
        bindingView.refreshLayout.setOnRefreshListener(refreshlayout -> refreshlayout.getLayout().postDelayed(() -> {
//            mPage = HttpUtils.start_page_java;
            loadData();
            refreshlayout.finishRefresh();
        }, 300));
        bindingView.refreshLayout.setEnableLoadmore(false);
//        bindingView.refreshLayout.setOnLoadmoreListener(refreshlayout -> refreshlayout.getLayout().postDelayed(() -> {
//            mPage++;
//            loadData();
//            refreshlayout.finishLoadmore();
//        }, 300));
        bindingView.tvMx.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 个人信息
     */
    public void requestPersonInfo() {
        mLoginModel.getLoginMemberInfo(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                LoginMemberInfo bean = (LoginMemberInfo) object;
                if (bean != null) {
//                    double wholesaleCapital = bean.getWholesaleCapital();
                    double wholesaleCapital = bean.getWholesaleCapital();
                    //总额度
//                    bindingView.tvPfedMoney.setText(String.valueOf(wholesaleCapital + disabledWholesaleCapital));
                    //可用额度
                    bindingView.tvMoney.setText(String.valueOf(wholesaleCapital));
                }

            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    private void loadData() {
        mGoldModel.getMemberWholesaleRushListByCache(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
//                showContentView();
                List<GetMemberWholesaleRushListByCache> data = (List<GetMemberWholesaleRushListByCache>) object;
//                if (mPage == HttpUtils.start_page_java) {
//                    if (data.size() > 0) {
//                        mAdapter.clear();
//                        mAdapter.addAll(data);
//                        mAdapter.notifyDataSetChanged();
//                    } else {
//                        mAdapter.clear();
//                        mAdapter.notifyDataSetChanged();
//                    }
//                } else {
//                    mAdapter.addAll(data);
//                    mAdapter.notifyDataSetChanged();
//                    bindingView.refreshLayout.finishLoadmore();
//                }
                mAdapter.clear();
                mAdapter.addAll(data);
                mAdapter.notifyDataSetChanged();
                bindingView.refreshLayout.finishLoadmore();
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
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_PFED) {
                if (rxBusMessage.getI() == 0) {
                    //刷新列表
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
