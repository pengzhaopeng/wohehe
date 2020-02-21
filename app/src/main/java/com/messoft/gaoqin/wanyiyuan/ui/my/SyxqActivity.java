package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.bean.MemberSettlementLineStatistics;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivitySyxqBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.WalletModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;

/**
 * 我的收益详情
 */
public class SyxqActivity extends BaseActivity<ActivitySyxqBinding> {

    private WalletModel mModel;

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syxq);
    }

    @Override
    protected void initSetting() {
        showContentView();
        mModel = new WalletModel();

        LoginMemberInfo userData = BusinessUtils.getUserData();
        ImgLoadUtil.displayCircle(bindingView.ivHeader, SysUtils.getImgURL(userData.getImgName()));
        bindingView.tvTitle.setText(BusinessUtils.memberLevel(userData.getRoleId()));

        loadData();
    }


    @Override
    protected void initListener() {
        bindingView.ivBack.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadData() {
        mModel.getMemberSettlementLineStatistics(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                MemberSettlementLineStatistics data = (MemberSettlementLineStatistics) object;
                bindingView.tvMoney1.setText(data.getProductGainsTotal());
                bindingView.tvMoney2.setText(data.getAgentGainsTotal());

                bindingView.tv12.setText(data.getGainsTotalToday());
                bindingView.tv15.setText(data.getOrderToday());

                bindingView.tv22.setText(data.getGainsTotalYesterday());
                bindingView.tv25.setText(data.getOrderYesterday());

                bindingView.tv32.setText(data.getGainsTotalWeek());
                bindingView.tv35.setText(data.getOrderWeek());

                bindingView.tv42.setText(data.getGainsTotalMonth());
                bindingView.tv45.setText(data.getOrderMonth());
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {

            }
        });
    }
}
