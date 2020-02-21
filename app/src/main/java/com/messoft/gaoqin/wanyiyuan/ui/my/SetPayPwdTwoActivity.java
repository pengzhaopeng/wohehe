package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivitySetPayPwdTwoBinding;


/**
 * 设置支付密码第一步
 */
public class SetPayPwdTwoActivity extends BaseActivity<ActivitySetPayPwdTwoBinding> {

    private LoginModel mLoginModel;
    private int mType; //0- 首次设置支付密码  1-忘记密码过来重置支付密码 3-修改支付密码
    private String mPayPwd1;
    private String mPayPwdOld;
    private String mLoginPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pay_pwd_two);
    }

    @Override
    protected void initSetting() {
        showContentView();
        setTitle("设置新支付密码");
        mLoginModel = new LoginModel();
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
            mPayPwd1 = getIntent().getStringExtra("payPwd1");
            if (mType == 2) {  //修改支付密码 要获取旧密码
                mPayPwdOld = getIntent().getStringExtra("oldPayPwd");
            }
            if (mType == 1) {  //忘记密码过来重置支付密码 要获取登陆密码
                mLoginPwd = getIntent().getStringExtra("loginPwd");
            }
        }
    }

    @Override
    protected void initListener() {
        //提交
        bindingView.tvNext.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                doNext();
            }
        });
    }

    private void doNext() {
        String pwd2 = bindingView.input.getPwd();

        if (mPayPwd1.length() < 6 || pwd2.length() < 6) {
            ToastUtil.showShortToast("密码输入有误");
            return;
        }
        if (!mPayPwd1.equals(pwd2)) {
            ToastUtil.showShortToast("两次密码不一致");
            return;
        }

        //0- 首次设置支付密码  1-忘记密码过来重置支付密码 3-修改支付密码
        if (mType == 0) {
            mLoginModel.modifyPayPassword(getActivity(), null, mPayPwd1, pwd2, new RequestImpl() {
                @Override
                public void loadSuccess(Object object) {
                    ToastUtil.showShortToast("成功设置支付密码~");
                    //刷新下对应的界面
                    RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_FIRST_SET_PAY_PWD, 0));
                    finish();
                }

                @Override
                public void loadFailed(int errorCode, String errorMessage) {
                    ToastUtil.showShortToast(errorMessage);
                }
            });
        } else if (mType == 1) {
            mLoginModel.resetPayPassword(getActivity(), mLoginPwd, mPayPwd1, pwd2, new RequestImpl() {
                @Override
                public void loadSuccess(Object object) {
                    ToastUtil.showShortToast("重置支付密码成功~");
                    finish();
                }

                @Override
                public void loadFailed(int errorCode, String errorMessage) {
                    ToastUtil.showShortToast(errorMessage);
                }
            });

        } else if (mType == 2) {
            mLoginModel.modifyPayPassword(getActivity(), mPayPwdOld, mPayPwd1, pwd2, new RequestImpl() {
                @Override
                public void loadSuccess(Object object) {
                    ToastUtil.showShortToast("修改支付密码成功~");
                    finish();
                }

                @Override
                public void loadFailed(int errorCode, String errorMessage) {
                    ToastUtil.showShortToast(errorMessage);
                }
            });
        }

    }

    public static void goPage(Context context, int type, String payPwd1, String oldPayPwd, String loginPwd) {
        Intent intent = new Intent(context, SetPayPwdTwoActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("payPwd1", payPwd1);
        intent.putExtra("oldPayPwd", oldPayPwd);
        intent.putExtra("loginPwd", loginPwd);
        context.startActivity(intent);
    }
}
