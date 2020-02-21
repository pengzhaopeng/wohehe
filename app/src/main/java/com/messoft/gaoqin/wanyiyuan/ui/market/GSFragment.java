package com.messoft.gaoqin.wanyiyuan.ui.market;

import android.support.v7.widget.LinearLayoutManager;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.GSAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseFragment;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.bean.BeanOrderInfoList;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonListBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.GoldModel;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.RecycleViewDivider;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

import java.util.List;
import java.util.Objects;

/**
 * 金币挂售
 */
public class GSFragment extends BaseFragment<CommonListBinding> {
    // 初始化完成后加载数据
    private boolean mIsPrepared = false;
    // 第一次显示时加载数据，第二次不显示
    private boolean mIsFirst = true;

    private int mPage = HttpUtils.start_page_java;
    private GSAdapter mGSAdapter;
    private GoldModel mGoldModel;

    String account = null;
    String beanAmountBegin = null;
    String beanAmountEnd = null;
    private RxBus mRxBus;

    @Override
    protected int setContent() {
        return R.layout.common_list;
    }

    @Override
    protected void initSetting() {
        showContentView();
        initRxBus();
        //手机号就是账号
//        account = BusinessUtils.getMobile();
        mGoldModel = new GoldModel();
        mGSAdapter = new GSAdapter();
//        initOnRefresh(getActivity(), bindingView.rc);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bindingView.rc.setLayoutManager(mLayoutManager);

        bindingView.rc.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL,
                DensityUtils.dp2px(Objects.requireNonNull(getActivity()), 0),
                getResources().getColor(R.color.colorPageBg)));
        bindingView.rc.setAdapter(mGSAdapter);
        // 准备就绪
        mIsPrepared = true;
        loadData();
    }

    @Override
    protected void initListener() {
        bindingView.refreshLayout.setEnableRefresh(false);
//        bindingView.refreshLayout.setEnableRefresh(false);

        bindingView.refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mPage++;
                        requestData();
                        refreshlayout.finishLoadmore();
                    }
                }, 300);

            }
        });

        mGSAdapter.setOnItemClickListener(new OnItemClickListener<BeanOrderInfoList>() {
            @Override
            public void onClick(BeanOrderInfoList beanOrderInfoList, int position) {
                if (beanOrderInfoList != null) {
                    GoldInfoActivity.goPage(getActivity(), beanOrderInfoList.getId());
                }
            }
        });
    }

    @Override
    protected void loadData() {
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }
        requestData();
    }

    private void requestData() {
        mGoldModel.getBeanOrderInfoList(getBaseActivity(), account, "0", beanAmountBegin, beanAmountEnd,
                mPage, HttpUtils.per_page_20, new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        showContentView();
                        mIsFirst = false;
                        List<BeanOrderInfoList> data = (List<BeanOrderInfoList>) object;
                        if (mPage == HttpUtils.start_page_java) {
                            if (data.size() > 0) {
                                mGSAdapter.clear();
                                mGSAdapter.addAll(data);
                                mGSAdapter.notifyDataSetChanged();
                            } else {
                                mGSAdapter.clear();
                                mGSAdapter.notifyDataSetChanged();
                                showNoData("暂无转让单");
                            }
                        } else {
                            mGSAdapter.addAll(data);
                            mGSAdapter.notifyDataSetChanged();
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

    public void setArgument(String phone, String beanAmountBegin, String beanAmountEnd) {
        this.account = phone;
        this.beanAmountBegin = beanAmountBegin;
        this.beanAmountEnd = beanAmountEnd;
        requestData();
    }

    /**
     * 注册RxBus
     */
    private void initRxBus() {
        mRxBus = RxBus.getInstanceBus();
        mRxBus.registerRxBus(mRxBus, RxBusMessage.class, rxBusMessage -> {
            //根据事件类型进行处理
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_GS_ORDER) {
                if (rxBusMessage.getI() == 0) {
                    //更新数据
                    requestData();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRxBus != null) {
            mRxBus.unSubscribe(this);
        }
    }
}
