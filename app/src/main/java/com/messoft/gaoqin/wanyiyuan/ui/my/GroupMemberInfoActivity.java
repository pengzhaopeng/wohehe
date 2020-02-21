package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.AgentMemberInfoDetail;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityGroupMemberInfoBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.GroupMemberModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.utils.UIUtils;
import com.messoft.gaoqin.wanyiyuan.utils.dialog.HttpDialogUtils;


public class GroupMemberInfoActivity extends BaseActivity<ActivityGroupMemberInfoBinding> {

    private String mType; //0-团队成员  1-代理详情
    private String mId;
    private GroupMemberModel mModel;
    private String mName;
    private String mRecommenderMobile;
    private String mAccount;

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_info);
    }

    @Override
    protected void initSetting() {
        showContentView();
        mModel = new GroupMemberModel();
        if (getIntent() != null) {
            mType = getIntent().getStringExtra("type");
            mId = getIntent().getStringExtra("id");
            mAccount = getIntent().getStringExtra("account");

            if (mType.equals("1")) {
                bindingView.rlGlq.setVisibility(View.VISIBLE);
            }
        }
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
        //零售区
        bindingView.rlLsq.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                MemberPFQOrderListActivity.goPage(getActivity(), mId,"lsq", null);
            }
        });
        //批发区
        bindingView.rlPfq.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                MemberPFQOrderListActivity.goPage(getActivity(),mId, null, "pfq");
            }
        });
        //管理服务费
        bindingView.rlGlq.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                //类型：0.商品销售 1.管理费
                XfDetailActivity.goPage(getActivity(), mId,"1");
            }
        });
        bindingView.tvNext.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
//                ActivityManage.finishActivity(MenberInfoActivity.class);
                HttpDialogUtils.showDialog(getActivity(), false, "加载中...");
                //清除缓存
                UIUtils.postTaskDelay(() -> {
                    MenberInfoActivity.goPage(getActivity(),mRecommenderMobile , mName,mType+"");
                    HttpDialogUtils.dismissDialog();
                }, 1000);
//                MenberInfoActivity.goPage(getActivity(), mId, mName);

            }
        });
    }

    private void loadData() {
        mModel.getAgentMemberInfoDetail(getActivity(), mId, new RequestImpl() {

            @SuppressLint("SetTextI18n")
            @Override
            public void loadSuccess(Object object) {
                AgentMemberInfoDetail data = (AgentMemberInfoDetail) object;
                ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getImgName()),bindingView.iv,0);
                mName = data.getName();
                bindingView.tvName.setText(mName);
                mRecommenderMobile = data.getAccount();
                bindingView.tvPhone.setText(mRecommenderMobile);
                bindingView.tvLevel.setText(BusinessUtils.memberLevel(data.getRoleId()));
                bindingView.tvTime.setText(data.getRegDays()+"天");
                bindingView.tvBuyMoney.setText(data.getConsumeAmountTotal()+"元");
                bindingView.tvTzMoney.setText(data.getRechargeAmountTotal()+"元");

                bindingView.tvNext.setText("查看团队成员："+data.getMemberNum()+"人");
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    public static void goPage(Context context, String type, String id, String account) {
        Intent intent = new Intent(context, GroupMemberInfoActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
        intent.putExtra("account", account);
        context.startActivity(intent);
    }
}
