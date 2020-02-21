package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.OrderSHAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseFragment;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.bean.OrderItemList;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonListBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.OrderModel;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.RecycleViewDivider;

import java.util.List;
import java.util.Objects;

/**
 * 订单列表(售后)
 */
public class OrderListShFragment extends BaseFragment<CommonListBinding> {

    // 初始化完成后加载数据
    private boolean mIsPrepared = false;
    // 第一次显示时加载数据，第二次不显示
    private boolean mIsFirst = true;
    private RxBus mRxBus;

    private int mPage = HttpUtils.start_page_java;
    private OrderSHAdapter mAdapter;

    private OrderModel mModel;
    private String mMemberId;
    //subStatus	Long 售后处理状态 0:未申请 1:审核中 2:审核驳回, 3:退款中 4.已退款
    private String subStatus ;

    public static OrderListShFragment newInstance(String memberId, String subStatus, int isComment) {
        OrderListShFragment fragment = new OrderListShFragment();
        Bundle args = new Bundle();
        args.putString("memberId", memberId);
        args.putString("subStatus", subStatus);
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
            mMemberId = getArguments().getString("memberId");
            subStatus = getArguments().getString("subStatus");
        }
    }

    @Override
    protected void initSetting() {
//        showContentView();
        initRxBus();
        mModel = new OrderModel();
        mAdapter = new OrderSHAdapter(getBaseActivity());
        initOnRefresh(getActivity(), bindingView.rc);

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

        mAdapter.setOnItemClickListener(new OnItemClickListener<OrderItemList>() {
            @Override
            public void onClick(OrderItemList orderItemList, int position) {
//                SqshActivity.goPage(getActivity(),orderItemList.getId(),orderItemList.getOrderItemData().get(0).getId(),subStatus);
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
        mModel.getOrderItemList(getBaseActivity(), mMemberId, null, null,subStatus,null, null,mPage, HttpUtils.per_page_20,
                new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        showContentView();
                        mIsFirst = false;
                        List<OrderItemList> data = (List<OrderItemList>) object;
                        if (mPage == HttpUtils.start_page_java) {
                            if (data.size() > 0) {
                                mAdapter.clear();
                                mAdapter.addAll(data);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.clear();
                                mAdapter.notifyDataSetChanged();
                                showNoData("暂无订单记录");
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
     * 注册RxBus
     */
    private void initRxBus() {
        mRxBus = RxBus.getInstanceBus();
        mRxBus.registerRxBus(mRxBus, RxBusMessage.class, rxBusMessage -> {
            //根据事件类型进行处理
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH__ORDER_LIST) {
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
