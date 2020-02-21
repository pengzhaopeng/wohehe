package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.MyJsOrderAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseFragment;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.bean.GetRushGoodsList;
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

import java.util.List;
import java.util.Objects;

/**
 * 待寄售list
 */
public class JsFragment extends BaseFragment<CommonListBinding> {
    // 初始化完成后加载数据
    private boolean mIsPrepared = false;
    // 第一次显示时加载数据，第二次不显示
    private boolean mIsFirst = true;
    private int mPage = HttpUtils.start_page_java;
    private MyJsOrderAdapter mAdapter;
    private RxBus mRxBus;
    private QgModel mGoldModel;

//    private int mType; //

    public static JsFragment newInstance() {
        JsFragment fragment = new JsFragment();
        Bundle args = new Bundle();
//        args.putInt("type", type);
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
//        if (getArguments() != null) {
//            mType = getArguments().getInt("type");
//        }
    }

    @Override
    protected void initSetting() {
//        showContentView();
        initRxBus();
        mAdapter = new MyJsOrderAdapter(getBaseActivity());
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

        mAdapter.setOnItemClickListener(new OnItemClickListener<GetRushGoodsList>() {
            @Override
            public void onClick(GetRushGoodsList getRushGoodsList, int position) {
                if (getRushGoodsList != null) {
                    MyJsInfoActivity.goPage(getActivity(),getRushGoodsList.getGoodsId());
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
        mGoldModel.getRushGoodsList(getBaseActivity(),
                BusinessUtils.getBindAccountId(),
                BusinessUtils.getMobile(),
                "1",
                null,
                mPage, HttpUtils.per_page_20, new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        showContentView();
                        mIsFirst = false;
                        List<GetRushGoodsList> data = (List<GetRushGoodsList>) object;
                        if (mPage == HttpUtils.start_page_java) {
                            if (data.size() > 0) {
                                mAdapter.clear();
                                mAdapter.addAll(data);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.clear();
                                mAdapter.notifyDataSetChanged();
                                showNoData("暂无待寄售订单");
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
//            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_ORDER_ADDRESS) {
//                if (rxBusMessage.getData() != null) {
//                    AddressBean getAddress = (AddressBean) rxBusMessage.getData();
//                    int position = rxBusMessage.getI();
//                    if (getAddress != null && mAdapter != null && mAdapter.getData() != null &&
//                            mAdapter.getData().size() >= position) {
//                        //补录地址
//                        GetRushOrderList getRushOrderList = mAdapter.getData().get(position);
//                        buLuAdddress(getAddress, getRushOrderList);
//                    }
//
//                }
//            }
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_QG_ORDER_LIST) {
                if (rxBusMessage.getI() == 0) {
                    //刷新数据
                    requestData();
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
