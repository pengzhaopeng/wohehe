package com.messoft.gaoqin.wanyiyuan.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.RegexUtil;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityReSetPwdBinding;

public class ResetPwdActivity extends BaseActivity<ActivityReSetPwdBinding> {

    private int mType; //0-忘记登录密码过来重置
    private String mMobile;
    private String mCode;

    private LoginModel mLoginModel;

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_set_pwd);
    }

    @Override
    protected void initSetting() {
        showContentView();
        mLoginModel = new LoginModel();
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
            mMobile = getIntent().getStringExtra("mobile");
            mCode = getIntent().getStringExtra("code");
        }
    }

    @Override
    protected void initListener() {
        bindingView.ivBack.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
        bindingView.tvNext.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                doNext();
            }
        });
    }

    private void doNext() {
        String etPsw = bindingView.etPwd.getText().toString().trim();
        String etPswSure = bindingView.etPwd1.getText().toString().trim();
        if (!StringUtils.isNoEmpty(etPsw)) {
            ToastUtil.showShortToast("新密码不能为空");
            return;
        }
        if (!StringUtils.isNoEmpty(etPswSure)) {
            ToastUtil.showShortToast("请确认密码");
            return;
        }
        if (!etPsw.equals(etPswSure)) {
            ToastUtil.showShortToast("两次密码输入不一致");
            return;
        }
        if (!RegexUtil.isPassword(etPsw) || !RegexUtil.isPassword(etPswSure)) {
            ToastUtil.showShortToast("请设置规范的密码（6-18位包含字母和数字）");
            return;
        }
        if (!StringUtils.isNoEmpty(mMobile) || !StringUtils.isNoEmpty(mCode)) {
            finish();
            ToastUtil.showShortToast("操作错误，请重试");
            return;
        }
        mLoginModel.resetPassword(getActivity(), mMobile, mCode, etPsw,etPswSure, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                ToastUtil.showShortToast("重置密码成功");
                finish();
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    public static void goPage(Context context, String mobile, String code, int type) {
        Intent intent = new Intent(context, ResetPwdActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("mobile", mobile);
        intent.putExtra("code", code);
        context.startActivity(intent);
    }
}
