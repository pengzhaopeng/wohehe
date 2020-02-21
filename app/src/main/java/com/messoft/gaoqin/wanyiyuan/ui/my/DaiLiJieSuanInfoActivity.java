package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.JsListAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.MemberSettlementLineList;
import com.messoft.gaoqin.wanyiyuan.bean.MemberSettlementLis;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityDaiLiJieSuanInfoBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.WalletModel;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.RecycleViewDivider;

import java.util.List;

public class DaiLiJieSuanInfoActivity extends BaseActivity<ActivityDaiLiJieSuanInfoBinding> {

    private JsListAdapter mAdapter;
    private WalletModel mModel;
    private int mPage = HttpUtils.start_page_java;
    private MemberSettlementLis mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dai_li_jie_suan_info);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initSetting() {
        showContentView();
        mModel = new WalletModel();
        setTitle("结算详情");

        if (getIntent() != null) {
            mData = (MemberSettlementLis) getIntent().getSerializableExtra("data");
            bindingView.tvDate.setText(mData.getBeginTime() + "-" + mData.getEndTime());
            bindingView.tvMoney.setText("¥" + mData.getSettlemtnAmount());
        }
        mAdapter = new JsListAdapter(getActivity());
        initOnRefresh(getActivity(), bindingView.rc);
        bindingView.rc.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL,
                DensityUtils.dp2px(getActivity(), 8),
                getResources().getColor(R.color.colorPageBg)));
        bindingView.rc.setAdapter(mAdapter);

        loadData();
    }

    @Override
    protected void initListener() {

    }

    private void loadData() {
        mModel.getMemberSettlementLineList(getActivity(), null, mData.getId(), null, mPage, HttpUtils.per_page_20,
                new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        showContentView();
                        List<MemberSettlementLineList> data = (List<MemberSettlementLineList>) object;
                        if (mPage == HttpUtils.start_page_java) {
                            if (data.size() > 0) {
                                mAdapter.clear();
                                mAdapter.addAll(data);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.clear();
                                mAdapter.notifyDataSetChanged();
                                showNoData("暂无记录");
                            }
                        } else {
                            mAdapter.addAll(data);
                            mAdapter.notifyDataSetChanged();
                            bindingView.refreshLayout.finishLoadmore();
                        }
                    }

                    @Override
                    public void loadFailed(int errorCode, String errorMessage) {
                        ToastUtil.showShortToast(errorMessage);

                    }
                });
    }

    public static void goPage(Context context, MemberSettlementLis data) {
        Intent intent = new Intent(context, DaiLiJieSuanInfoActivity.class);
        intent.putExtra("data", data);
        context.startActivity(intent);
    }
}
