package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityInputLoginPwdBinding;


/**
 * 输入登录密码
 */
public class InputLoginPwdActivity extends BaseActivity<ActivityInputLoginPwdBinding> {

    private LoginModel mLoginModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_login_pwd);
    }

    @Override
    protected void initSetting() {
        showContentView();
        setTitle("输入登录密码");
        mLoginModel = new LoginModel();
    }

    @Override
    protected void initListener() {
        bindingView.tvNext.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                String loginPwd = bindingView.et.getText().toString().trim();
                if (!StringUtils.isNoEmpty(loginPwd)) {
                    ToastUtil.showShortToast("请输入登录密码");
                    return;
                }
                mLoginModel.validatePassword(getActivity(), loginPwd, new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        SetPayPwdOneActivity.goPage(getActivity(),1,null,loginPwd);
                        finish();
                    }

                    @Override
                    public void loadFailed(int errorCode, String errorMessage) {
                        ToastUtil.showShortToast(errorMessage);
                    }
                });
            }
        });
    }
}
