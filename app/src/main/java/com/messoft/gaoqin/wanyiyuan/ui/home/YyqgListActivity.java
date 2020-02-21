package com.messoft.gaoqin.wanyiyuan.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.QgListAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.bean.GetRushGoodsTypeList;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityYyqgListBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.QgModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 预约抢购列表
 */
public class YyqgListActivity extends BaseActivity<ActivityYyqgListBinding> {

    private QgModel mQgModel;
    private QgListAdapter mAdapter;
    private int mPage = HttpUtils.start_page_java;
    private RxBus mRxBus;

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yyqg_list);
    }

    @Override
    protected void initSetting() {
        showContentView();
        initRxBus();
        if (getActivity() != null && getActivity().getWindow() != null) {
            StatusBarUtil.setColor(getActivity(), Color.parseColor("#AD64FF"), 0);
        }
        mAdapter = new QgListAdapter(getActivity());
        mQgModel = new QgModel();
        initOnRefresh(getActivity(), bindingView.rc);
        bindingView.rc.setAdapter(mAdapter);

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
        mAdapter.setOnItemClickListener(new OnItemClickListener<GetRushGoodsTypeList>() {
            @Override
            public void onClick(GetRushGoodsTypeList getRushGoodsTypeList, int position) {
                if (getRushGoodsTypeList != null && getRushGoodsTypeList.getGoodsTypeId() != null) {
                    QgInfoActivity.goPage(getActivity(), getRushGoodsTypeList.getGoodsTypeId());
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        ArrayList arr = new ArrayList();
        arr.add(0);
        arr.add(2);
//        arr.add(4);
//        arr.add(6);
//        arr.add(8);
//        arr.add(10);
//        arr.add(12);
//        arr.add(14);
        mQgModel.getRushGoodsTypeList(getActivity(), BusinessUtils.getBindAccountId(), arr, mPage, HttpUtils.per_page,
                new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        showContentView();
                        List<GetRushGoodsTypeList> data = (List<GetRushGoodsTypeList>) object;
                        if (mPage == HttpUtils.start_page_java) {
                            if (data.size() > 0) {
                                mAdapter.clear();
                                mAdapter.addAll(data);
                                mAdapter.notifyDataSetChanged();
                            } else {
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
                    }
                });
    }

    /**
     * 注册RxBus
     */
    private void initRxBus() {
        mRxBus = RxBus.getInstanceBus();
        mRxBus.registerRxBus(mRxBus, RxBusMessage.class, rxBusMessage -> {
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_QG_LIST) {
                if (rxBusMessage.getI() == 0) {
                    //刷新数据
                    loadData();
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
