package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.GetRushOrderList;
import com.messoft.gaoqin.wanyiyuan.bean.TransferMemberInfo;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityFindFriendBinding;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.RegexUtil;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

import org.json.JSONObject;

/**
 * 查找好友进行转增金豆
 */
public class FindFriendActivity extends BaseActivity<ActivityFindFriendBinding> {

    private int mType; //0-转账 1-订单转让
    private GetRushOrderList mOrderBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
    }

    @Override
    protected void initSetting() {
        showContentView();
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
            switch (mType){
                case 0:
                    setTitle("好友转赠");
                    break;
                case 1:
                    mOrderBean = (GetRushOrderList) getIntent().getSerializableExtra("bean");
                    setTitle("订单转让");
                    break;
            }
        }

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
        String trim = bindingView.et.getText().toString().trim();
        if (!StringUtils.isNoEmpty(trim)) {
            ToastUtil.showLongToast("请输入对方的手机号码");
            return;
        }
        if (!RegexUtil.checkMobile(trim)) {
            ToastUtil.showLongToast("请输入正确的手机号码");
            return;
        }
        if (BusinessUtils.getMobile().equals(trim)) {
            ToastUtil.showLongToast("不能转增给自己");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("account", trim);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getTransferMemberInfo(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(this.bindToLifecycle())
                .subscribe(new BaseObserver<TransferMemberInfo>(this, true, "加载中...") {
                    @Override
                    protected void onSuccess(TransferMemberInfo login) {
                        if (login != null) {
                            switch (mType){
                                case 0:
                                    ZZActivity.goPage(getActivity(), login);
                                    break;
                                case 1:
                                    ZrddActivity.goPage(getActivity(), mOrderBean,login);
                                    break;
                            }

                            finish();
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        ToastUtil.showLongToast(msg);
                    }
                });

    }

    public static void goPage(Context context, int type, GetRushOrderList bean) {
        Intent intent = new Intent(context, FindFriendActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("bean",bean);
        context.startActivity(intent);
    }
}
