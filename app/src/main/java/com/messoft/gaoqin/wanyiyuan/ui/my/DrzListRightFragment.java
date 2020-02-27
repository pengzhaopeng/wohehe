package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.DrzListRightAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseFragment;
import com.messoft.gaoqin.wanyiyuan.bean.MemberGainsWaitLogList;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonListBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.WalletModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.RecycleViewDivider;

import java.util.List;
import java.util.Objects;

/**
 * 待入账左边列表
 */
public class DrzListRightFragment extends BaseFragment<CommonListBinding> {

    // 初始化完成后加载数据
    private boolean mIsPrepared = false;
    // 第一次显示时加载数据，第二次不显示
    private boolean mIsFirst = true;

    private int mPage = HttpUtils.start_page_java;
    private DrzListRightAdapter mAdapter;
    private String beginTime = null; //用来提交后台 需要时分秒 补0
    private String endTime = null;

    private WalletModel mModel;


    public static DrzListRightFragment newInstance(String beginTime, String endTime) {
        DrzListRightFragment fragment = new DrzListRightFragment();
        Bundle args = new Bundle();
        args.putString("beginTime", beginTime);
        args.putString("endTime", endTime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setContent() {
        return R.layout.common_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            beginTime = getArguments().getString("beginTime");
            endTime = getArguments().getString("endTime");

        }
    }

    @Override
    protected void initSetting() {
//        showContentView();
        mModel = new WalletModel();
        mAdapter = new DrzListRightAdapter();
        initOnRefresh(getActivity(), bindingView.rc);

        bindingView.rc.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL,
                DensityUtils.dp2px(Objects.requireNonNull(getActivity()), 6),
                getResources().getColor(R.color.colorPageBg)));
        bindingView.rc.setAdapter(mAdapter);

        // 准备就绪
        mIsPrepared = true;
        loadData();
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

//        mAdapter.setOnItemClickListener(new OnItemClickListener<MemberCapitalWaitLogList>() {
//            @Override
//            public void onClick(MemberCapitalWaitLogList memberCapitalWaitLogList, int position) {
//                if (memberCapitalWaitLogList != null && memberCapitalWaitLogList.getType().equals("0")) {
//                    DaiRuZhangInfoActivit.goPage(getActivity(), memberCapitalWaitLogList);
//                }
//            }
//        });
    }

    @Override
    protected void loadData() {
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }
        requestData();
    }

    private void requestData() {
        mModel.getMemberGainsWaitLogList(getBaseActivity(), BusinessUtils.getBindAccountId(), "0","0", beginTime, endTime, mPage, HttpUtils.per_page_20, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                mIsFirst = false;
                showContentView();
                List<MemberGainsWaitLogList> data = (List<MemberGainsWaitLogList>) object;
                if (mPage == HttpUtils.start_page_java) {
                    if (data != null && data.size() > 0) {
                        mAdapter.clear();
                        mAdapter.addAll(data);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter.clear();
                        mAdapter.notifyDataSetChanged();
                        showNoData("未查询到明细数据");
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

    public void setBeginAndEndTime(String beginTime, String endTime) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        requestData();
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
        mPage = HttpUtils.start_page_java;
        requestData();
    }


}
