package com.messoft.gaoqin.wanyiyuan.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.LoginBean;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.bean.SendSms;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityRegisterBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.ui.MainActivity;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.RegexUtil;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TimeCount;
import com.messoft.gaoqin.wanyiyuan.utils.TimeListener;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;


/**
 * 注册
 */
public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {

    private TimeCount mTimeCount;
    private LoginModel mLoginModel;
    private boolean mIsCheck = false;

    private boolean showFirst; //首次勾选弹框
    private RxBus mRxBus;

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void initSetting() {
        showContentView();
        initRxBus();
        //首次勾选弹出用户协议
        showFirst = true;

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

            @SuppressLint("SetTextI18n")
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

        //用户协议
        String deal = String.format("<font style='font-size:14px'>" +
                "<font color=\"#999999\">我已阅读并同意</font>" +
                "<font color=\"#2F97F1\"> 《万益源用户协议》</font>" +
                "</font>");
        bindingView.tvDeal.setText(Html.fromHtml(deal));
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
        //注册
        bindingView.tvNext.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                register();
            }
        });
        //去登陆
        bindingView.llGoLogin.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
        //协议内容
        bindingView.tvDeal.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                CommonWebContentActivity.goPage(getActivity(), "1", 0);
            }
        });
//        bindingView.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                //首次进来并且勾选就弹出用户协议
//                if (showFirst) {
//                    //首次勾选不要勾，直接弹框，等用户同意再勾选
//                    CommonWebContentActivity.goPage(getActivity(),"1",0);
//                    bindingView.cb.setChecked(false);
//                }else{
//                    mIsCheck = b;
//                }
//                showFirst = false;
//            }
//        });

    }

    private void getCode() {
        String etPhone = bindingView.etPhone.getText().toString().trim();
        if (!StringUtils.isNoEmpty(etPhone)) {
            ToastUtil.showShortToast("手机号码不能为空");
            return;
        }
        if (!RegexUtil.checkMobile(etPhone)) {
            ToastUtil.showShortToast("手机号码格式不正确");
            return;
        }
        mLoginModel.sendSms(RegisterActivity.this, etPhone, 2, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                ToastUtil.showShortToast("验证码已发送到您的手机");
                SendSms bean = (SendSms) object;
                mTimeCount.start();
                bindingView.etPhone.clearFocus();
                bindingView.etCode.requestFocus();
                bindingView.etCode.setText(bean.getCode());
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    /**
     * 注册
     */
    private void register() {
        String etPhone = bindingView.etPhone.getText().toString().trim();
        String etCode = bindingView.etCode.getText().toString().trim();
        String etPsw = bindingView.etPwd.getText().toString().trim();
        String etTjr = bindingView.etTjr.getText().toString().trim();
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
        if (!StringUtils.isNoEmpty(etPsw)) {
            ToastUtil.showShortToast("密码不能为空");
            return;
        }
        if (!RegexUtil.isPassword(etPsw)) {
            ToastUtil.showShortToast("请设置规范的密码（6-18位包含字母和数字）");
            return;
        }
//        if (!mIsCheck) {
//            ToastUtil.showShortToast("认真阅读并同意《万益源用户协议》才能注册哦~");
//            return;
//        }
        if (!bindingView.cb.isChecked()) {
            ToastUtil.showShortToast("认真阅读并同意《万益源用户协议》才能注册哦~");
            return;
        }
        mLoginModel.register(RegisterActivity.this, etPhone, etPsw, etCode, etTjr, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                LoginBean bean = (LoginBean) object;
                requestMemberInfo(bean);
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {

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

    /**
     * 注册RxBus
     */
    private void initRxBus() {
        mRxBus = RxBus.getInstanceBus();
        mRxBus.registerRxBus(mRxBus, RxBusMessage.class, rxBusMessage -> {
            //根据事件类型进行处理
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_AGREE) {
                if (rxBusMessage.getI() == 0) {
                    //同意协议
                    bindingView.cb.setChecked(true);
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRxBus != null) {
            mRxBus.unSubscribe(this);
        }
        if (isFinishing()) {
            mTimeCount.cancel();
        }
    }
}
