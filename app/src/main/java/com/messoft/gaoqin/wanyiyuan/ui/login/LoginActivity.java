package com.messoft.gaoqin.wanyiyuan.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.LoginBean;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityLoginBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.ui.MainActivity;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;


public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private LoginModel mLoginModel;

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    protected void initSetting() {
        showContentView();
        mLoginModel = new LoginModel();
    }

    @Override
    protected void initListener() {
        //注册
        bindingView.tvRegister.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), RegisterActivity.class);
            }
        });

        //忘记密码
        bindingView.tvForgetPwd.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                ForgetPwdActivity.goPage(getActivity(), 0);
            }
        });

        //登录
        bindingView.tvNext.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                login();
            }
        });
    }

    /**
     * 登录
     */
    private void login() {
        String etPhone = bindingView.etPhone.getText().toString().trim();
        String etPsw = bindingView.etPwd.getText().toString().trim();
        if (!StringUtils.isNoEmpty(etPhone)) {
            ToastUtil.showShortToast("手机号码不能为空");
            return;
        }
//        if (!RegexUtil.checkMobile(etPhone)) {
//            ToastUtil.showShortToast("手机号码格式不正确");
//            return;
//        }
        if (!StringUtils.isNoEmpty(etPsw)) {
            ToastUtil.showShortToast("密码不能为空");
            return;
        }
        mLoginModel.signIn(LoginActivity.this, etPhone, etPsw, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                LoginBean bean = (LoginBean) object;
                requestMemberInfo(bean);
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    /**
     * 获取用户信息
     *
     * @param bean
     */
    private void requestMemberInfo(LoginBean bean) {
        //保存信息
        BusinessUtils.setToken(bean.getAccessToken());
        BusinessUtils.setSecret(bean.getSecret());
        mLoginModel.getLoginMemberInfo(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                LoginMemberInfo bean = (LoginMemberInfo) object;
                BusinessUtils.saveUserData(bean);
                goPage(MainActivity.class);
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    private <T> void goPage(Class<T> cla) {
        startActivity(new Intent(getActivity(), cla));
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        finish();
    }
}
