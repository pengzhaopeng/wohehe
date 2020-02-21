package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.MemberListAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.bean.MemberInfoList;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityMenberInfoBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.GroupMemberModel;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.RecycleViewDivider;

import java.util.List;

/**
 * 团队成员
 *
 * @author 鹏鹏鹏先森
 * created at 2019/11/7 15:45
 */
public class MenberInfoActivity extends BaseActivity<ActivityMenberInfoBinding> {

    private String recommenderMobile;
    private int mPage = HttpUtils.start_page_java;
    private MemberListAdapter mAdapter;
    private GroupMemberModel mModel;
    private String mPageType; //(0.我的团队，1.我的代理)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menber_info);
    }

    @Override
    protected void initSetting() {
        showContentView();
        mModel = new GroupMemberModel();
        if (getIntent() != null) {
            recommenderMobile = getIntent().getStringExtra("recommenderMobile");
            String memberName = getIntent().getStringExtra("memberName");
            mPageType = getIntent().getStringExtra("pageType");
            String t1 = mPageType.endsWith("0")?"团队成员":"代理";
            setTitle(memberName + t1);
        }
        mAdapter = new MemberListAdapter();
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
        mAdapter.setOnItemClickListener(new OnItemClickListener<MemberInfoList>() {
            @Override
            public void onClick(MemberInfoList memberInfoList, int position) {
                GroupMemberInfoActivity.goPage(getActivity(), mPageType, memberInfoList.getId(),memberInfoList.getAccount());
            }
        });
    }

    private void loadData() {
        mModel.getMemberInfoList(getActivity(), recommenderMobile,mPageType, mPage, HttpUtils.per_page_20, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                List<MemberInfoList> data = (List<MemberInfoList>) object;
                if (mPage == HttpUtils.start_page_java) {
                    if (data != null && data.size() > 0) {
                        mAdapter.clear();
                        mAdapter.addAll(data);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter.clear();
                        mAdapter.notifyDataSetChanged();
                        ToastUtil.showLongToast("暂无团队成员~");
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

    public static void goPage(Context context, String recommenderMobile, String memberName,String pageType) {
        Intent intent = new Intent(context, MenberInfoActivity.class);
        intent.putExtra("recommenderMobile", recommenderMobile);
        intent.putExtra("memberName", memberName);
        intent.putExtra("pageType", pageType);
        context.startActivity(intent);
    }
}
