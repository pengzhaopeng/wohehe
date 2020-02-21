package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.MyFragmentPagerAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonToolbarPageBinding;

import java.util.ArrayList;

/**
 * 我的预约抢购
 */
public class MyQgListActivity extends BaseActivity<CommonToolbarPageBinding> {

    public static final int my_qg_dfu = 2; //买家待付款
    public static final int my_qg_yfk = 4; //买家已付款
    public static final int my_qg_ywc = 14; //买家已完成

    public static final int my_js_dsk = 2; //卖家待收款
    public static final int my_js_dfx = 4; //卖家待放行
    public static final int my_js_ywc = 8; //卖家已完成

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_toolbar_page);
    }

    @Override
    protected void initSetting() {
        showContentView();
        setTitle("我的预约抢购");

        //状态：0：待付款，1：待发货，2：已发货
        ArrayList<Fragment> mFragments = new ArrayList<>();
        ArrayList<String> mTitleList = new ArrayList<>();
        mTitleList.add("待付款");
        mTitleList.add("已付款");
        mTitleList.add("已完成");
        ArrayList<Integer> arr1 = new ArrayList<>();
        arr1.add(MyQgListActivity.my_qg_dfu);
        mFragments.add(GgFragment.newInstance(MyQgListActivity.my_qg_dfu,arr1,0));
        ArrayList<Integer> arr2 = new ArrayList<>();
        arr2.add(MyQgListActivity.my_qg_yfk);
        mFragments.add(GgFragment.newInstance(MyQgListActivity.my_qg_yfk,arr2,0));
        ArrayList<Integer> arr3 = new ArrayList<>();
        arr3.add(6);
        arr3.add(8);
        arr3.add(9);
        arr3.add(10);
        arr3.add(12);
        arr3.add(14);
        mFragments.add(GgFragment.newInstance(MyQgListActivity.my_qg_ywc,arr3,0));
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

    }
}
