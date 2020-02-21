package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.MemberCapitalWaitLogList;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityDaiRuZhangInfoBinding;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;

/**
 * 待入账详情
 */
public class DaiRuZhangInfoActivit extends BaseActivity<ActivityDaiRuZhangInfoBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dai_ru_zhang_info);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initSetting() {
        showContentView();
        setTitle("待入账详情");
        if (getIntent() != null) {
            MemberCapitalWaitLogList data = (MemberCapitalWaitLogList) getIntent().getSerializableExtra("data");
            if (data != null) {
                bindingView.tv1.setText("待入账金额：" + data.getAmount());
                bindingView.tv2.setText("排队开始时间：" + data.getCreateTime());
                bindingView.tv3.setText("待入账原因："+ TimeUtils.timeFormat(data.getCreateTime(),"mm月dd号")+"商品批发区商品出售");
                bindingView.tv4.setText("预计出货时间：" + data.getQueueWaitTime());
                String money = null;
                if (data.getQueueMaxAmount() <= 10000) {
                    money = data.getQueueWaitTotalAmount() > data.getQueueMaxAmount() ?
                            data.getQueueMaxAmount() + " 以上" :
                            data.getQueueWaitTotalAmount() + "";

                } else {
                    money = data.getQueueWaitTotalAmount() > data.getQueueMaxAmount() ?
                            data.getQueueMaxAmount() / 10000 + "w 以上" :
                            data.getQueueWaitTotalAmount() / 10000 + "w";
                }

                bindingView.tv5.setText("待出货排队金额：" + money);
            }
        }
    }

    @Override
    protected void initListener() {

    }

    public static void goPage(Context context, MemberCapitalWaitLogList data) {
        Intent intent = new Intent(context, DaiRuZhangInfoActivit.class);
        intent.putExtra("data", data);
        context.startActivity(intent);
    }
}
