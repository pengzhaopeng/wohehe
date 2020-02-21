package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.adapter.MyBillListAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.MemberBillList;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.WalletModel;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.RecycleViewDivider;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityMyBillInfoBinding;

import java.util.List;

/**
 * 个人账单详情列表
 */
public class MyBillInfoActivity extends BaseActivity<ActivityMyBillInfoBinding> {

    private int mType;
    private MyBillListAdapter mAdapter;
    private WalletModel mModel;
    private String beginTime = null;
    private String endTime = null;

    private int mPage = HttpUtils.start_page_java;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bill_info);

        setRightImg(R.drawable.rili);
    }

    @Override
    protected void initSetting() {
        showContentView();
        mModel = new WalletModel();
        bindingView.llMyBill.setVisibility(View.VISIBLE);
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
            String title = getIntent().getStringExtra("title");
            setTitle(title);
            String money = getIntent().getStringExtra("money");
            bindingView.tvTotalMoney.setText("总计：" + money);
        }
        mAdapter = new MyBillListAdapter();
        initOnRefresh(getActivity(), bindingView.rc);
//        bindingView.rcAddress.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        bindingView.rc.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL,
                DensityUtils.dp2px(getActivity(), 8),
                getResources().getColor(R.color.colorPageBg)));
        bindingView.rc.setAdapter(mAdapter);
        loadData();
    }

    @Override
    protected void initListener() {
        bindingView.refreshLayout.setEnableRefresh(false);
        bindingView.refreshLayout.setOnLoadmoreListener(refreshlayout -> refreshlayout.getLayout().postDelayed(() -> {
            mPage++;
            loadData();
            refreshlayout.finishLoadmore();
        }, 300));
    }

    private void loadData() {
        mModel.getMemberBillList(getActivity(), mType + "", beginTime, endTime, mPage, HttpUtils.per_page_20, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                List<MemberBillList> data = (List<MemberBillList>) object;
                if (mPage == HttpUtils.start_page_java) {
                    if (data != null && data.size() > 0) {
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

    public static void goPage(Context context, int type, String title, String money) {
        Intent intent = new Intent(context, MyBillInfoActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("title", title);
        intent.putExtra("money", money);
        context.startActivity(intent);
    }
}
