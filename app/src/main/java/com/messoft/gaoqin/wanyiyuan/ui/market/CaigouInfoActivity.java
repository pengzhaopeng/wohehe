package com.messoft.gaoqin.wanyiyuan.ui.market;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.BeanOrderInfoById;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityCaigouInfoBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.GoldModel;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TextViewUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.CountDownTextView;


/**
 * 采购详情
 */
public class CaigouInfoActivity extends BaseActivity<ActivityCaigouInfoBinding> {

    private GoldModel mGoldModel;
    private String mId;
    private int mType; //(0:待交易,1:待付款,2:已付款待发货,3:已发货已完成,9:已取消)
    private boolean mIsInvalid = false; //是否失效


    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caigou_info);
        mGoldModel = new GoldModel();
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
            mId = getIntent().getStringExtra("id");
            switch (mType) {
                case 1:
                    dfk();
                    break;
                case 2:
                    yfk();
                    break;
                case 3:
                    ywc();
                    break;
            }
        }
        loadData();
    }

    @Override
    protected void initSetting() {
        showContentView();
//        mModel = new GoldModel();
        if (getIntent() != null) {
            mId = getIntent().getStringExtra("id");
        }
    }

    @Override
    protected void initListener() {
        bindingView.ivBack.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });

        //我要申述
        bindingView.btnWyss.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(), WoYaoShenShuActivity.class);
            }
        });

        //取消订单
        bindingView.btnQxdd.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(),
                        "", "确认取消此订单？",
                        "确认取消", "我再想想", new DialogWithYesOrNoUtils.DialogCallBack() {
                            @Override
                            public void exectEvent(DialogInterface dialog) {
                                mGoldModel.cancelBeanOrderForm(getActivity(), mId, new RequestImpl() {
                                    @Override
                                    public void loadSuccess(Object object) {

                                        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_MY_CG_ORDER, 0));
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
        //我已付款
        bindingView.btnYfk.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (mIsInvalid) {
                    ToastUtil.showShortToast("订单已失效");
                    return;
                }
                DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(),
                        "", "确定在线下已付款？",
                        "确定", "取消", new DialogWithYesOrNoUtils.DialogCallBack() {
                            @Override
                            public void exectEvent(DialogInterface dialog) {
                                mGoldModel.payBeanOrderForm(getActivity(), mId, new RequestImpl() {
                                    @Override
                                    public void loadSuccess(Object object) {
                                        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_MY_CG_ORDER, 0));
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
        //暂时没有字段
//        int[] blankBgAndLog = BusinessUtils.getBlankBgAndLog(data.getTypeCode());
//        bindingView.ivYhkBg.setImageResource(blankBgAndLog[0]);
//        bindingView.ivYhkLogo.setImageResource(blankBgAndLog[1]);
        //(0:微信,1:支付宝,2:银行卡)
        String payType = data.getPayType();
        switch (mType) {
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
     * 设置交易数据
     *
     * @param data
     * @param payType
     */
    private void setDataNoDjy(BeanOrderInfoById data, String payType) {
        bindingView.tvTitle.setText(data.getTitle());
        bindingView.tvNumber.setText(data.getBeanAmount());
        bindingView.tvMoney.setText(data.getAmount());
        bindingView.tvName.setText(data.getName());
        bindingView.tvPhone.setText(data.getContactMobile());
        bindingView.tvWx.setText(data.getWechat());
        bindingView.tvCode.setText(data.getOrderNo());
        bindingView.tvOrderTime.setText(data.getOrderTime());
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
                bindingView.cardName.setText(data.getName());
                bindingView.cardType.setText(data.getBankName() + " (" + data.getBanckBranchName() + ")");
                bindingView.cardCode.setText(data.getCardNo());
                break;
        }
    }


    /**
     * 待付款
     */
    public void dfk() {
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

        bindingView.llTitleDfk.setVisibility(View.VISIBLE);
        //剩余时间
        bindingView.tvTime.setText("");
        //底部
        bindingView.rlBottom.setVisibility(View.VISIBLE);
        //取消订单 我已付款
        bindingView.btnQxdd.setVisibility(View.VISIBLE);
        bindingView.btnYfk.setVisibility(View.VISIBLE);
    }

    /**
     * 已付款
     */
    public void yfk() {
        bindingView.llTitleYfk.setVisibility(View.VISIBLE);
        //底部
        bindingView.rlBottom.setVisibility(View.VISIBLE);
        bindingView.tvSs.setVisibility(View.VISIBLE);
        bindingView.btnWyss.setVisibility(View.VISIBLE);
    }

    /**
     * 已完成
     */
    public void ywc() {
        bindingView.llTitleYwc.setVisibility(View.VISIBLE);
    }

    public static void goPage(Context context, int type, String id) {
        Intent intent = new Intent(context, CaigouInfoActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }
}
