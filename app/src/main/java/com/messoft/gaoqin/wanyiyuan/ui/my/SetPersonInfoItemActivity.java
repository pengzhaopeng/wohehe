package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivitySetCarNoBinding;

/**
 * 设置车牌号(通用的editText输入框 页面)
 *
 * @author 鹏鹏鹏先森
 * created at 2018/11/22 17:57
 */
public class SetPersonInfoItemActivity extends BaseActivity<ActivitySetCarNoBinding> {

    private int mType; //0-姓名 1-微信
    private String mMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_car_no);
    }

    @Override
    protected void initSetting() {
        setTitle("车牌号设置");
        showContentView();

        BusinessUtils.setRightTv(getRightTv(), "保存");

        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
            mMsg = getIntent().getStringExtra("msg");
            switch (mType) {
                case 0://姓名
                    setTitle("设置姓名");
                    bindingView.editText.setText(mMsg);
                    break;
                case 1://姓名
                    setTitle("设置微信号");
                    bindingView.editText.setText(mMsg);
                    break;
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initListener() {
        getRightTv().setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                save();
            }
        });
    }

    private void save() {
        String number = bindingView.editText.getText().toString().trim();
        switch (mType) {
            case 0: //姓名
                if (!StringUtils.isNoEmpty(number)) {
                    ToastUtil.showLongToast("请输入姓名");
                    return;
                }
                RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_PERSON_INFO, mType, number));
                finish();
                break;
            case 1: //微信号
                if (!StringUtils.isNoEmpty(number)) {
                    ToastUtil.showLongToast("请输入微信号");
                    return;
                }
                RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_PERSON_INFO, mType, number));
                finish();
                break;
        }

    }



    public static void goPage(Context context, int type, String msg) {
        Intent intent = new Intent(context, SetPersonInfoItemActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("msg", msg);
        context.startActivity(intent);
    }


}
