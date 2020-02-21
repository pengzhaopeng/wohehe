package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.HomeMenuAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.AgentMemberIndexInfo;
import com.messoft.gaoqin.wanyiyuan.bean.HomeMenu;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityDaiLiZhuanQuBinding;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 代理专区
 */
public class DaiLiZhuanQuActivity extends BaseActivity<ActivityDaiLiZhuanQuBinding> {

    private HomeMenuAdapter mHomeMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dai_li_zhuan_qu);
    }

    @Override
    protected void initSetting() {
        setTitle("代理专区");
        showContentView();

        LoginMemberInfo userData = BusinessUtils.getUserData();
        ImgLoadUtil.displayCircle(bindingView.ivHeader, SysUtils.getImgURL(userData.getImgName()));
        bindingView.tvTitle.setText(BusinessUtils.memberLevel(userData.getRoleId()));

        //底部menu
        initMenu();

        loadData();
    }

    @Override
    protected void initListener() {

    }

    private void loadData() {
        HttpClient.Builder.getWYYServer().getAgentMemberIndexInfo("")
                .compose(RxSchedulers.compose())
                .compose(this.bindToLifecycle())
                .subscribe(new BaseObserver<AgentMemberIndexInfo>(this, true, "加载中...") {
                    @Override
                    protected void onSuccess(AgentMemberIndexInfo login) {
                        bindingView.tvMoney1.setText(login.getAgentAmount());
                        bindingView.tvMoney2.setText(login.getSaleAmount());
                        bindingView.tvDfh.setText(login.getDeliverNum());
                        bindingView.tvWxfh.setText(login.getTransferNum());
                        bindingView.tvSy.setText(login.getAgentAmountToday());
                        bindingView.tvDds.setText(login.getSaleAmountToday());
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        ToastUtil.showShortToast(msg);
                    }
                });
    }

    /**
     * 功能大全
     */
    private void initMenu() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayoutManager.setAutoMeasureEnabled(true);
        bindingView.rc.setLayoutManager(gridLayoutManager);
        bindingView.rc.setNestedScrollingEnabled(false);
        bindingView.rc.setHasFixedSize(false);

        mHomeMenuAdapter = new HomeMenuAdapter(getActivity());

        List<HomeMenu> menuList = new ArrayList<>();
        menuList.add(new HomeMenu("商品管理", R.drawable.spgl));
        menuList.add(new HomeMenu("订单管理", R.drawable.ddgl));
        menuList.add(new HomeMenu("收益管理", R.drawable.sygl));
        menuList.add(new HomeMenu("代理管理", R.drawable.dlgl));
        menuList.add(new HomeMenu("代理结算", R.drawable.dljs));
        mHomeMenuAdapter.addAll(menuList);
        bindingView.rc.setAdapter(mHomeMenuAdapter);

        mHomeMenuAdapter.setOnItemClickListener((homeMenu, position) -> {
            clickMenu(position);
        });
    }

    private void clickMenu(int position) {
        switch (position) {
            case 0://商品管理
                SysUtils.startActivity(getActivity(), MyProductListActivity.class);
                break;
            case 1://订单管理
                SysUtils.startActivity(getActivity(), OrderSellListActivity.class);
                break;
            case 2://收益管理
                SysUtils.startActivity(getActivity(), SyxqActivity.class);
                break;
            case 3://代理管理
                MenberInfoActivity.goPage(getActivity(), BusinessUtils.getMobile(), "我的", "1");
                break;
            case 4://代理结算
                SysUtils.startActivity(getActivity(),DaiLiJieSuanActivity.class);
                break;
        }
    }
}
