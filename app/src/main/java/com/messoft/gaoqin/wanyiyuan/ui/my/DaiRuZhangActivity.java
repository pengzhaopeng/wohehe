package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.DrzListAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.bean.MemberCapitalWaitLogList;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityMyBillInfoBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.WalletModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DebugUtil;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.RecycleViewDivider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 待入张
 */
public class DaiRuZhangActivity extends BaseActivity<ActivityMyBillInfoBinding> {

    private DrzListAdapter mAdapter;
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

    private int mPage = HttpUtils.start_page_java;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bill_info);

    }

    @Override
    protected void initSetting() {
        showContentView();
        setTitle("待入账");
        setRightImg(R.drawable.rili);
        mModel = new WalletModel();
        bindingView.rlDrz.setVisibility(View.VISIBLE);
        if (getIntent() != null) {
            String money = getIntent().getStringExtra("money");
            bindingView.tvDrzMoney.setText(money);
        }
        mAdapter = new DrzListAdapter();
        initOnRefresh(getActivity(), bindingView.rc);
//        bindingView.rcAddress.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        bindingView.rc.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL,
                DensityUtils.dp2px(getActivity(), 8),
                getResources().getColor(R.color.colorPageBg)));
        bindingView.rc.setAdapter(mAdapter);

        initTime();

        loadData();
    }

    @Override
    protected void initListener() {
        bindingView.refreshLayout.setEnableRefresh(false);
        bindingView.refreshLayout.setOnLoadmoreListener(refreshlayout -> refreshlayout.getLayout().postDelayed(() -> {
            mPage++;
            loadData();
            refreshlayout.finishLoadmore();
        }, 300));

        getRightTv().setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                //开始时间
                mDialogStart.show(getSupportFragmentManager(), "year_month_day");
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener<MemberCapitalWaitLogList>() {
            @Override
            public void onClick(MemberCapitalWaitLogList memberCapitalWaitLogList, int position) {
                if (memberCapitalWaitLogList != null && memberCapitalWaitLogList.getType().equals("0")) {
                    DaiRuZhangInfoActivit.goPage(getActivity(), memberCapitalWaitLogList);
                }
            }
        });
    }

    private void loadData() {
        mModel.getMemberCapitalWaitLogList(getActivity(), BusinessUtils.getBindAccountId(), "0", beginTime, endTime, mPage, HttpUtils.per_page_20, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                List<MemberCapitalWaitLogList> data = (List<MemberCapitalWaitLogList>) object;
                if (mPage == HttpUtils.start_page_java) {
                    if (data != null && data.size() > 0) {
                        mAdapter.clear();
                        mAdapter.addAll(data);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.showLongToast("未查询到明细数据");
                        mAdapter.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    mAdapter.addAll(data);
                    mAdapter.notifyDataSetChanged();
                    bindingView.refreshLayout.finishLoadmore();
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
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

                        //开始搜索
                        loadData();
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
        Intent intent = new Intent(context, DaiRuZhangActivity.class);
        intent.putExtra("money", money);
        context.startActivity(intent);
    }
}
