package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityMyQgBinding;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;

/**
 * 我的抢购+我的寄售+我的申述
 */
public class MyQgActivity extends BaseActivity<ActivityMyQgBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qg);
    }

    @Override
    protected void initSetting() {
        setTitle("我的抢购");
        showContentView();
    }

    @Override
    protected void initListener() {
        bindingView.cwQg.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(),MyQgListActivity.class);
            }
        });
        bindingView.cwJs.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), MyJsListActivity.class);
            }
        });
        bindingView.cwSs.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), MySsListActivity.class);
            }
        });
    }
}
