package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.OrderSHSqAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.bean.ReturnApplyList;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonListBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.OrderModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.RecycleViewDivider;

import java.util.List;
import java.util.Objects;

/**
 * 售后申请列表
 */
public class OrderShSqActivity extends BaseActivity<CommonListBinding> {

    private int mPage = HttpUtils.start_page_java;
    private OrderSHSqAdapter mAdapter;

    private OrderModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_list);
    }

    @Override
    protected void initSetting() {
        setTitle("申请列表");
        mModel = new OrderModel();
        mAdapter = new OrderSHSqAdapter(getActivity());
        initOnRefresh(getActivity(), bindingView.rc);

        bindingView.rc.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL,
                DensityUtils.dp2px(Objects.requireNonNull(getActivity()), 6),
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

        mAdapter.setOnItemClickListener(new OnItemClickListener<ReturnApplyList>() {
            @Override
            public void onClick(ReturnApplyList returnApplyList, int position) {
                if (returnApplyList != null) {
                    OrderShSqInfoActivity.goPage(getActivity(),returnApplyList);
                }
            }
        });
    }

    private void requestData() {
        mModel.getReturnApplyList(getActivity(), BusinessUtils.getBindAccountId(), null, null, "4", mPage, HttpUtils.per_page_20,
                new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        showContentView();
                        List<ReturnApplyList> data = (List<ReturnApplyList>) object;
                        if (mPage == HttpUtils.start_page_java) {
                            if (data.size() > 0) {
                                mAdapter.clear();
                                mAdapter.addAll(data);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.clear();
                                mAdapter.notifyDataSetChanged();
                                showNoData("暂无订单记录");
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
