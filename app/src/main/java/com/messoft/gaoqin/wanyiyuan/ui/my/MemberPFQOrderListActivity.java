package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.messoft.gaoqin.wanyiyuan.adapter.PFQOrderAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.OrderItemList;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.OrderModel;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.RecycleViewDivider;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonListBinding;

import java.util.List;
import java.util.Objects;

/**
 * 团队成员 零售区/批发区消费 订单
 */
public class MemberPFQOrderListActivity extends BaseActivity<CommonListBinding> {

    private String mMemberId;

    private PFQOrderAdapter mAdapter;
    private int mPage = HttpUtils.start_page_java;
    private OrderModel mModel;
    private String lsq = null;
    private String classCode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_list);

    }

    @Override
    protected void initSetting() {
        if (getIntent() != null) {
            lsq = getIntent().getStringExtra("lsq");
            classCode = getIntent().getStringExtra("classCode");
            mMemberId = getIntent().getStringExtra("memberId");
        }
        if (lsq != null) {
//            String title = (lsq.equals("lsq")) ? "零售区消费" : "批发区消费";
            setTitle("零售区消费");
        }else{
            setTitle("批发区消费");
        }

        mAdapter = new PFQOrderAdapter();
        mModel = new OrderModel();
        initOnRefresh(getActivity(), bindingView.rc);
        bindingView.rc.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL,
                DensityUtils.dp2px(Objects.requireNonNull(getActivity()), 6),
                getResources().getColor(R.color.colorPageBg)));
        bindingView.rc.setAdapter(mAdapter);

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
        mModel.getOrderItemList(getActivity(), mMemberId, lsq, classCode,null,"1,2,4,5",null, mPage, HttpUtils.per_page_20,
                new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        showContentView();
                        List<OrderItemList> data = (List<OrderItemList>) object;
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

    public static void goPage(Context context, String memberId, String lsq,String classCode) {
        Intent intent = new Intent(context, MemberPFQOrderListActivity.class);
        intent.putExtra("memberId", memberId);
        intent.putExtra("lsq", lsq);
        intent.putExtra("classCode", classCode);
        context.startActivity(intent);
    }
}
