package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.JfAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.GetMemberPointsLogList;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonListBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.WalletModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

import java.util.List;

/**
 * 钱包明细
 */
public class JfDetailActivity extends BaseActivity<CommonListBinding> {

    private JfAdapter mAdapter;
    private int mPage = HttpUtils.start_page_java;
    private WalletModel mModel;
    private int mType; //0：购物积分，1：代金券，2：生态分

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_list);
    }

    @Override
    protected void initSetting() {

        setTitle("钱包明细");
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
            switch (mType) {
                case 0:
                    setTitle("积分明细");
                    break;
                case 1:
                    setTitle("代金券明细");
                    break;
            }
        }
        mAdapter = new JfAdapter();
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
        mModel.getMemberPointsLogList(getActivity(), BusinessUtils.getBindAccountId(), mType, mPage, HttpUtils.per_page_20,
                new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        showContentView();
                        List<GetMemberPointsLogList> data = (List<GetMemberPointsLogList>) object;
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

    public static void goPage(Context context, int type) {
        Intent intent = new Intent(context, JfDetailActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }
}
