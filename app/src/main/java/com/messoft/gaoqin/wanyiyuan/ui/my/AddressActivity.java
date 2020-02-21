package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.AddressListAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.AddressBean;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityAddressBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.AddressModel;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.RecycleViewDivider;

import java.util.List;

public class AddressActivity extends BaseActivity<ActivityAddressBinding> {

    private AddressModel mModel;
    //    private String mBindAccountId;
    private AddressListAdapter mAdapter;

    private RxBus mRxBus;
    private int mType; //0-普通 1-下单界面 2-我的抢购订单补录地址
    private int mPostition; //标志从列表那个位置点进来选中地址 比如我的抢购订单补录地址
    private List<AddressBean> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
    }

    @Override
    protected void initSetting() {
        setTitle("收货地址");
        showContentView();
        initRxBus();

        if (getIntent() != null) {
            //1-- 下单界面过来
            mType = getIntent().getIntExtra("type", -1);
            mPostition = getIntent().getIntExtra("position", -1);
        }

        mModel = new AddressModel();
        mAdapter = new AddressListAdapter(getActivity());

//        mBindAccountId = BusinessUtils.getBindAccountId();
        initOnRefresh(getActivity(), bindingView.rcAddress);
//        bindingView.rcAddress.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        bindingView.rcAddress.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL,
                DensityUtils.dp2px(getActivity(), 6),
                getResources().getColor(R.color.colorPageBg)));
        bindingView.rcAddress.setAdapter(mAdapter);
        loadData();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initListener() {
        bindingView.refreshLayout.setOnRefreshListener(refreshlayout -> refreshlayout.getLayout().postDelayed(() -> {
            loadData();
            refreshlayout.finishRefresh();
        }, 300));

        bindingView.tvAddAddress.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), AddAddressActivity.class);
            }
        });

        mAdapter.setOnItemClickListener((getAddress, position) -> {
            switch (mType) {
                case 0:
                    break;
                case 1:
                    //下单界面过来
                    if (getAddress != null) {
                        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_ORDER_ADDRESS, getAddress));
                        finish();
                    }
                case 2:
                    //我的抢购订单补录地址
                    if (getAddress != null) {
                        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_ORDER_ADDRESS, getAddress,mPostition));
                        finish();
                    }
                    break;
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        mModel.getAddress(AddressActivity.this, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                showContentView();
                mData = (List<AddressBean>) object;
                if (mData.size() > 0) {
                    mAdapter.clear();
                    mAdapter.addAll(mData);
                    mAdapter.notifyDataSetChanged();
                    bindingView.refreshLayout.finishRefresh();
                } else {
                    mAdapter.clear();
                    mAdapter.notifyDataSetChanged();
                    showNoData("暂无收货地址");
                }
//                mAdapter.setSelectedItem(0);
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
//                showError();
            }
        });
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
        loadData();
    }

    /**
     * 注册RxBus
     */
    private void initRxBus() {
        mRxBus = RxBus.getInstanceBus();
        mRxBus.registerRxBus(mRxBus, RxBusMessage.class, rxBusMessage -> {
            //根据事件类型进行处理
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_ADDRESS_LIST) {
                if (rxBusMessage.getI() == 0) {
                    //刷新列表
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

    @Override
    public void onBackPressed() {
        if (mType == 1) {
            //订单界面过来 返回时看地址是否全被删除了
            if (mData == null || mData.size() < 1) {
                RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_ORDER_ADDRESS, null));
                finish();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    public static void goPage(Context context, int type) {
        Intent intent = new Intent(context, AddressActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    public static void goPage(Context context, int type, int postition) {
        Intent intent = new Intent(context, AddressActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("position", postition);
        context.startActivity(intent);
    }
}
