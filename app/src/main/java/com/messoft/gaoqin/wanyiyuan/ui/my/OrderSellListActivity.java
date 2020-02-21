package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.messoft.gaoqin.wanyiyuan.adapter.MyFragmentPagerAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonToolbarPageBinding;

import java.util.ArrayList;

/**
 * 我是卖家
 */
public class OrderSellListActivity extends BaseActivity<CommonToolbarPageBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_toolbar_page);
    }

    @Override
    protected void initSetting() {
        showContentView();
        setTitle("我的订单");
        String bindAccountId = BusinessUtils.getBindAccountId();
        //状态：0：待付款，1：待发货，2：已发货
        ArrayList<Fragment> mFragments = new ArrayList<>();
        ArrayList<String> mTitleList = new ArrayList<>();
        mTitleList.add("全部");
        mTitleList.add("待发货");
        mTitleList.add("待买家收货");
        mTitleList.add("已完成");
        //0:等待买家付款 1:等待卖家发货(买家已付款) 2:等待买家确认收货(卖家已发货), 3:买家已签收(货到付款) 4.交易成功 5.交易关闭 100:已取消
        mFragments.add(OrderListFragment.newInstance(1,-1, null,"", bindAccountId, -1));
        mFragments.add(OrderListFragment.newInstance(1,1,  null,"", bindAccountId, -1));
        mFragments.add(OrderListFragment.newInstance(1,2,  null,"", bindAccountId, -1));
        mFragments.add(OrderListFragment.newInstance(1,-1,  "4,5","", bindAccountId, -1));
        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitleList);
        bindingView.vpLinli.setAdapter(myAdapter);
        // 左右预加载页面的个数
        bindingView.vpLinli.setOffscreenPageLimit(mTitleList.size() - 1);
        myAdapter.notifyDataSetChanged();
        bindingView.tabLinli.setTabMode(TabLayout.MODE_SCROLLABLE);
        bindingView.tabLinli.setupWithViewPager(bindingView.vpLinli);
    }

    @Override
    protected void initListener() {

    }
}
