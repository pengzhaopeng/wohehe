package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivitySetPayPwdOneBinding;
import com.messoft.gaoqin.wanyiyuan.view.PwdInput;


/**
 * 设置支付密码第一步
 */
public class SetPayPwdOneActivity extends BaseActivity<ActivitySetPayPwdOneBinding> {

    private int mType; //0- 首次设置支付密码  1-忘记密码过来重置支付密码 2-修改支付密码
    private String mOldPayPwd;
    private String mLoginPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pay_pwd_one);
    }

    @Override
    protected void initSetting() {
        showContentView();
        setTitle("设置新支付密码");
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
            if (mType == 2) {  //修改支付密码 要获取旧密码
                mOldPayPwd = getIntent().getStringExtra("oldPayPwd");
            }
            if (mType == 1) {  //忘记密码过来重置支付密码 要获取登陆密码
                mLoginPwd = getIntent().getStringExtra("loginPwd");
            }
        }
    }

    @Override
    protected void initListener() {
        bindingView.input.setOnInputFinished(new PwdInput.OnInputFinished() {
            @Override
            public void onFinished(String pwd) {
                if (pwd != null && pwd.length() == 6) {
                    if (mType == 0) {
                        SetPayPwdTwoActivity.goPage(getActivity(), mType, pwd, null, null);
                    }else if (mType == 1) {
                        SetPayPwdTwoActivity.goPage(getActivity(), mType, pwd, null, mLoginPwd);
                    }if (mType == 2) {
                        SetPayPwdTwoActivity.goPage(getActivity(), mType, pwd, mOldPayPwd, null);
                    }

                    finish();
                }
            }
        });
    }

    public static void goPage(Context context, int type,String oldPayPwd,String loginPwd) {
        Intent intent = new Intent(context, SetPayPwdOneActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("oldPayPwd", oldPayPwd);
        intent.putExtra("loginPwd", loginPwd);
        context.startActivity(intent);
    }
}
