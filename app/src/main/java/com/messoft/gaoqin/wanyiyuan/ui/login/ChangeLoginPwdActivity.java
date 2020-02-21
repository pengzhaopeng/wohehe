package com.messoft.gaoqin.wanyiyuan.ui.login;

import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityChangeLoginPwdBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.RegexUtil;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;


public class ChangeLoginPwdActivity extends BaseActivity<ActivityChangeLoginPwdBinding> {

    private LoginModel mLoginModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_login_pwd);
    }

    @Override
    protected void initSetting() {
        setTitle("修改登录密码");
        showContentView();

        mLoginModel = new LoginModel();
    }

    @Override
    protected void initListener() {
        bindingView.tvNext.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                doNext();
            }
        });
    }
    private void doNext() {
        String etPswOld = bindingView.etPwdOld.getText().toString().trim();
        String etPsw = bindingView.etPwd.getText().toString().trim();
        String etPswSure = bindingView.etPwd1.getText().toString().trim();
        if (!StringUtils.isNoEmpty(etPsw)) {
            ToastUtil.showShortToast("原登录密码不能为空");
            return;
        }
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

        mLoginModel.updatePassword(getActivity(), etPswOld, etPsw, etPswSure, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                ToastUtil.showShortToast("修改登录密码成功");
                finish();
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

}
