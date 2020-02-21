package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.flyco.roundview.RoundTextView;
import com.jaeger.library.StatusBarUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.AddressBean;
import com.messoft.gaoqin.wanyiyuan.bean.GetRushOrderList;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityMyQgInfoBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.QgModel;
import com.messoft.gaoqin.wanyiyuan.ui.home.CommonOrderPayActivity;
import com.messoft.gaoqin.wanyiyuan.ui.market.WoYaoShenShuActivity;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.HideViewUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TextViewUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.CountDownTextView;

/**
 * 我的抢购订单详情
 */
public class MyQgInfoActivity extends BaseActivity<ActivityMyQgInfoBinding> {

    private String mId; //orderId
    private int mType; // 4-待付款 6-已付款 14-已完成
    private int flagType; // 0-抢购 1-寄售
    private QgModel mQgModel;
    private RxBus mRxBus;
    private GetRushOrderList mData;
    private boolean mIsInvalid = false; //是否失效

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qg_info);
    }

    @Override
    protected void initSetting() {
        showContentView();
        initRxBus();
        mQgModel = new QgModel();
        if (getActivity() != null && getActivity().getWindow() != null) {
            StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.theme_color), 0);
        }
        if (getIntent() != null) {
            mId = getIntent().getStringExtra("id");
            mType = getIntent().getIntExtra("type", -1);
            flagType = getIntent().getIntExtra("flagType", -1);

            //初始化倒计时控件
            if (mType == MyQgListActivity.my_qg_dfu) {
                bindingView.tvCtTime.setVisibility(View.VISIBLE);
                bindingView.tvPayMsg.setVisibility(View.VISIBLE);
                bindingView.tvCtTime.setNormalText("剩下")
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

        //折叠寄售方
        bindingView.rlJsfTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideViewUtils.newInstance(getActivity(), bindingView.llJsfContent,
                        bindingView.ivDown).toggle();
            }
        });
    }

    private void loadData() {
        mQgModel.getRushOrderById(getActivity(), mId, BusinessUtils.getBindAccountId(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                mData = (GetRushOrderList) object;
                if (mData != null) {
                    setData(mData);
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    /**
     * 设置数据
     *
     * @param data
     */
    @SuppressLint("SetTextI18n")
    private void setData(GetRushOrderList data) {
        if (flagType == 0) {
            //顶部状态
            switch (mType) {
                case MyQgListActivity.my_qg_dfu:
                    setDjsTime(data.getOrderTime());
                    bindingView.tvPayStatus.setText("待付款");
                    break;
                case MyQgListActivity.my_qg_yfk:
                    bindingView.tvPayStatus.setText("已付款");
                    bindingView.tvTitle1.setVisibility(View.VISIBLE);
                    bindingView.tvTitle1.setText("请等待对方确认中…");
                    break;
                case MyQgListActivity.my_qg_ywc:
                    bindingView.tvPayStatus.setText("已完成");
                    bindingView.tvTitle1.setVisibility(View.VISIBLE);
                    bindingView.tvTitle1.setText("订单已完成~");
                    break;

            }
        } else {
            //顶部状态
            switch (mType) {
                case MyQgListActivity.my_js_dsk:
                    setDjsTime(data.getOrderTime());
                    bindingView.tvPayStatus.setText("待收款");
                    break;
                case MyQgListActivity.my_js_dfx:
                    bindingView.tvPayStatus.setText("待放行");
                    break;
                case MyQgListActivity.my_js_ywc:
                    bindingView.tvPayStatus.setText("已完成");
                    bindingView.tvTitle1.setVisibility(View.VISIBLE);
                    bindingView.tvTitle1.setText("订单已完成~");
                    break;

            }
        }

        bindingView.tvCode.setText(data.getOrderNo());
        bindingView.tvTime.setText(data.getCreateTime());
        if (data.getImgsList() != null && data.getImgsList().size() > 0) {
            ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getImgsList().get(0)), bindingView.iv, 1, TimeUtils.date2TimeStamp(data.getUpdateTime()));
        }
        bindingView.tvTitle.setText(data.getGoodsName());
//        bindingView.tvNumber.setText("产品编号：" + data.getGoodsId());
        if (StringUtils.isNoEmpty(data.getEarningRate())) {
            bindingView.tvNumber.setText("产品编号：" + data.getEarningRate());
        }else{
            bindingView.tvNumber.setText("产品编号：" + data.getGoodsId());
        }
        bindingView.tvPrice.setText("￥" + data.getGoodsPrice());
        bindingView.tvName.setText("收件人：" + data.getConsigneeName());
        bindingView.tvPhone.setText(data.getConsigneeMobile());
        @SuppressLint("StringFormatMatches") String addressDesc = String.format(getResources().getString(R.string.address_info),
                data.getProvinceText(),
                data.getCityText(),
                data.getDistrictText(),
                data.getAddress());
        bindingView.tvAddress.setText(addressDesc);

        //抢购方隐藏购买人信息
        if (flagType == 0) {
            bindingView.llGmf.setVisibility(View.VISIBLE);
        }
        //购买人
        bindingView.tvGmGmr.setText(data.getName());
        bindingView.tvGmLxfs.setText(data.getMobile());
        bindingView.tvGmWx.setText(data.getPartner());
        //寄售方
        bindingView.tvJsf.setText(data.getShopName());
        bindingView.tvLxfs.setText(data.getShopMobile());
        bindingView.tvWx.setText(data.getWechat());
        //付款时间
        bindingView.payTime.setText(data.getOrderTime());
        //是否提货
        String isTh = data.getIsRevenue().equals("0") ? "否" : "是";
        bindingView.payTh.setText(isTh);

        //(0:微信,1:支付宝,2:银行卡)
        String payType = data.getPayType();
        switch (payType) {
            case "0":
                ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getPayCode()), bindingView.ivMa1);
                bindingView.llErweima.setVisibility(View.VISIBLE);
                TextViewUtils.setTvImg("微信收款码", R.drawable.wx_48, bindingView.ma11, 0);
                break;
            case "1":
                ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getPayCode()), bindingView.ivMa1);
                bindingView.llErweima.setVisibility(View.VISIBLE);
                TextViewUtils.setTvImg("支付宝收款码", R.drawable.zfb_48, bindingView.ma11, 0);
                break;
            case "2":
                bindingView.llYhk.setVisibility(View.VISIBLE);
                bindingView.cardName.setText(data.getCardName());
                bindingView.cardType.setText(data.getBankName() + " (" + data.getBanckBranchName() + ")");
                bindingView.cardCode.setText(data.getCardNo());
                break;
        }

        //设置底部按钮
        if (flagType == 0) {
            setQgBtn(data);
        } else {
            setJsBtn(data);
        }
    }

    /**
     * 设计倒计时间
     *
     * @param orderTime
     */
    private void setDjsTime(String orderTime) {
//        if (!StringUtils.isNoEmpty(orderTime)) return;
//        //计算时间差 毫秒 如果小于15分钟就倒计时 否则待付款订单失效
//        Long diffTime = TimeUtils.longTimeToDay1(orderTime);
//        if (diffTime <= 15 * 60 * 1000 && diffTime > 0) {
//            bindingView.tvCtTime.startCountDown(15 * 60 - (diffTime / 1000));
//        } else {
//            //TODO 订单已失效
//            ToastUtil.showShortToast("订单已失效");
//            mIsInvalid = true;
//        }
        //2020/1/7 改动 orderTime为截至时间
        if (!StringUtils.isNoEmpty(orderTime)) return;
        Long diffTime = TimeUtils.longTimeToDay1(orderTime);
        if (diffTime <= 0) { //未失效
            bindingView.tvCtTime.startCountDown(Math.abs(diffTime) / 1000);
        } else {
            ToastUtil.showShortToast("订单已失效");
            mIsInvalid = true;
        }
    }


    private void setJsBtn(GetRushOrderList data) {
        switch (mType) {
            case MyQgListActivity.my_js_dsk: //待付款
//                bindingView.rlBottom.setVisibility(View.GONE);
                bindingView.rlPayTime.setVisibility(View.GONE);
                if (!StringUtils.isNoEmpty(data.getPayType())) {
                    setBtnState(bindingView.btnRight, View.VISIBLE, "补录收款信息", "#FF9800", "#FF9800");
                    bindingView.btnRight.setOnClickListener(new PerfectClickListener() {
                        @Override
                        protected void onNoDoubleClick(View v) {
                            WyjsActivity.goPage(getActivity(), data.getOrderId(), data.getGoodsId(), data.getGoodsTypeId(), data.getGoodsName(),
                                    data.getShopName(),
                                    data.getShopMobile(),
                                    data.getWechat(),
                                    1);
                        }
                    });
                } else {
                    setBtnState(bindingView.btnRight, View.GONE, "补录收款信息", "#FF9800", "#FF9800");
                }
                break;
            case MyQgListActivity.my_js_dfx: //待放行
                bindingView.rlBottom.setVisibility(View.VISIBLE);
                setBtnState(bindingView.btnRight, View.VISIBLE, "放行", "#FF9800", "#FF9800");
                bindingView.btnRight.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        //去放行
                        DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(), "温馨提示", "是否已收到对应货款,确认放行后无法再进行申述,是否确认?", "确认放行", "取消", new DialogWithYesOrNoUtils.DialogCallBack() {
                            @Override
                            public void exectEvent(DialogInterface dialog) {
                                dialog.dismiss();
                                //放行
                                mQgModel.updateRushOrderForm(getActivity(),
                                        1,
                                        BusinessUtils.getBindAccountId(),
                                        data.getOrderId(),
                                        "10",
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null, new RequestImpl() {
                                            @Override
                                            public void loadSuccess(Object object) {
                                                ToastUtil.showShortToast("放行成功");
                                                data.setStatus("8");
                                                RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_QG_ORDER_LIST, 0));
                                                finish();
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
                });
                //查看是否申述 0:无申诉，1：申诉
                if (data.getIsVipShop().equals("0")) {
                    setBtnState(bindingView.btnLeft, View.VISIBLE, "申述", "#999999", "#FF666666");
                    bindingView.btnLeft.setOnClickListener(new PerfectClickListener() {
                        @Override
                        protected void onNoDoubleClick(View v) {
                            //去申述
                            WoYaoShenShuActivity.goPage(getActivity(), 1, data.getOrderId(), data.getOrderNo());
                        }
                    });
                } else {
                    setBtnState(bindingView.btnLeft, View.VISIBLE, "已申述", "#999999", "#FF666666");
                }

                break;
            case MyQgListActivity.my_js_ywc: //已完成
                bindingView.rlBottom.setVisibility(View.GONE);
                bindingView.rlTh.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setQgBtn(GetRushOrderList data) {
        switch (mType) {
            case MyQgListActivity.my_qg_dfu: //待付款
                bindingView.rlPayTime.setVisibility(View.GONE);
                String distributionStatus = data.getDistributionStatus();
                //设置是否转让
                if (distributionStatus.equals("0")) { //未转让
                    setBtnState(bindingView.btnRight, View.VISIBLE, "转让订单", "#999999", "#FF666666");
                    //转让订单 经补录收货地址的订单不可以点击订单转让
                    bindingView.btnRight.setOnClickListener(new PerfectClickListener() {
                        @Override
                        protected void onNoDoubleClick(View v) {
                            if (StringUtils.isNoEmpty(data.getProvince())) {
                                ToastUtil.showLongToast("已补录收货地址的订单不可以转让");
                                return;
                            }
                            FindFriendActivity.goPage(getActivity(), 1, data);
                        }
                    });
                } else {
                    setBtnState(bindingView.btnRight, View.VISIBLE, "已转让", "#FF9800", "#FF9800");
                }
                //看是否补录了收货地址
                setBtnState(bindingView.btnLeft, -1, "补录收货地址", "#999999", "#FF666666");
                if (StringUtils.isNoEmpty(data.getProvince())) {
                    bindingView.btnLeft.setVisibility(View.GONE);
                } else {
                    bindingView.btnLeft.setVisibility(View.VISIBLE);
                }
                //去补录
                bindingView.btnLeft.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        AddressActivity.goPage(getActivity(), 2);
                    }
                });
                //显示我已付款 待付款都显示 点了就改状态
                setBtnState(bindingView.btnRight1, View.VISIBLE, "我已付款", "#FF9800", "#FF9800");
                bindingView.btnRight1.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        //未补录收货地址的订单不可以付款
                        if (!StringUtils.isNoEmpty(data.getProvince())) {
                            ToastUtil.showLongToast("未补录收货地址的订单不可以付款");
                            return;
                        }
                        DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(), "温馨提示", "是否已转账给对方账户应付金额？", "我已付款", "未转账", new DialogWithYesOrNoUtils.DialogCallBack() {
                            @Override
                            public void exectEvent(DialogInterface dialog) {
                                dialog.dismiss();
                                //我已付款
                                mQgModel.updateRushOrderForm(getActivity(),
                                        0,
                                        BusinessUtils.getBindAccountId(),
                                        data.getOrderId(),
                                        "4",
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null, new RequestImpl() {
                                            @Override
                                            public void loadSuccess(Object object) {
                                                ToastUtil.showShortToast("付款成功");
                                                data.setStatus("4");

                                                loadData();
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
                });
                break;
            case MyQgListActivity.my_qg_yfk: //已付款
                //查看是否申述 0:无申诉，1：申诉
                if (data.getIsVipMember().equals("0")) {
                    setBtnState(bindingView.btnRight, View.VISIBLE, "申述", "#FF9800", "#FF9800");
                    bindingView.btnRight.setOnClickListener(new PerfectClickListener() {
                        @Override
                        protected void onNoDoubleClick(View v) {
                            //去申述
                            WoYaoShenShuActivity.goPage(getActivity(), 2, data.getOrderId(), data.getOrderNo());
                        }
                    });
                } else {
                    setBtnState(bindingView.btnRight, View.VISIBLE, "已申述", "#999999", "#FF666666");
                }


                setBtnState(bindingView.btnLeft, View.VISIBLE, "等待放行", "#999999", "#FFBBBBBB");
                break;
            case MyQgListActivity.my_qg_ywc: //已完成
                bindingView.btnRight1.setVisibility(View.GONE);
                if (data.getIsRevenue().equals("0")) { //我要提货
                    //显示已提货按钮
                    bindingView.rlTh.setVisibility(View.VISIBLE);

                    Long intervalDays = TimeUtils.longTimeToDay1(data.getIntervalDays());
                    Long intervalTimes = TimeUtils.longTimeToDay1(data.getIntervalTimes());
                    Long beginSwapTime = TimeUtils.longTimeToDay1(data.getBeginSwapTime());
                    Long endSwapTime = TimeUtils.longTimeToDay1(data.getEndSwapTime());
                    if (intervalDays >= 0) {
                        if (beginSwapTime >= 0 && endSwapTime <= 0) {
                            //开始判断状态
                            if (data.getStatus().equals("10")) {
                                //申请置换
                                setBtnState(bindingView.btnRight, View.VISIBLE, "申请置换", "#FF9800", "#FF9800");
                                //申请置换
                                bindingView.btnRight.setOnClickListener(new PerfectClickListener() {
                                    @Override
                                    protected void onNoDoubleClick(View v) {
                                        String msg = "申请置换前，需要缴纳 " + (data.getReplacementFee() * 100) + "% 的手续费，金额为￥" + data.getStatusRatio() + "，缴纳后自动置换成功，是否继续？";
                                        DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(), "提示", msg, "支付", "取消", new DialogWithYesOrNoUtils.DialogCallBack() {
                                            @Override
                                            public void exectEvent(DialogInterface dialog) {
                                                CommonOrderPayActivity.goPage(getActivity(), data.getOrderId(), null, Double.parseDouble(data.getStatusRatio()),
                                                        "2", 0);
                                            }

                                            @Override
                                            public void exectCancel(DialogInterface dialog) {
                                                dialog.dismiss();
                                            }
                                        });
                                    }
                                });
                            } else if (data.getStatus().equals("12") || data.getStatus().equals("14")) {
                                //置换换成 显示已完成
                                setBtnState(bindingView.btnRight, View.VISIBLE, "置换完成", "#999999", "#FFBBBBBB");
                                setBtnState(bindingView.btnLeft, View.GONE, "置换完成", "#999999", "#FFBBBBBB");
                            }
                        } else {
                            switch (data.getStatus()) {
                                case "12":
                                case "14":
                                    //置换换成 显示已完成
                                    setBtnState(bindingView.btnRight, View.VISIBLE, "置换完成", "#999999", "#FFBBBBBB");
                                    setBtnState(bindingView.btnLeft, View.GONE, "置换完成", "#999999", "#FFBBBBBB");
                                    break;
                                case "10":
                                    //申请置换
                                    setBtnState(bindingView.btnRight, View.VISIBLE, "申请置换", "#999999", "#FFBBBBBB");

                                    break;
                                default:
                                    //等待置灰 显示等待置换
                                    setBtnState(bindingView.btnRight, View.VISIBLE, "等待置换", "#999999", "#FFBBBBBB");
                                    break;
                            }

                        }
                    }

                    if (intervalTimes <= 0) {
                        if (data.getStatus().equals("12") || data.getStatus().equals("14")) {
                            bindingView.btnLeft.setVisibility(View.GONE);
                        } else {
                            if(data.getStatus().equals("10")) {
                                //申请置换变灰
                                setBtnState(bindingView.btnRight, View.VISIBLE, "申请置换", "#999999", "#FFBBBBBB");
                            }
                            //可以申请提货
                            setBtnState(bindingView.btnLeft, View.VISIBLE, "我要提货", "#FF9800", "#FF9800");
                            bindingView.btnLeft.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(), "温馨提示", "确认提货吗", "收货", "取消", new DialogWithYesOrNoUtils.DialogCallBack() {
                                        @Override
                                        public void exectEvent(DialogInterface dialog) {
                                            mQgModel.updateRushOrderForm(getActivity(),
                                                    0,
                                                    BusinessUtils.getBindAccountId(),
                                                    data.getOrderId(),
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    "1", new RequestImpl() {
                                                        @Override
                                                        public void loadSuccess(Object object) {

                                                            ToastUtil.showShortToast("提货成功");

                                                            setBtnState(bindingView.btnLeft, View.VISIBLE, "已提货", "#999999", "#FFBBBBBB");
                                                            data.setIsRevenue("1");
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
                            });
                        }

                    } else {
                        //不能申请提货 提货按钮隐藏
                        setBtnState(bindingView.btnLeft, View.GONE, "我要提货", "#FF9800", "#FF9800");
                    }

                } else if (data.getIsRevenue().equals("1")) { //已提货
                    //显示已完成
                    setBtnState(bindingView.btnLeft, View.VISIBLE, "已完成提货", "#999999", "#FFBBBBBB");
                    bindingView.btnRight.setVisibility(View.GONE);
                }

                break;
        }
    }

    private void setBtnState(RoundTextView tv, int visible, String msg, String strokeColor, String textColor) {
        if (visible != -1) {
            tv.setVisibility(visible);
        }
        tv.setText(msg);
        tv.getDelegate().setStrokeColor(Color.parseColor(strokeColor));
        tv.setTextColor(Color.parseColor(textColor));
    }


    /**
     * 补录地址
     *
     * @param getAddress
     * @param data
     */
    private void buLuAdddress(AddressBean getAddress, GetRushOrderList data) {
        //待付款再操作
        if (mType != MyQgListActivity.my_qg_dfu) return;
        mQgModel.updateRushOrderForm(getActivity(),
                0,
                BusinessUtils.getBindAccountId(),
                data.getOrderId(),
                null,
                null,
                getAddress.getContactName(),
                getAddress.getContactMobile(),
                getAddress.getProvince(),
                getAddress.getProvinceText(),
                getAddress.getCity(),
                getAddress.getCityText(),
                getAddress.getDistrict(),
                getAddress.getDistrictText(),
                getAddress.getAddress(),
                null, new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        ToastUtil.showShortToast("补录地址成功");
                        bindingView.btnLeft.setVisibility(View.GONE);

                        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_QG_ORDER_LIST, 0));
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
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_ORDER_ADDRESS) {
                if (rxBusMessage.getData() != null) {
                    AddressBean getAddress = (AddressBean) rxBusMessage.getData();
                    buLuAdddress(getAddress, mData);
                }
            }
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_QG_LIST) {
                if (rxBusMessage.getI() == 0) {
                    //刷新数据
                    loadData();
                }
            }

            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.PAY_SUCESS_WX) {
//                int i = rxBusMessage.getI();
                //刷新列表
                loadData();
            }

            //支付宝支付成功
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.PAY_SUCESS_ZFB) {
//                int i = rxBusMessage.getI();
                //刷新列表
                loadData();
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

    public static void goPage(Context context, String id, int type, int flagType) {
        Intent intent = new Intent(context, MyQgInfoActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        intent.putExtra("flagType", flagType);
        context.startActivity(intent);
    }

}
