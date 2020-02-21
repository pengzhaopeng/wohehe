package com.messoft.gaoqin.wanyiyuan.ui.market;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.messoft.gaoqin.wanyiyuan.adapter.MyFragmentPagerAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.R;

import com.messoft.gaoqin.wanyiyuan.databinding.CommonToolbarPageBinding;

import java.util.ArrayList;

/**
 * 我的采购
 */
public class MyCaiGouListActivity extends BaseActivity<CommonToolbarPageBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_toolbar_page);
    }

    @Override
    protected void initSetting() {
        showContentView();
        setTitle("我的采购");

        //状态：0：待付款，1：待发货，2：已发货
        ArrayList<Fragment> mFragments = new ArrayList<>();
        ArrayList<String> mTitleList = new ArrayList<>();
        mTitleList.add("待付款");
        mTitleList.add("已付款");
        mTitleList.add("已完成");
        mFragments.add(CaigouFragment.newInstance(1));
        mFragments.add(CaigouFragment.newInstance(2));
        mFragments.add(CaigouFragment.newInstance(3));
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
