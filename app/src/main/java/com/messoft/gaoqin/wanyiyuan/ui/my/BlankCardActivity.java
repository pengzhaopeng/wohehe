package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.adapter.BlankCardAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.bean.BlankCard;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.BlankCardModel;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityBlankCardBinding;

import java.util.List;

/**
 * 银行卡
 */
public class BlankCardActivity extends BaseActivity<ActivityBlankCardBinding> {

    private RxBus mRxBus;
    private BlankCardModel mModel;
    private List<BlankCard> mData;
    private BlankCardAdapter mAdapter;

    private boolean isEdit = false; //是否处于编辑状态，默认不是
    private int mType; //0-默认不用操作 1-过来选择银行卡

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_card);
    }

    @Override
    protected void initSetting() {
        setTitle("银行卡管理");
        showContentView();
        setRightText("管理");
        initRxBus();
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
        }
        mModel = new BlankCardModel();
        mAdapter = new BlankCardAdapter(getActivity());
        initOnRefresh(getActivity(), bindingView.rc);
        bindingView.rc.setAdapter(mAdapter);
        loadData();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void initListener() {
        getRightTv().setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (isEdit) {
                    getRightTv().setText("管理");
                    mAdapter.setIsEdit(false);
                    bindingView.tvAddCard.setVisibility(View.GONE);
                } else { //非管理状态，点击进入管理状态
                    getRightTv().setText("完成");
                    mAdapter.setIsEdit(true);
                    bindingView.tvAddCard.setVisibility(View.VISIBLE);
                }
                isEdit = !isEdit;
            }
        });

        bindingView.tvAddCard.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                EditBlankCardActivity.goPage(getActivity(), 0, null);
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener<BlankCard>() {
            @Override
            public void onClick(BlankCard blankCard, int position) {
                if (mType == 1 && blankCard != null) {
                    RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_WITHDRAW, blankCard));
                    finish();
                }
            }
        });
    }

    private void loadData() {
        mModel.getMemberBankList(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                showContentView();
                mData = (List<BlankCard>) object;
                if (mData.size() > 0) {
                    mAdapter.clear();
                    mAdapter.addAll(mData);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter.clear();
                    mAdapter.notifyDataSetChanged();
                    showNoData("暂无银行卡");
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
            //根据事件类型进行处理
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_BLANK_CARD) {
                if (rxBusMessage.getI() == 0) {
                    //刷新列表
                    if (mAdapter != null) {
                        mAdapter.setIsEdit(false);
                    }
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

    public static void goPage(Context context, int type) {
        Intent intent = new Intent(context, BlankCardActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }
}
