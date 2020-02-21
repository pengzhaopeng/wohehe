package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.ExistPayPassword;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.ui.login.ChangeLoginPwdActivity;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityChangePwdTypeBinding;


public class ChangePwdTypeActivity extends BaseActivity<ActivityChangePwdTypeBinding> {

    private LoginModel mLoginModel;

    private String mIsExist = "-1";//1-代表已设置

    private RxBus mRxBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd_type);
    }

    @Override
    protected void initSetting() {
        setTitle("修改密码");
        showContentView();
        initRxBus();
        mLoginModel = new LoginModel();
        //检查是否设置了支付密码
        checkIsSetPayPwd();
    }

    @Override
    protected void initListener() {
        bindingView.cwDlmm.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), ChangeLoginPwdActivity.class);
            }
        });
        bindingView.cwZfmm.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (mIsExist.equals("1")) {
                    //已设置 修改
                    SysUtils.startActivity(getActivity(),InputOldPayPwdActivity.class);
                }else{
                    //设置新的
                    SetPayPwdOneActivity.goPage(getActivity(),0,null,null);
                }
            }
        });
    }

    private void checkIsSetPayPwd() {
        mLoginModel.existPayPassword(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                ExistPayPassword bean = (ExistPayPassword) object;
                mIsExist = bean.getIsExist();
                if (mIsExist.equals("1")) {
                    //已设置
                    bindingView.cwZfmm.setTvLeftStr("修改支付密码");
                }else{
                    bindingView.cwZfmm.setTvLeftStr("设置支付密码");
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    /**
     * 注册RxBus
     */
    private void initRxBus() {
        mRxBus = RxBus.getInstanceBus();
        mRxBus.registerRxBus(mRxBus, RxBusMessage.class, rxBusMessage -> {
            //根据事件类型进行处理
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_FIRST_SET_PAY_PWD) {
                if (rxBusMessage.getI() == 0) {
                    //刷新列表
                    checkIsSetPayPwd();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRxBus != null) {
            mRxBus.unSubscribe(this);
        }
    }
}
