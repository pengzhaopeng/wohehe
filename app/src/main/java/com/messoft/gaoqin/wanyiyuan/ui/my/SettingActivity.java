package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivitySettingBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.ui.login.CommonWebContentActivity;
import com.messoft.gaoqin.wanyiyuan.ui.login.LoginActivity;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.utils.UIUtils;
import com.messoft.gaoqin.wanyiyuan.utils.dialog.HttpDialogUtils;
import com.yzq.zxinglibrary.common.Constant;


public class SettingActivity extends BaseActivity<ActivitySettingBinding> {

    private LoginModel mLoginModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void initSetting() {
        showContentView();
        mLoginModel = new LoginModel();
        setTitle("设置");

        if (Constants.isOnline()){
            bindingView.btnChangeHost.setText("切换域名(当前是正式线)");
        }else{
            bindingView.btnChangeHost.setText("切换域名(当前是测试线)");
        }
    }

    @Override
    protected void initListener() {
        //个人资料
        bindingView.cwGgzl.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(),PersonInfoActivity.class);
            }
        });
        //隐私协议
        bindingView.cwYsxy.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                CommonWebContentActivity.goPage(getActivity(),"1",1);
            }
        });
        //地址管理
        bindingView.cwDdgl.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                AddressActivity.goPage(getActivity(),0);
            }
        });
        //银行卡
        bindingView.cwYhk.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                BlankCardActivity.goPage(getActivity(),0);
            }
        });
        //修改密码
        bindingView.cwXgmm.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(),ChangePwdTypeActivity.class);
            }
        });
        //清除缓存
        bindingView.cwQchc.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                HttpDialogUtils.showDialog(getActivity(), false, "清除中...");
                //清除缓存
                UIUtils.postTaskDelay(() -> {
                    ToastUtil.showShortToast("清除成功");
                    HttpDialogUtils.dismissDialog();
                }, 1500);
            }
        });
        //注销账号
        bindingView.cwZxzh.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                zs();
            }
        });
        //退出登录
        bindingView.btnPay.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(), null, "确定退出登录吗？", new DialogWithYesOrNoUtils.DialogCallBack() {
                    @Override
                    public void exectEvent(DialogInterface dialog) {
                        BusinessUtils.loginOut(getActivity(), LoginActivity.class);
                    }

                    @Override
                    public void exectCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
            }
        });

        //切换域名
        bindingView.btnChangeHost.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(), "切换域名", "切换域名会退出登录","正式","测试", new DialogWithYesOrNoUtils.DialogCallBack() {
                    @Override
                    public void exectEvent(DialogInterface dialog) {
                        dialog.dismiss();
                        if (Constants.isOnline()) {
                            ToastUtil.showShortToast("已经在正式域名下");
                            return;
                        }
                        //正式
                        BusinessUtils.setOnlineHost(true);
                        Constants.setHostOnline();
                        BusinessUtils.loginOut(getActivity(), LoginActivity.class);
                    }

                    @Override
                    public void exectCancel(DialogInterface dialog) {
                        dialog.dismiss();
                        if (!Constants.isOnline()) {
                            ToastUtil.showShortToast("已经在测试域名下");
                            return;
                        }
                        //测试
                        BusinessUtils.setOnlineHost(false);
                        Constants.setHostOffline();
                        BusinessUtils.loginOut(getActivity(), LoginActivity.class);
                    }
                });
            }
        });
    }

    /**
     * 注销用户
     */
    private void zs() {
        DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(), "温馨提示", "注销后将清空您所有数据且无法再登录，是否继续？","注销","我再想想", new DialogWithYesOrNoUtils.DialogCallBack() {
            @Override
            public void exectEvent(DialogInterface dialog) {
                mLoginModel.cancelAccount(getActivity(), new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        ToastUtil.showShortToast("账户已注销");
                        BusinessUtils.loginOut(getActivity(), LoginActivity.class);
                    }

                    @Override
                    public void loadFailed(int errorCode, String errorMessage) {
                        ToastUtil.showShortToast(errorMessage);
                    }
                });
            }

            @Override
            public void exectCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }
}
