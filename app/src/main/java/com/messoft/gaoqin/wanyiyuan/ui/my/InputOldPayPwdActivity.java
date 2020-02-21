package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.PwdInput;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityInputOldPayPwdBinding;


/**
 * 输入原支付密码
 */
public class InputOldPayPwdActivity extends BaseActivity<ActivityInputOldPayPwdBinding> {

    private LoginModel mLoginModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_old_pay_pwd);
    }

    @Override
    protected void initSetting() {
        setTitle("身份验证");
        showContentView();
        mLoginModel = new LoginModel();
    }

    @Override
    protected void initListener() {
        bindingView.input.setOnInputFinished(new PwdInput.OnInputFinished() {
            @Override
            public void onFinished(String pwd) {
                if (pwd != null && pwd.length() == 6) {
                    //验证支付密码
                    mLoginModel.validatePayPassword(getActivity(), pwd, new RequestImpl() {
                        @Override
                        public void loadSuccess(Object object) {
                            SetPayPwdOneActivity.goPage(getActivity(),2,pwd,null);
                            finish();
                        }

                        @Override
                        public void loadFailed(int errorCode, String errorMessage) {
                            ToastUtil.showShortToast(errorMessage);
                        }
                    });

                }
            }
        });

        bindingView.tvForget.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(),InputLoginPwdActivity.class);
                finish();
            }
        });
    }
}
