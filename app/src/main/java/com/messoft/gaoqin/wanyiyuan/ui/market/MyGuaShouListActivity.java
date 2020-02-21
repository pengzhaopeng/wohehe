package com.messoft.gaoqin.wanyiyuan.ui.market;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.messoft.gaoqin.wanyiyuan.adapter.MyFragmentPagerAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.R;

import com.messoft.gaoqin.wanyiyuan.databinding.CommonToolbarPageBinding;

import java.util.ArrayList;

public class MyGuaShouListActivity extends BaseActivity<CommonToolbarPageBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_toolbar_page);
    }

    @Override
    protected void initSetting() {
        showContentView();
        setTitle("我的挂售");

        //状态：0：待交易，1：待收款，2：待放行 3：已完成
        ArrayList<Fragment> mFragments = new ArrayList<>();
        ArrayList<String> mTitleList = new ArrayList<>();
        mTitleList.add("待交易");
        mTitleList.add("待收款");
        mTitleList.add("待放行");
        mTitleList.add("已完成");
        mFragments.add(GuaShouFragment.newInstance(0));
        mFragments.add(GuaShouFragment.newInstance(1));
        mFragments.add(GuaShouFragment.newInstance(2));
        mFragments.add(GuaShouFragment.newInstance(3));
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
