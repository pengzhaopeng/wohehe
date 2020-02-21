package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.MyQgOrderAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseFragment;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.bean.AddressBean;
import com.messoft.gaoqin.wanyiyuan.bean.GetRushOrderList;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonListBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.QgModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 抢购list
 */
public class GgFragment extends BaseFragment<CommonListBinding> {
    // 初始化完成后加载数据
    private boolean mIsPrepared = false;
    // 第一次显示时加载数据，第二次不显示
    private boolean mIsFirst = true;
    private int mPage = HttpUtils.start_page_java;
    private MyQgOrderAdapter mAdapter;
    private RxBus mRxBus;
    private QgModel mGoldModel;

    private int mType;//标记类型
    private int flagType; //0-抢购 1-寄售
    private ArrayList<Integer> mArr;

    public static GgFragment newInstance(int type, ArrayList arrayList, int flagType) {
        GgFragment fragment = new GgFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("flagType", flagType);
        args.putIntegerArrayList("arr", arrayList);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int setContent() {
        return R.layout.common_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt("type");
            flagType = getArguments().getInt("flagType");
            mArr = getArguments().getIntegerArrayList("arr");
        }
    }

    @Override
    protected void initSetting() {
//        showContentView();
        initRxBus();
        mAdapter = new MyQgOrderAdapter(getBaseActivity(), flagType, flagType == 0 ? mType : -1, flagType == 1 ? mType : -1);
        initOnRefresh(getActivity(), bindingView.rc);
        mGoldModel = new QgModel();
        bindingView.rc.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL,
                DensityUtils.dp2px(Objects.requireNonNull(getActivity()), 6),
                getResources().getColor(R.color.colorPageBg)));
        bindingView.rc.setAdapter(mAdapter);

        // 准备就绪
        mIsPrepared = true;
        loadData();
    }

    @Override
    protected void initListener() {
        bindingView.refreshLayout.setOnRefreshListener(refreshlayout -> refreshlayout.getLayout().postDelayed(() -> {
            mPage = HttpUtils.start_page_java;
            requestData();
            refreshlayout.finishRefresh();
        }, 300));

        bindingView.refreshLayout.setOnLoadmoreListener(refreshlayout -> refreshlayout.getLayout().postDelayed(() -> {
            mPage++;
            requestData();
            refreshlayout.finishLoadmore();
        }, 300));

        mAdapter.setOnItemClickListener(new OnItemClickListener<GetRushOrderList>() {
            @Override
            public void onClick(GetRushOrderList getRushOrderList, int position) {
                if (getRushOrderList != null) {
                    MyQgInfoActivity.goPage(getActivity(), getRushOrderList.getOrderId(), mType, flagType);
                }
            }
        });
    }

    @Override
    protected void loadData() {
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }
        requestData();
    }

    private void requestData() {
        mGoldModel.getRushOrderList(getBaseActivity(),
                flagType,
                BusinessUtils.getBindAccountId(),
                mArr,
                mPage, HttpUtils.per_page_20, new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        showContentView();
                        mIsFirst = false;
                        List<GetRushOrderList> data = (List<GetRushOrderList>) object;
                        if (mPage == HttpUtils.start_page_java) {
                            if (data.size() > 0) {
                                mAdapter.clear();
                                mAdapter.addAll(data);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.clear();
                                mAdapter.notifyDataSetChanged();
                                if (flagType==0) {
                                    showNoData("暂无抢购订单");
                                }else{
                                    showNoData("暂无寄售订单");
                                }
                            }
                        } else {
                            mAdapter.addAll(data);
                            mAdapter.notifyDataSetChanged();
                            bindingView.refreshLayout.finishLoadmore();
                        }
                    }

                    @Override
                    public void loadFailed(int errorCode, String errorMessage) {
                        ToastUtil.showShortToast(errorMessage);
                        showError();
                    }
                });
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
        mPage = HttpUtils.start_page_java;
        requestData();
    }

    /**
     * 补录地址
     *
     * @param getAddress
     * @param data
     */
    private void buLuAdddress(AddressBean getAddress, GetRushOrderList data) {
        //待付款再操作
        if (mType != MyQgListActivity.my_qg_dfu) return;

        mGoldModel.updateRushOrderForm(getBaseActivity(),
                0,
                BusinessUtils.getBindAccountId(),
                data.getOrderId(),
                null,
                null,
                getAddress.getContactName(),
                getAddress.getContactMobile(),
                getAddress.getProvince(),
                getAddress.getProvinceText(),
                getAddress.getCity(),
                getAddress.getCityText(),
                getAddress.getDistrict(),
                getAddress.getDistrictText(),
                getAddress.getAddress(),
                null, new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        ToastUtil.showShortToast("补录地址成功");
                        requestData();
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
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_ORDER_ADDRESS) {
                if (rxBusMessage.getData() != null) {
                    AddressBean getAddress = (AddressBean) rxBusMessage.getData();
                    int position = rxBusMessage.getI();
                    if (getAddress != null && mAdapter != null && mAdapter.getData() != null &&
                            mAdapter.getData().size() >= position) {
                        //补录地址
                        GetRushOrderList getRushOrderList = mAdapter.getData().get(position);
                        buLuAdddress(getAddress, getRushOrderList);
                    }

                }
            }
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_QG_ORDER_LIST) {
                if (rxBusMessage.getI() == 0) {
                    //刷新列表
                    requestData();
                }
            }

            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.PAY_SUCESS_WX) {
//                int i = rxBusMessage.getI();
                //刷新列表
                requestData();
            }

            //支付宝支付成功
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.PAY_SUCESS_ZFB) {
//                int i = rxBusMessage.getI();
                //刷新列表
                requestData();
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
