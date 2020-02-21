package com.messoft.gaoqin.wanyiyuan.ui.market;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.BeanOrderInfoById;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityGuashouInfoBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.GoldModel;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TextViewUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.CountDownTextView;


public class GuaShouInfoActivity extends BaseActivity<ActivityGuashouInfoBinding> {

    private int mType; //(0:待交易,1:待付款,2:已付款待发货,3:已发货已完成,9:已取消)
    private String mId;
    private GoldModel mGoldModel;

    private boolean mIsInvalid = false; //是否失效

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guashou_info);
    }

    @Override
    protected void initSetting() {
        showContentView();
        mGoldModel = new GoldModel();
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type",-1);
            mId = getIntent().getStringExtra("id");
            switch (mType){
                case 0:
                    daijiaoyi();
                    break;
                case 1:
                    daishoukuan();
                    break;
                case 2:
                    daifangxing();
                    break;
                case 3:
                    yiwancheng();
                    break;
            }
        }
        loadData();
    }

    @Override
    protected void initListener() {
        bindingView.ivBack.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
        //取消挂售
        bindingView.btnQxgs.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                mGoldModel.cancelBeanOrderForm(getActivity(), mId, new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        ToastUtil.showShortToast("取消挂售成功");
                        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_MY_GS_ORDER, 0));
                    }

                    @Override
                    public void loadFailed(int errorCode, String errorMessage) {
                        ToastUtil.showShortToast(errorMessage);
                    }
                });
            }
        });
        //放行
        bindingView.btnFx.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                mGoldModel.deliverBeanOrderForm(getActivity(), mId, new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        ToastUtil.showShortToast("放行成功");
                        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_MY_GS_ORDER, 0));
                    }

                    @Override
                    public void loadFailed(int errorCode, String errorMessage) {
                        ToastUtil.showShortToast(errorMessage);
                    }
                });
            }
        });
    }

    private void loadData() {
        mGoldModel.getBeanOrderInfoById(getActivity(), mId, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                BeanOrderInfoById data = (BeanOrderInfoById) object;
                setData(data);
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setData(BeanOrderInfoById data) {
        bindingView.tvTitle.setText(data.getTitle());
        bindingView.tvTitle1.setText(data.getTitle());
        //暂时没有字段
//        int[] blankBgAndLog = BusinessUtils.getBlankBgAndLog(data.getTypeCode());
//        bindingView.ivYhkBg.setImageResource(blankBgAndLog[0]);
//        bindingView.ivYhkLogo.setImageResource(blankBgAndLog[1]);
        //(0:微信,1:支付宝,2:银行卡)
        String payType = data.getPayType();
        switch (mType) {
            case 0: //待交易
                bindingView.tvCsjd.setText(data.getBeanAmount());
                bindingView.tvSfje.setText(data.getAmount());
                bindingView.tvSkr.setText(data.getName());
                bindingView.tvLxfs.setText(data.getContactMobile());
                bindingView.tvWxh.setText(data.getWechat());
                bindingView.tvGssj.setText(data.getCreateTime());
                switch (payType){
                    case "0":
                        bindingView.llErweima1.setVisibility(View.VISIBLE);
                        ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getPayCode()),bindingView.ivMa);
                        TextViewUtils.setTvImg("微信收款码", R.drawable.wx_48, bindingView.ma1, 0);
                        break;
                    case "1":
                        ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getPayCode()),bindingView.ivMa);
                        bindingView.llErweima1.setVisibility(View.VISIBLE);
                        TextViewUtils.setTvImg("支付宝收款码", R.drawable.zfb_48, bindingView.ma1, 0);
                        break;
                    case "2":
                        bindingView.llYhk1.setVisibility(View.VISIBLE);
                        bindingView.cardName1.setText(data.getName());
                        bindingView.cardType1.setText(data.getBankName() + " (" + data.getBanckBranchName() + ")");
                        bindingView.cardCode1.setText(data.getCardNo());
                        break;
                }
                break;
            case 1: //待付款
                // 倒计时时间
                setDjsTime(data.getOrderTime());
                setDataNoDjy(data, payType);
                break;
            case 2:
                setDataNoDjy(data, payType);
                break;
            case 3:
                setDataNoDjy(data, payType);
                break;
        }
    }

    /**
     * 设计倒计时间
     *
     * @param orderTime
     */
    private void setDjsTime(String orderTime) {
        if (!StringUtils.isNoEmpty(orderTime)) return;
        //计算时间差 毫秒 如果小于15分钟就倒计时 否则待付款订单失效
        Long diffTime = TimeUtils.longTimeToDay1(orderTime);
        if (diffTime <= 15 * 60 * 1000 && diffTime > 0) {
            bindingView.tvTime.startCountDown(15 * 60 - (diffTime / 1000));
        } else {
            //TODO 订单已失效
            ToastUtil.showShortToast("订单已失效");
            mIsInvalid = true;
        }

    }

    /**
     * 设置非待交易数据
     * @param data
     * @param payType
     */
    private void setDataNoDjy(BeanOrderInfoById data, String payType) {
        bindingView.tvJdsl.setText(data.getBeanAmount());
        bindingView.tvSfje1.setText(data.getAmount());
        bindingView.tvMg.setText(data.getName());
        bindingView.tvLxfs1.setText(data.getContactMobile());
        bindingView.tvWx.setText(data.getWechat());
        bindingView.tvDdh.setText(data.getOrderNo());
        bindingView.tvXdsj.setText(data.getOrderTime());
        bindingView.tvGssj.setText(data.getCreateTime());
        bindingView.tvFksj.setText(data.getPayTime());
        switch (payType){
            case "0":
                ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getPayCode()),bindingView.ivMa1);
                bindingView.llErweima.setVisibility(View.VISIBLE);
                TextViewUtils.setTvImg("微信收款码", R.drawable.wx_48, bindingView.ma11, 0);
                break;
            case "1":
                ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getPayCode()),bindingView.ivMa1);
                bindingView.llErweima.setVisibility(View.VISIBLE);
                TextViewUtils.setTvImg("支付宝收款码", R.drawable.zfb_48, bindingView.ma11, 0);
                break;
            case "2":
                bindingView.llYhk.setVisibility(View.VISIBLE);
                bindingView.cardName.setText(data.getName());
                bindingView.cardType.setText(data.getBankName() + " (" + data.getBanckBranchName() + ")");
                bindingView.cardCode.setText(data.getCardNo());
                break;
        }
    }


    /**
     * 待交易
     */
    public void daijiaoyi(){

        bindingView.llTitleDjy.setVisibility(View.VISIBLE);
        bindingView.rlBottom.setVisibility(View.VISIBLE);
        //取消挂售
        bindingView.btnQxgs.setVisibility(View.VISIBLE);
    }

    /**
     * 待收款
     */
    public void daishoukuan(){
        //倒计时控件
        final String label = "剩下 ";
        bindingView.tvTime.setNormalText("剩下")
//                .setBeforeIndex(label.length())
                .setCountDownClickable(false)
                .setIsShowComplete(true)
                .setShowFormatTime(true)
                .setOnCountDownTickListener(new CountDownTextView.OnCountDownTickListener() {
                    @Override
                    public void onTick(long untilFinished, String showTime, CountDownTextView tv) {
//                        tv.setText(CustomerViewUtils.getMixedText(label + showTime, tv.getTimeIndexes(), true));
                        tv.setText(showTime);
                    }
                })
                .setOnCountDownFinishListener(new CountDownTextView.OnCountDownFinishListener() {
                    @Override
                    public void onFinish() {
                        bindingView.tvTime.setText("订单已失效");
                        mIsInvalid = true;
                    }
                });

        bindingView.llTitleDsk.setVisibility(View.VISIBLE);
        bindingView.llNoDjyContent.setVisibility(View.VISIBLE);
        bindingView.llFksj.setVisibility(View.VISIBLE);

    }

    /**
     * 待放行
     */
    public void daifangxing(){
        bindingView.llTitleDfx.setVisibility(View.VISIBLE);
        bindingView.llNoDjyContent.setVisibility(View.VISIBLE);
        bindingView.rlFksj.setVisibility(View.VISIBLE);
        bindingView.rlBottom.setVisibility(View.VISIBLE);
        bindingView.tvSs.setVisibility(View.VISIBLE);
        bindingView.btnFx.setVisibility(View.VISIBLE);
    }

    /**
     * 已完成
     */
    public void yiwancheng(){
        bindingView.llTitleYwc.setVisibility(View.VISIBLE);
        bindingView.llNoDjyContent.setVisibility(View.VISIBLE);
        bindingView.rlFksj.setVisibility(View.VISIBLE);
    }


    public static void goPage(Context context, int type, String id) {
        Intent intent = new Intent(context, GuaShouInfoActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }
}
