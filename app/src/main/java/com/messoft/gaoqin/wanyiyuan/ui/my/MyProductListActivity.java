package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.MyFragmentPagerAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.ProductInfo;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityMyProductListBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.ProductModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的商品
 */
public class MyProductListActivity extends BaseActivity<ActivityMyProductListBinding> {

    private ProductModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product_list);
    }

    @Override
    protected void initSetting() {
        showContentView();
        setTitle("我的商品");
        mModel = new ProductModel();
        //状态：0：待付款，1：待发货，2：已发货
        ArrayList<Fragment> mFragments = new ArrayList<>();
        ArrayList<String> mTitleList = new ArrayList<>();
        mTitleList.add("待审核");
        mTitleList.add("审核失败");
        mTitleList.add("展示中");
        mTitleList.add("已下架");
        mFragments.add(MyProductListFragment.newInstance(0));
        mFragments.add(MyProductListFragment.newInstance(1));
        mFragments.add(MyProductListFragment.newInstance(2));
        mFragments.add(MyProductListFragment.newInstance(3));
        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitleList);
        bindingView.vpLinli.setAdapter(myAdapter);
        // 左右预加载页面的个数
        bindingView.vpLinli.setOffscreenPageLimit(mTitleList.size() - 1);
        myAdapter.notifyDataSetChanged();
        bindingView.tabLinli.setTabMode(TabLayout.MODE_FIXED);
        bindingView.tabLinli.setupWithViewPager(bindingView.vpLinli);
    }

    @Override
    protected void initListener() {
        bindingView.tvNext.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                mModel.getProductList(getActivity(), BusinessUtils.getBindAccountId(), null, null, "0", "0",
                        HttpUtils.start_page_java, HttpUtils.per_page_20, new RequestImpl() {
                            @Override
                            public void loadSuccess(Object object) {
                                List<ProductInfo> data = (List<ProductInfo>) object;
                                if (data != null && data.size() > 0) {
                                    ToastUtil.showShortToast("您当前有商品正在审核，请等待审核后再上传新的商品！");
                                } else {
                                    MyShenheProductActivity.goPage(getActivity(), 2, "");
                                }
                            }

                            @Override
                            public void loadFailed(int errorCode, String errorMessage) {
                                ToastUtil.showShortToast(errorMessage);
                            }
                        });
            }

        });
    }
}
