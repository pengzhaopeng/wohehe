package com.messoft.gaoqin.wanyiyuan.ui.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.ProductListJfAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.bean.ProductInfo;
import com.messoft.gaoqin.wanyiyuan.bean.ProductType;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonListBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.ProductModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.utils.UIUtils;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.GridSpacingItemDecoration;

import java.util.List;

/**
 * 积分商城全部商品
 */
public class AllJfProductListActivity extends BaseActivity<CommonListBinding> {

    private int mPage = HttpUtils.start_page_java;

    private String mId; //积分商城id

    private ProductModel mProductModel;
    private ProductListJfAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_list);
    }

    @Override
    protected void initSetting() {
        setTitle("全部商品");

        mProductModel = new ProductModel();
        mAdapter = new ProductListJfAdapter(getActivity());
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        bindingView.rc.setNestedScrollingEnabled(false);
        bindingView.rc.setHasFixedSize(false);
        bindingView.rc.setLayoutManager(mLayoutManager);

        bindingView.rc.addItemDecoration(new GridSpacingItemDecoration(
                2,
                UIUtils.dip2Px(10),
                true));
        bindingView.rc.setAdapter(mAdapter);

        loadProductTag();
    }

    @Override
    protected void initListener() {
        bindingView.refreshLayout.setOnRefreshListener(refreshlayout -> refreshlayout.getLayout().postDelayed(() -> {
            mPage = HttpUtils.start_page_java;
            loadData();
            refreshlayout.finishRefresh();
        }, 300));
        bindingView.refreshLayout.setOnLoadmoreListener(refreshlayout -> refreshlayout.getLayout().postDelayed(() -> {
            mPage++;
            loadData();
            refreshlayout.finishLoadmore();
        }, 300));

        mAdapter.setOnItemClickListener(new OnItemClickListener<ProductInfo>() {
            @Override
            public void onClick(ProductInfo productInfo, int position) {
                ProductInfoActivity.goPage(getActivity(), productInfo.getId(), 0);
            }
        });
    }

    /**
     * 商品分类
     */
    private void loadProductTag() {
        mProductModel.getAreaList(getActivity(), BusinessUtils.getBindAccountId(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                List<ProductType> mTabList = (List<ProductType>) object;
                if (mTabList != null && mTabList.size() > 0) {
//                    setProduct(mTabList);
                    setProduct1(mTabList);
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    private void setProduct1(List<ProductType> data) {
        mId = null;
        for (ProductType getChannelList : data) {
            if (getChannelList.getCode().equals("jfsc")) {
                mId = getChannelList.getId();
                break;
            }
        }
        loadData();
    }

    private void loadData() {
        mProductModel.getProductList(getActivity(), null, mId, "1", "0", null,
                mPage, HttpUtils.per_page_20, new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        showContentView();
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
        loadData();
    }
}
