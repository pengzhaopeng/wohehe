package com.messoft.gaoqin.wanyiyuan.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;

import com.messoft.gaoqin.wanyiyuan.adapter.ProductListAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseFragment;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.bean.ProductInfo;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.ProductModel;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.utils.UIUtils;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.GridSpacingItemDecoration;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonListBinding;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

import java.util.List;

public class ProductListFragment extends BaseFragment<CommonListBinding> {
    // 初始化完成后加载数据
    private boolean mIsPrepared = false;
    // 第一次显示时加载数据，第二次不显示
    private boolean mIsFirst = true;

    private String id;
    private int type; //-1 1.0版本默认不操作 0-生活必备

    private ProductModel mProductModel;
    private ProductListAdapter mAdapter;
    private int mPage = HttpUtils.start_page_java;

    @Override
    protected int setContent() {
        return R.layout.common_list;
    }

    public static ProductListFragment newInstance(String id,int type) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putInt("type",type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
            type = getArguments().getInt("type");
        }
    }

    @Override
    protected void initSetting() {
        mProductModel = new ProductModel();
        mAdapter = new ProductListAdapter(getActivity());
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        bindingView.rc.setNestedScrollingEnabled(false);
        bindingView.rc.setHasFixedSize(false);
        bindingView.rc.setLayoutManager(mLayoutManager);

        bindingView.rc.addItemDecoration(new GridSpacingItemDecoration(
                2,
                UIUtils.dip2Px(10),
                true));
        bindingView.rc.setAdapter(mAdapter);

        mIsPrepared = true;
        loadData();
    }

    @Override
    protected void initListener() {
        bindingView.refreshLayout.setEnableRefresh(false);
//        bindingView.refreshLayout.setEnableRefresh(false);

        bindingView.refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage++;
                        requestData();
                        refreshlayout.finishLoadmore();
                    }
                }, 300);

            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener<ProductInfo>() {
            @Override
            public void onClick(ProductInfo productInfo, int position) {
                //商品详情
                if (productInfo != null && productInfo.getId() != null) {
                    ProductInfoActivity.goPage(getActivity(), productInfo.getId(), 2);
//                    ProductInfoActivity.goPage(getActivity(), "22", 0);
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
        mProductModel.getProductList(getBaseActivity(), null, id, "1", "0", null,
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

    @Override
    protected void onRefresh() {
        super.onRefresh();
        mPage = HttpUtils.start_page_java;
        requestData();
    }
}
