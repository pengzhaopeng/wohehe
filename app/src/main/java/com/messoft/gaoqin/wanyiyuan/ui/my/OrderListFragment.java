package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.OrderAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseFragment;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.bean.OrderInfo;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonListBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.OrderModel;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.RecycleViewDivider;

import java.util.List;
import java.util.Objects;

/**
 * 订单列表
 */
public class OrderListFragment extends BaseFragment<CommonListBinding> {

    // 初始化完成后加载数据
    private boolean mIsPrepared = false;
    // 第一次显示时加载数据，第二次不显示
    private boolean mIsFirst = true;
    private RxBus mRxBus;

    private int mPage = HttpUtils.start_page_java;
    private OrderAdapter mAdapter;

    private OrderModel mModel;
    private String mMemberId;
    private String mCompanyId;
    //0:等待买家付款 1:等待卖家发货(买家已付款) 2:等待买家确认收货(卖家已发货), 3:买家已签收(货到付款) 4.交易成功 5.交易关闭 100:已取消
    private int mHandleStatus;
    private int mIsComment; //是否评论
    private int mType; //private int mType; //0-买家  1-卖家 2-售后(不区分买家和卖家)
    private String mHandleStatuses;

    public static OrderListFragment newInstance(int type,int handleStatus,String handleStatuses, String memberId,String companyId,int isComment) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("handleStatus", handleStatus);
        args.putString("handleStatuses", handleStatuses);
        args.putInt("isComment", isComment);
        args.putString("memberId", memberId);
        args.putString("companyId", companyId);
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
            mHandleStatus = getArguments().getInt("handleStatus");
            mHandleStatuses = getArguments().getString("handleStatuses");
            mIsComment = getArguments().getInt("isComment");
            mMemberId = getArguments().getString("memberId");
            mCompanyId = getArguments().getString("companyId");
        }
    }

    @Override
    protected void initSetting() {
//        showContentView();
        initRxBus();
        mModel = new OrderModel();
        mAdapter = new OrderAdapter(getBaseActivity(),mType);
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

        mAdapter.setOnItemClickListener(new OnItemClickListener<OrderInfo>() {
            @Override
            public void onClick(OrderInfo orderInfo, int position) {

                OrderInfoActivity.goPage(getContext(),orderInfo.getId(),mType);
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
        mModel.getOrderList(getBaseActivity(),mHandleStatus,mHandleStatuses, mMemberId, mCompanyId, mIsComment,mPage, HttpUtils.per_page_20, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                mIsFirst = false;
                showContentView();
                List<OrderInfo> data = (List<OrderInfo>) object;
                if (mPage == HttpUtils.start_page_java) {
                    if (data != null && data.size() > 0) {
                        mAdapter.clear();
                        mAdapter.addAll(data);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter.clear();
                        mAdapter.notifyDataSetChanged();
                        showNoData("暂无订单");
                    }
                } else {
                    mAdapter.addAll(data);
                    mAdapter.notifyDataSetChanged();
                    bindingView.refreshLayout.finishLoadmore();
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
//                ToastUtil.showShortToast(errorMessage);
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
