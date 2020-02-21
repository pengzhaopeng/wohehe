package com.messoft.gaoqin.wanyiyuan.ui;

import android.support.v7.widget.LinearLayoutManager;

import com.messoft.gaoqin.wanyiyuan.adapter.TestAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseFragment;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.RecycleViewDivider;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonListBinding;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestFragment extends BaseFragment<CommonListBinding> {

    private TestAdapter mTestAdapter;

    @Override
    protected void initSetting() {
        showContentView();

        mTestAdapter = new TestAdapter();
//        initOnRefresh(getActivity(), bindingView.rc);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bindingView.rc.setLayoutManager(mLayoutManager);

        bindingView.rc.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL,
                DensityUtils.dp2px(Objects.requireNonNull(getActivity()), 6),
                getResources().getColor(R.color.colorPageBg)));
        bindingView.rc.setAdapter(mTestAdapter);

        List<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add("狗子" + i);
        }
        mTestAdapter.addAll(data);
        mTestAdapter.notifyDataSetChanged();
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
                        List<String> data = new ArrayList<>();
                        for (int i = 0; i < 10; i++) {
                            data.add("屠夫" + i);
                        }
                        mTestAdapter.addAll(data);
                        mTestAdapter.notifyDataSetChanged();

                        refreshlayout.finishLoadmore();
                    }
                }, 300);

            }
        });
    }

    @Override
    protected int setContent() {
        return R.layout.common_list;
    }
}
