package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;

import com.messoft.gaoqin.wanyiyuan.adapter.FwmxListAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.MemberSettlementLineList;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.WalletModel;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonListBinding;

import java.util.List;

/**
 * 消费
 */
public class XfDetailActivity extends BaseActivity<CommonListBinding> {

    private FwmxListAdapter mAdapter;
    private int mPage = HttpUtils.start_page_java;
    private WalletModel mModel;
    private String mId;
    private String mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_list);
    }

    @Override
    protected void initSetting() {
        setTitle("服务费明细");
        if (getIntent() != null) {
            mId = getIntent().getStringExtra("id");
            mType = getIntent().getStringExtra("type");

        }
        mAdapter = new FwmxListAdapter();
        mModel = new WalletModel();
        initOnRefresh(getActivity(), bindingView.rc);
        bindingView.rc.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        bindingView.rc.addItemDecoration(new RecycleViewDivider(
//                getActivity(), LinearLayoutManager.VERTICAL,
//                UIUtils.dip2Px(1),
//                getResources().getColor(R.color.colorPageBg)));
        bindingView.rc.setAdapter(mAdapter);

        loadData();
    }

    @Override
    protected void initListener() {
        bindingView.refreshLayout.setOnRefreshListener(refreshlayout -> refreshlayout.getLayout().postDelayed(() -> {
            mPage = HttpUtils.start_page_java;
            loadData();
            refreshlayout.finishRefresh();
        }, 300));
        bindingView.refreshLayout.setOnLoadmoreListener(refreshlayout -> refreshlayout.getLayout().postDelayed(() -> {
            mPage++;
            loadData();
            refreshlayout.finishLoadmore();
        }, 300));

    }

    private void loadData() {
        mModel.getMemberSettlementLineList(getActivity(), mId,null, mType,mPage, HttpUtils.per_page_20,
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
                        showError();
                    }
                });
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
        mPage = HttpUtils.start_page_java;
        loadData();
    }

    public static void goPage(Context context,String id,String type) {
        Intent intent = new Intent(context, XfDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }
}
