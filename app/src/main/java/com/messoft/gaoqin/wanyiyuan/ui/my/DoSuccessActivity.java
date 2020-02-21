package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityDoSuccessBinding;

/**
 * 充值 提现 转账 成功页面
 */


public class DoSuccessActivity extends BaseActivity<ActivityDoSuccessBinding> {

    private int mType; //0-充值 1-提现 2-转账

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_success);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initSetting() {
        showContentView();
        setTitle("操作成功");
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
        }
        switch (mType) {
            case 0:
                bindingView.tvType.setText("充值成功");
                bindingView.llCz.setVisibility(View.VISIBLE);
                String number2 = getIntent().getStringExtra("number");
                bindingView.tvNumber.setText(number2);
                String payType = getIntent().getStringExtra("payType");
                bindingView.tvCz1.setText("付款方式：" + payType);
                break;
            case 1:
                bindingView.tvType.setText("提现成功");
                bindingView.llTx.setVisibility(View.VISIBLE);
                String number = getIntent().getStringExtra("number");
                bindingView.tvNumber.setText(number);
                String blank = getIntent().getStringExtra("blank");
                bindingView.tvTx1.setText("提现银行：" + blank);
                bindingView.tvTx2.setText("付款方式：余额支付");
                String sxf = getIntent().getStringExtra("sxf");
                bindingView.tvTx3.setText("手续费：" + sxf);
                break;
            case 2:
                bindingView.tvType.setText("转赠成功");
                bindingView.llZz.setVisibility(View.VISIBLE);
                String number1 = getIntent().getStringExtra("number");
                bindingView.tvNumber.setText(number1);
                String blank1 = getIntent().getStringExtra("blank");
                bindingView.tvZz1.setText("对方账户信息" + blank1);
                bindingView.tvZz2.setText("付款方式：余额支付");
                String sxf1 = getIntent().getStringExtra("sxf");
                bindingView.tvZz3.setText("手续费：" + sxf1);
                break;

        }
    }

    @Override
    protected void initListener() {
        bindingView.tvNext.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
    }

    public static void goPage(Context context, int type, String payType) {
        Intent intent = new Intent(context, DoSuccessActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("payType", payType);
        context.startActivity(intent);
    }

    public static void goPage(Context context, int type, String number, String blank, String sxf) {
        Intent intent = new Intent(context, DoSuccessActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("number", number);
        intent.putExtra("blank", blank);
        intent.putExtra("sxf", sxf);
        context.startActivity(intent);
    }
}
