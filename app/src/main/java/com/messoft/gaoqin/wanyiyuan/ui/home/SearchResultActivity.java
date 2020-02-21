package com.messoft.gaoqin.wanyiyuan.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.ProductListAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.bean.ProductInfo;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonListBinding;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.utils.UIUtils;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.GridSpacingItemDecoration;

import org.json.JSONObject;

import java.util.List;

public class SearchResultActivity extends BaseActivity<CommonListBinding> {

    private int mPage = HttpUtils.start_page_java;
    private int mType;
    private String key;
    private ProductListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_list);
    }

    @Override
    protected void initSetting() {
        showContentView();
        if (getIntent() != null) {
            //type 0-搜索商品
            mType = getIntent().getIntExtra("type", -1);
            key = getIntent().getStringExtra("key");
            switch (mType) {
                case 0:
                    setTitle("搜索结果");
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

                    //加载商品
                    loadProduct();

                    mAdapter.setOnItemClickListener(new OnItemClickListener<ProductInfo>() {
                        @Override
                        public void onClick(ProductInfo productInfo, int position) {
                            //商品详情
                            if (productInfo != null && productInfo.getId() != null) {
                                ProductInfoActivity.goPage(getActivity(), productInfo.getId(), 0);
                            }
                        }
                    });

                    break;
            }
        }
    }

    @Override
    protected void initListener() {

    }

    private void loadProduct() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("isSale", 1);
            jsonObject.put("isDel", 0);
            jsonObject.put("name", key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getProductList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), mPage, HttpUtils.per_page_20)
                .compose(RxSchedulers.compose())
                .compose(getActivity().bindToLifecycle())
                .subscribe(new BaseObserver<List<ProductInfo>>(getActivity(), false, "加载中...") {
                    @Override
                    protected void onSuccess(List<ProductInfo> login) {
                        showContentView();
                        List<ProductInfo> data = login;
                        if (mPage == HttpUtils.start_page_java) {
                            if (data.size() > 0) {
                                mAdapter.clear();
                                mAdapter.addAll(data);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.clear();
                                mAdapter.notifyDataSetChanged();
                                showNoData("没有搜索到商品");
                            }
                        } else {
                            mAdapter.addAll(data);
                            mAdapter.notifyDataSetChanged();
                            bindingView.refreshLayout.finishLoadmore();
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        ToastUtil.showShortToast(msg);
                        showError();
                    }
                });

    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
        mPage = HttpUtils.start_page_java;
        loadProduct();
    }

    public static void goPage(Context context, String key,int type) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("key", key);
        context.startActivity(intent);
    }
}
