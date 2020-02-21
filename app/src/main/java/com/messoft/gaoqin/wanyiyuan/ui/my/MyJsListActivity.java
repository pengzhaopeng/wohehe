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
 * 我的寄售
 */
public class MyJsListActivity extends BaseActivity<CommonToolbarPageBinding> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_toolbar_page);
    }

    @Override
    protected void initSetting() {
        showContentView();
        setTitle("我的寄售");

        //状态：0：待付款，1：待发货，2：已发货
        ArrayList<Fragment> mFragments = new ArrayList<>();
        ArrayList<String> mTitleList = new ArrayList<>();
        mTitleList.add("待寄售");
        mTitleList.add("待收款");
        mTitleList.add("待放行");
        mTitleList.add("已完成");
        mFragments.add(JsFragment.newInstance());
        ArrayList<Integer> arr1 = new ArrayList<>();
        arr1.add(MyQgListActivity.my_js_dsk);
        mFragments.add(GgFragment.newInstance(MyQgListActivity.my_js_dsk,arr1, 1));
        ArrayList<Integer> arr2 = new ArrayList<>();
        arr2.add(MyQgListActivity.my_js_dfx);
        mFragments.add(GgFragment.newInstance(MyQgListActivity.my_js_dfx,arr2, 1));
        ArrayList<Integer> arr3 = new ArrayList<>();
        arr3.add(6);
        arr3.add(8);
        arr3.add(9);
        arr3.add(10);
        arr3.add(12);
        arr3.add(14);
        mFragments.add(GgFragment.newInstance(MyQgListActivity.my_js_ywc,arr3, 1));
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
