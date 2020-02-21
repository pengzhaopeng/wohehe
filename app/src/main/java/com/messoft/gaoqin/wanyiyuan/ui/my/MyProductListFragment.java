package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.MyProductListAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseFragment;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.bean.ProductInfo;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonListBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.ProductModel;
import com.messoft.gaoqin.wanyiyuan.ui.home.ProductInfoActivity;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.RecycleViewDivider;

import java.util.List;
import java.util.Objects;

/**
 * 我的商品列表
 */
public class MyProductListFragment extends BaseFragment<CommonListBinding> {

    // 初始化完成后加载数据
    private boolean mIsPrepared = false;
    // 第一次显示时加载数据，第二次不显示
    private boolean mIsFirst = true;

    private int mPage = HttpUtils.start_page_java;
    private MyProductListAdapter mAdapter;

    private ProductModel mModel;

    private int mType;
    private RxBus mRxBus;


    public static MyProductListFragment newInstance(int type) {
        MyProductListFragment fragment = new MyProductListFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
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
        }
    }

    @Override
    protected void initSetting() {
//        showContentView();
        initRxBus();
        mModel = new ProductModel();
        mAdapter = new MyProductListAdapter(getBaseActivity(), mType);
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

        mAdapter.setOnItemClickListener(new OnItemClickListener<ProductInfo>() {
            @Override
            public void onClick(ProductInfo productInfo, int position) {
                if (mType == 0 || mType == 1) {
                    //商品详情
                    MyShenheProductActivity.goPage(getContext(), 0, productInfo.getId());
                } else {
                    ProductInfoActivity.goPage(getActivity(), productInfo.getId(), 1);
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
        String isSale = null;
        String auditState = "0";
        //审核状态 0.待审核 1.审核通过 2.审核不通过
        switch (mType) {
            case 0:
                auditState = "0";
                isSale = null;
                break;
            case 1:
                auditState = "2";
                isSale = null;
                break;
            case 2:
                auditState = null;
                isSale = "1";
                break;
            case 3:
                isSale = "0";
                auditState = null;
                break;
        }
        mModel.getProductList(getBaseActivity(), BusinessUtils.getBindAccountId(), null, isSale, "0", auditState,
                mPage, HttpUtils.per_page_20, new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        showContentView();
                        mIsFirst = false;
                        List<ProductInfo> data = (List<ProductInfo>) object;
                        if (mPage == HttpUtils.start_page_java) {
                            if (data.size() > 0) {
                                mAdapter.clear();
                                mAdapter.addAll(data);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.clear();
                                mAdapter.notifyDataSetChanged();
                                showNoData("暂无商品");
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

    /**
     * 注册RxBus
     */
    private void initRxBus() {
        mRxBus = RxBus.getInstanceBus();
        mRxBus.registerRxBus(mRxBus, RxBusMessage.class, rxBusMessage -> {
            //根据事件类型进行处理
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_MY_PRODUCT_LIST) {
                if (rxBusMessage.getI() == 0) {
                    //更新数据
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

    @Override
    protected void onRefresh() {
        super.onRefresh();
        mPage = HttpUtils.start_page_java;
        requestData();
    }

}
