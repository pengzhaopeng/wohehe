package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.MyFragmentPagerAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityMyBillInfo1Binding;
import com.messoft.gaoqin.wanyiyuan.model.WalletModel;
import com.messoft.gaoqin.wanyiyuan.utils.DebugUtil;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 待入张
 */
public class DaiRuZhangActivity1 extends BaseActivity<ActivityMyBillInfo1Binding> {


    private WalletModel mModel;
    private String beginTime = null; //用来提交后台 需要时分秒 补0
    private String endTime = null;
    private String mDateStart;
    private String mDateEnd;

    private TimePickerDialog mDialogStart;
    private TimePickerDialog mDialogEnd;
    private long mStartMillseconds = -1;
    private long mEndMillseconds = -1;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //    private int mPage = HttpUtils.start_page_java;
    DrzListLeftFragment drzListLeftFragment;
    DrzListRightFragment drzListRightFragment;
    private int mPosition = 0; //选中位置默认0

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bill_info1);

    }

    @Override
    protected void initSetting() {
        showContentView();
        setTitle("待入账");
        setRightImg(R.drawable.rili);
        mModel = new WalletModel();
        bindingView.rlDrz.setVisibility(View.VISIBLE);
        bindingView.drzEdmx.setVisibility(View.GONE);
        if (getIntent() != null) {
            String money = getIntent().getStringExtra("money");
            bindingView.tvDrzMoney.setText(money);
        }
        initTime();

        initTab();
    }

    @Override
    protected void initListener() {
        bindingView.tabLinli.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initTab() {
        ArrayList<Fragment> mFragments = new ArrayList<>();
        ArrayList<String> mTitleList = new ArrayList<>();
        mTitleList.add("互销区商品销售");
        mTitleList.add("业绩提成");
        drzListLeftFragment = DrzListLeftFragment.newInstance(beginTime, endTime);
        drzListRightFragment = DrzListRightFragment.newInstance(beginTime, endTime);
        mFragments.add(drzListLeftFragment);
        mFragments.add(drzListRightFragment);

        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitleList);
        bindingView.vpLinli.setAdapter(myAdapter);
        // 左右预加载页面的个数
        bindingView.vpLinli.setOffscreenPageLimit(mTitleList.size() - 1);
        myAdapter.notifyDataSetChanged();
        bindingView.tabLinli.setTabMode(TabLayout.MODE_FIXED);
        bindingView.tabLinli.setupWithViewPager(bindingView.vpLinli);
    }

    private void initTime() {
        //时间
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mDialogStart = new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        DebugUtil.error("TimePickerDialog", "开始时间：" + millseconds);
                        mStartMillseconds = millseconds;

                        mDateStart = getDateToString(millseconds);
                        beginTime = getDateToString1(millseconds);

//                        bindingView.tvTime.setText(mDateStart);

                        //弹出结束时间
                        mDialogEnd.show(getSupportFragmentManager(), "year_month_day");
                    }
                })
                .setCancelStringId("取消")
                .setSureStringId("下一步")
                .setTitleStringId("开始时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setCyclic(false)
//                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.theme_color))
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.theme_color))
                .setWheelItemTextSize(14)
                .build();

        mDialogEnd = new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        mEndMillseconds = millseconds;
                        if (mStartMillseconds != -1 && mStartMillseconds > mEndMillseconds) {
//                            bindingView.tvTime.setText(null);
                            ToastUtil.showShortToast("开始时间不能大于结束时间");
                            return;
                        }

                        mDateEnd = getDateToString(millseconds);
                        endTime = getDateToString1(millseconds);

                        bindingView.drzEdmx.setText("额度明细 (" + mDateStart + " - " + mDateEnd + ")");

                        if (mPosition == 0) {
                            drzListLeftFragment.setBeginAndEndTime(beginTime, endTime);
                        } else {
                            drzListRightFragment.setBeginAndEndTime(beginTime, endTime);
                        }
                    }
                })
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("结束时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setCyclic(false)
//                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.theme_color))
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.theme_color))
                .setWheelItemTextSize(14)
                .build();


    }

    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }

    public String getDateToString1(long time) {
        Date d = new Date(time);
        return sf1.format(d);
    }

    public static void goPage(Context context, String money) {
        Intent intent = new Intent(context, DaiRuZhangActivity1.class);
        intent.putExtra("money", money);
        context.startActivity(intent);
    }
}
