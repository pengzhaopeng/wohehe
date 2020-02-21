package com.messoft.gaoqin.wanyiyuan.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.SendSms;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityForgetPwdBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.RegexUtil;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TimeCount;
import com.messoft.gaoqin.wanyiyuan.utils.TimeListener;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;


public class ForgetPwdActivity extends BaseActivity<ActivityForgetPwdBinding> {

    private TimeCount mTimeCount;
    private LoginModel mLoginModel;
    private int mType; //0-忘记密码
    private String mCode;

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
    }

    @Override
    protected void initSetting() {
        showContentView();
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
            switch (mType) {
                case 0:
                    setTitle("忘记密码");
                    break;
                case 1:
                    setTitle("验证手机号");
                    bindingView.etPhone.setText(BusinessUtils.getMobile());
                    bindingView.etPhone.setEnabled(false);
                    bindingView.etPhone.setClickable(false);
                    break;
                case 2:
                    setTitle("验证手机号");
                    bindingView.etPhone.setText(BusinessUtils.getMobile());
                    bindingView.etPhone.setEnabled(false);
                    bindingView.etPhone.setClickable(false);
                    break;
            }
        }

        mLoginModel = new LoginModel();

        //计时器
        mTimeCount = new TimeCount(60000, 1000, new TimeListener() {
            @Override
            public void timeFinish() {
                if (!isFinishing()) {
                    bindingView.tvGetCode.setText("重新获取");
                    bindingView.tvGetCode.setClickable(true);
                    mTimeCount.cancel();
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {
                //计时过程显示
                if (!isFinishing()) {
                    bindingView.tvGetCode.setClickable(false);
                    bindingView.tvGetCode.setText(millisUntilFinished / 1000 + "秒");
                }
                if (isFinishing()) {
                    mTimeCount.cancel();
                }
            }
        });
    }

    @Override
    protected void initListener() {
        bindingView.ivBack.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });

        //获取验证码
        bindingView.tvGetCode.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                getCode();
            }
        });
        //下一步
        bindingView.tvNext.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                doNext();
            }
        });
    }

    private void getCode() {
        final String etPhone = bindingView.etPhone.getText().toString().trim();
        if (!StringUtils.isNoEmpty(etPhone)) {
            ToastUtil.showShortToast("手机号码不能为空");
            return;
        }
        if (!RegexUtil.checkMobile(etPhone)) {
            ToastUtil.showShortToast("手机号码格式不正确");
            return;
        }
        mLoginModel.sendSms(getActivity(), etPhone, 4, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                SendSms bean = (SendSms) object;
                ToastUtil.showShortToast("验证码已发送到您的手机");
                mTimeCount.start();
                bindingView.etPhone.clearFocus();
                bindingView.etCode.requestFocus();
                mCode = bean.getCode();
                bindingView.etCode.setText(bean.getCode());
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    private void doNext() {
        final String etPhone = bindingView.etPhone.getText().toString().trim();
        final String etCode = bindingView.etCode.getText().toString().trim();
        if (!StringUtils.isNoEmpty(etPhone)) {
            ToastUtil.showShortToast("手机号码不能为空");
            return;
        }
        if (!RegexUtil.checkMobile(etPhone)) {
            ToastUtil.showShortToast("手机号码格式不正确");
            return;
        }
        if (!StringUtils.isNoEmpty(etCode)) {
            ToastUtil.showShortToast("验证码不能为空");
            return;
        }
        if (!etCode.equals(mCode)) {
            ToastUtil.showShortToast("验证码错误");
            return;
        }

        switch (mType) {
            case 0:
                //跳转重置密码
                ResetPwdActivity.goPage(getActivity(),etPhone,etCode,0);
                break;
        }

        finish();
    }

    public static void goPage(Context context, int type) {
        Intent intent = new Intent(context, ForgetPwdActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }
}
