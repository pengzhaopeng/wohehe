package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.MyBillAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.MemberBillStatistics;
import com.messoft.gaoqin.wanyiyuan.bean.MyBillBean;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityMyBillBinding;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的账单
 */
public class MyBillActivity extends BaseActivity<ActivityMyBillBinding> {

    private MyBillAdapter mHomeMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bill);
    }

    @Override
    protected void initSetting() {
        showContentView();
        setTitle("个人账单总览");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setAutoMeasureEnabled(true);
        bindingView.rc.setLayoutManager(gridLayoutManager);
        bindingView.rc.setNestedScrollingEnabled(false);
        bindingView.rc.setHasFixedSize(false);

        mHomeMenuAdapter = new MyBillAdapter(getActivity());
        bindingView.rc.setAdapter(mHomeMenuAdapter);

        mHomeMenuAdapter.setOnItemClickListener((homeMenu, position) -> {
            MyBillInfoActivity.goPage(getActivity(), position + 1, homeMenu.getTitle(),homeMenu.getMoney());
        });

        loadData();
    }

    @Override
    protected void initListener() {

    }

    private void loadData() {

        HttpClient.Builder.getWYYServer().getMemberBillStatistics("")
                .compose(RxSchedulers.compose())
                .compose(this.bindToLifecycle())
                .subscribe(new BaseObserver<MemberBillStatistics>(this, true, "加载中...") {
                    @Override
                    protected void onSuccess(MemberBillStatistics login) {
                        List<MyBillBean> menuList = new ArrayList<>();
                        menuList.add(new MyBillBean("投资额", "#FF7676", login.getInvestAmount()));
                        menuList.add(new MyBillBean("批发区收益", "#FF88EF", login.getWholesaleAmount()));
                        menuList.add(new MyBillBean("转让获利", "#9087FF", login.getTransferAmount()));
                        menuList.add(new MyBillBean("分销奖励", "#5ED0FF", login.getDistributionAmount()));
                        menuList.add(new MyBillBean("管理奖励", "#67FF86", login.getAgentAmount()));
                        menuList.add(new MyBillBean("商品售卖", "#FF9800", login.getSaleProductAmount()));
                        mHomeMenuAdapter.addAll(menuList);
                        mHomeMenuAdapter.notifyDataSetChanged();
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        ToastUtil.showShortToast(msg);
                    }
                });
    }


}
