package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.JsAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.bean.MemberSettlementLis;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonListBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.WalletModel;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.RecycleViewDivider;

import java.util.List;

/**
 * 代理结算
 */
public class DaiLiJieSuanActivity extends BaseActivity<CommonListBinding> {

    private JsAdapter mAdapter;
    private WalletModel mModel;
    private int mPage = HttpUtils.start_page_java;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_list);
    }

    @Override
    protected void initSetting() {
        setTitle("代理结算");
        mModel = new WalletModel();

        mAdapter = new JsAdapter();
        initOnRefresh(getActivity(), bindingView.rc);
//        bindingView.rcAddress.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        bindingView.rc.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL,
                DensityUtils.dp2px(getActivity(), 2),
                getResources().getColor(R.color.colorPageBg)));
        bindingView.rc.setAdapter(mAdapter);
        requestData();
    }

    @Override
    protected void initListener() {
        bindingView.refreshLayout.setOnRefreshListener(refreshlayout -> refreshlayout.getLayout().postDelayed(() -> {
            mPage = HttpUtils.start_page_java;
            requestData();
            refreshlayout.finishRefresh();
        }, 300));
        bindingView.refreshLayout.setOnLoadmoreListener(refreshlayout -> refreshlayout.getLayout().postDelayed(() -> {
            mPage++;
            requestData();
            refreshlayout.finishLoadmore();
        }, 300));

        mAdapter.setOnItemClickListener(new OnItemClickListener<MemberSettlementLis>() {
            @Override
            public void onClick(MemberSettlementLis memberSettlementLis, int position) {
                DaiLiJieSuanInfoActivity.goPage(getActivity(),memberSettlementLis);
            }
        });
    }

    private void requestData() {
        mModel.getMemberSettlementLis(getActivity(),mPage, HttpUtils.per_page_20, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                showContentView();
                List<MemberSettlementLis> data = (List<MemberSettlementLis>) object;
                if (mPage == HttpUtils.start_page_java) {
                    if (data != null && data.size() > 0) {
                        mAdapter.clear();
                        mAdapter.addAll(data);
                        mAdapter.notifyDataSetChanged();
                    } else {
//                        ToastUtil.showShortToast("没有列表信息");
                        showNoData("没有列表信息");
                        mAdapter.clear();
                        mAdapter.notifyDataSetChanged();
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
                showError();
            }
        });
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
        mPage = HttpUtils.start_page_java;
        requestData();
    }
}
