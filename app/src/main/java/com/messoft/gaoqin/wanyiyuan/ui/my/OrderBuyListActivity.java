package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.MyFragmentPagerAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonToolbarPageBinding;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;

import java.util.ArrayList;

/**
 * 订单列表
 * 我是买家
 */
public class OrderBuyListActivity extends BaseActivity<CommonToolbarPageBinding> {

    private int mType;

    @Override
    public void onBackPressed() {
        //通知外面刷新
        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_MY_INFO, 0));
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_toolbar_page);
    }

    @Override
    protected void initSetting() {
        showContentView();
        setTitle("我的订单");
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
        }
        String bindAccountId = BusinessUtils.getBindAccountId();
        //状态：0：待付款，1：待发货，2：已发货
        ArrayList<Fragment> mFragments = new ArrayList<>();
        ArrayList<String> mTitleList = new ArrayList<>();
        mTitleList.add("全部");
        mTitleList.add("待付款");
        mTitleList.add("待发货");
        mTitleList.add("待收货");
        mTitleList.add("待评价");
        mTitleList.add("已完成");
        //0:等待买家付款 1:等待卖家发货(买家已付款) 2:等待买家确认收货(卖家已发货), 3:买家已签收(货到付款) 4.交易成功 5.交易关闭 100:已取消
        mFragments.add(OrderListFragment.newInstance(0, -1, null, bindAccountId, "", -1));
        mFragments.add(OrderListFragment.newInstance(0, 0, null, bindAccountId, "", -1));
        mFragments.add(OrderListFragment.newInstance(0, 1, null, bindAccountId, "", -1));
        mFragments.add(OrderListFragment.newInstance(0, 2, null, bindAccountId, "", -1));
        mFragments.add(OrderListFragment.newInstance(0, 4, null, bindAccountId, "", 0));
        mFragments.add(OrderListFragment.newInstance(0, -1, "4,5", bindAccountId, "", -1));
        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitleList);
        bindingView.vpLinli.setAdapter(myAdapter);
        // 左右预加载页面的个数
        bindingView.vpLinli.setOffscreenPageLimit(mTitleList.size() - 1);
        myAdapter.notifyDataSetChanged();
        bindingView.tabLinli.setTabMode(TabLayout.MODE_SCROLLABLE);
        bindingView.tabLinli.setupWithViewPager(bindingView.vpLinli);


        switch (mType) {
            case 0:
//                bindingView.tabLinli.setScrollPosition(1,0,true);
                bindingView.vpLinli.setCurrentItem(1);
                break;
            case 1:
//                bindingView.tabLinli.setScrollPosition(2,0,true);
                bindingView.vpLinli.setCurrentItem(2);
                break;
            case 2:
//                bindingView.tabLinli.setScrollPosition(3,0,true);
                bindingView.vpLinli.setCurrentItem(3);
                break;
            case 3:
//                bindingView.tabLinli.setScrollPosition(5,0,true);
                bindingView.vpLinli.setCurrentItem(5);
                break;

        }
    }

    @Override
    protected void initListener() {

    }

    public static void goPage(Context context, int type) {
        Intent intent = new Intent(context, OrderBuyListActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }
}
