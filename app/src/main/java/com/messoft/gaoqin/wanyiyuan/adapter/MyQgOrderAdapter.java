package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.roundview.RoundTextView;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.GetRushOrderList;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemOrderQgBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.QgModel;
import com.messoft.gaoqin.wanyiyuan.ui.home.CommonOrderPayActivity;
import com.messoft.gaoqin.wanyiyuan.ui.market.WoYaoShenShuActivity;
import com.messoft.gaoqin.wanyiyuan.ui.my.AddressActivity;
import com.messoft.gaoqin.wanyiyuan.ui.my.FindFriendActivity;
import com.messoft.gaoqin.wanyiyuan.ui.my.MyQgListActivity;
import com.messoft.gaoqin.wanyiyuan.ui.my.WyjsActivity;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

/**
 * 我的抢购列表
 */
public class MyQgOrderAdapter extends BaseRecyclerViewAdapter<GetRushOrderList> {

    private BaseActivity mActivity;
    private int type;//0-抢购 1-寄售
    private int qg_type;//抢购
    private int js_type;//寄售
    private QgModel mQgModel;

    public MyQgOrderAdapter(BaseActivity activity, int type, int qg_type, int js_type) {
        this.mActivity = activity;
        this.type = type;
        this.qg_type = qg_type;
        this.js_type = js_type;
        mQgModel = new QgModel();
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_order_qg);
    }

    class ViewHolder extends BaseRecyclerViewHolder<GetRushOrderList, ItemOrderQgBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final GetRushOrderList data, final int position) {
            if (data == null) return;
            //抢购和寄售的公共字段
            binding.tvOrderNumber.setText("单号：" + data.getOrderNo());
            binding.tvTime.setText(data.getCreateTime());
            if (data.getImgsList() != null && data.getImgsList().size() > 0) {
                ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getImgsList().get(0)), binding.iv, 1, TimeUtils.date2TimeStamp(data.getUpdateTime()));
            }
            binding.tvName.setText(data.getGoodsName());

//            binding.tv1.setText("产品编号：" + data.getGoodsId());
            if (StringUtils.isNoEmpty(data.getEarningRate())) {
                binding.tv1.setText("产品编号：" + data.getEarningRate());
            }else{
                binding.tv1.setText("产品编号：" + data.getGoodsId());
            }

            binding.tvPrice.setText("￥" + data.getGoodsPrice());
            //抢购 和 寄售
            if (type == 0) {
                setQg(data, position);
            } else if (type == 1) {
                setJs(data, position);
            }
        }

        //设置寄售
        private void setJs(GetRushOrderList data, int position) {
            switch (js_type) {
                case MyQgListActivity.my_js_dsk: //待付款
//                    binding.rlBottom.setVisibility(View.GONE);
                    if (!StringUtils.isNoEmpty(data.getPayType())) {
                        setBtnState(binding.btnRight, View.VISIBLE, "补录收款信息", "#FF9800", "#FF9800");
                        binding.btnRight.setOnClickListener(new PerfectClickListener() {
                            @Override
                            protected void onNoDoubleClick(View v) {
                                WyjsActivity.goPage(mActivity, data.getOrderId(), data.getGoodsId(), data.getGoodsTypeId(), data.getGoodsName(),
                                        data.getShopName(),
                                        data.getShopMobile(),
                                        data.getWechat(),
                                        1);
                            }
                        });
                    } else {
                        setBtnState(binding.btnRight, View.GONE, "补录收款信息", "#FF9800", "#FF9800");
                    }
                    break;
                case MyQgListActivity.my_js_dfx: //待放行
                    binding.rlBottom.setVisibility(View.VISIBLE);
                    //查看是否申述 0:无申诉，1：申诉
                    if (data.getIsVipShop().equals("0")) {
                        setBtnState(binding.btnLeft, View.VISIBLE, "申述", "#FF9800", "#FF9800");
                        binding.btnLeft.setOnClickListener(new PerfectClickListener() {
                            @Override
                            protected void onNoDoubleClick(View v) {
                                //去申述
                                WoYaoShenShuActivity.goPage(mActivity, 1, data.getOrderId(), data.getOrderNo());
                            }
                        });
                    } else {
                        setBtnState(binding.btnLeft, View.VISIBLE, "已申述", "#999999", "#FF666666");
                    }

                    setBtnState(binding.btnRight, View.VISIBLE, "放行", "#FF9800", "#FF9800");
                    binding.btnRight.setOnClickListener(new PerfectClickListener() {
                        @Override
                        protected void onNoDoubleClick(View v) {
                            //去放行
                            DialogWithYesOrNoUtils.getInstance().showDialog(mActivity, "温馨提示", "是否已收到对应货款,确认放行后无法再进行申述,是否确认?", "确认放行", "取消", new DialogWithYesOrNoUtils.DialogCallBack() {
                                @Override
                                public void exectEvent(DialogInterface dialog) {
                                    dialog.dismiss();
                                    //放行
                                    mQgModel.updateRushOrderForm(mActivity,
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
                                                    //把这条记录从列表删除
                                                    remove(position);
                                                    notifyDataSetChanged();
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
                case MyQgListActivity.my_js_ywc: //已完成
                    binding.rlBottom.setVisibility(View.GONE);
                    break;
            }
        }

        //设置抢购
        private void setQg(GetRushOrderList data, int position) {
            switch (qg_type) {
                case MyQgListActivity.my_qg_dfu: //待付款
                    //看是否补录了收货地址
                    setBtnState(binding.btnLeft, -1, "补录收货地址", "#999999", "#FF666666");
                    if (StringUtils.isNoEmpty(data.getProvince())) {
                        binding.btnLeft.setVisibility(View.GONE);
                    } else {
                        binding.btnLeft.setVisibility(View.VISIBLE);
                    }
                    //去补录
                    binding.btnLeft.setOnClickListener(new PerfectClickListener() {
                        @Override
                        protected void onNoDoubleClick(View v) {
                            AddressActivity.goPage(mActivity, 2, position);
                        }
                    });

                    String distributionStatus = data.getDistributionStatus();
                    if (distributionStatus.equals("0")) { //未转让
                        setBtnState(binding.btnRight, View.VISIBLE, "转让订单", "#999999", "#FF666666");
                        //转让订单 经补录收货地址的订单不可以点击订单转让
                        binding.btnRight.setOnClickListener(new PerfectClickListener() {
                            @Override
                            protected void onNoDoubleClick(View v) {
                                if (StringUtils.isNoEmpty(data.getProvince())) {
                                    ToastUtil.showLongToast("已补录收货地址的订单不可以转让");
                                    return;
                                }
                                FindFriendActivity.goPage(mActivity, 1, data);
                            }
                        });
                    } else {
                        setBtnState(binding.btnRight, View.VISIBLE, "已转让", "#FF9800", "#FF9800");
                    }

                    //显示我已付款 待付款都显示 点了就改状态
                    setBtnState(binding.btnRight1, View.VISIBLE, "我已付款", "#FF9800", "#FF9800");
                    binding.btnRight1.setOnClickListener(new PerfectClickListener() {
                        @Override
                        protected void onNoDoubleClick(View v) {
                            //未补录收货地址的订单不可以付款
                            if (!StringUtils.isNoEmpty(data.getProvince())) {
                                ToastUtil.showLongToast("未补录收货地址的订单不可以付款");
                                return;
                            }
                            DialogWithYesOrNoUtils.getInstance().showDialog(mActivity, "温馨提示", "是否已转账给对方账户应付金额？", "我已付款", "未转账", new DialogWithYesOrNoUtils.DialogCallBack() {
                                @Override
                                public void exectEvent(DialogInterface dialog) {
                                    dialog.dismiss();
                                    //我已付款
                                    mQgModel.updateRushOrderForm(mActivity,
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
                                                    //把这条记录从列表删除
                                                    remove(position);
                                                    notifyDataSetChanged();
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
                    binding.btnRight1.setVisibility(View.GONE);
                    setBtnState(binding.btnLeft, View.VISIBLE, "等待放行", "#999999", "#FFBBBBBB");

                    //查看是否申述 0:无申诉，1：申诉
                    if (data.getIsVipMember().equals("0")) {
                        setBtnState(binding.btnRight, View.VISIBLE, "申述", "#FF9800", "#FF9800");
                        binding.btnRight.setOnClickListener(new PerfectClickListener() {
                            @Override
                            protected void onNoDoubleClick(View v) {
                                //去申述
                                WoYaoShenShuActivity.goPage(mActivity, 2, data.getOrderId(), data.getOrderNo());
                            }
                        });
                    } else {
                        setBtnState(binding.btnRight, View.VISIBLE, "已申述", "#999999", "#FF666666");
                    }

                    break;
                case MyQgListActivity.my_qg_ywc: //已完成
                    binding.btnRight1.setVisibility(View.GONE);
                    if (data.getIsRevenue().equals("0")) { //我要提货
                        Long intervalDays = TimeUtils.longTimeToDay1(data.getIntervalDays());
                        Long intervalTimes = TimeUtils.longTimeToDay1(data.getIntervalTimes());
                        Long beginSwapTime = TimeUtils.longTimeToDay1(data.getBeginSwapTime());
                        Long endSwapTime = TimeUtils.longTimeToDay1(data.getEndSwapTime());
                        if (intervalDays >= 0) {
                            if (beginSwapTime >= 0 && endSwapTime <= 0) {
                                //开始判断状态
                                if (data.getStatus().equals("10")) {
                                    //申请置换
                                    setBtnState(binding.btnRight, View.VISIBLE, "申请置换", "#FF9800", "#FF9800");
                                    //申请置换
                                    binding.btnRight.setOnClickListener(new PerfectClickListener() {
                                        @Override
                                        protected void onNoDoubleClick(View v) {
                                            String msg = "申请置换前，需要缴纳 " + (data.getReplacementFee() * 100) + "% 的手续费，金额为￥" + data.getStatusRatio() + "，缴纳后自动置换成功，是否继续？";
                                            DialogWithYesOrNoUtils.getInstance().showDialog(mActivity, "提示", msg, "支付", "取消", new DialogWithYesOrNoUtils.DialogCallBack() {
                                                @Override
                                                public void exectEvent(DialogInterface dialog) {
                                                    CommonOrderPayActivity.goPage(mActivity, data.getOrderId(), null, Double.parseDouble(data.getStatusRatio()),
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
                                    setBtnState(binding.btnRight, View.VISIBLE, "置换完成", "#999999", "#FFBBBBBB");
                                    binding.btnLeft.setVisibility(View.GONE);
                                }

                            } else {
                                switch (data.getStatus()) {
                                    case "12":
                                    case "14":
                                        //置换换成 显示已完成
                                        setBtnState(binding.btnRight, View.VISIBLE, "置换完成", "#999999", "#FFBBBBBB");
                                        binding.btnLeft.setVisibility(View.GONE);
                                        break;
                                    case "10":
                                        //申请置换变灰
                                        setBtnState(binding.btnRight, View.VISIBLE, "申请置换", "#999999", "#FFBBBBBB");

                                        break;
                                    default:
                                        //等待置灰 显示等待置换
                                        setBtnState(binding.btnRight, View.VISIBLE, "等待置换", "#999999", "#FFBBBBBB");
                                        break;
                                }

                            }
                        }

                        if (intervalTimes <= 0) {
                            if (data.getStatus().equals("12") || data.getStatus().equals("14")) {
                                binding.btnLeft.setVisibility(View.GONE);
                            } else {
                                if(data.getStatus().equals("10")) {
                                    //申请置换变灰
                                    setBtnState(binding.btnRight, View.VISIBLE, "申请置换", "#999999", "#FFBBBBBB");
                                }
                                //可以申请提货
                                setBtnState(binding.btnLeft, View.VISIBLE, "我要提货", "#FF9800", "#FF9800");
                                binding.btnLeft.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        DialogWithYesOrNoUtils.getInstance().showDialog(mActivity, "温馨提示", "确认提货吗", "收货", "取消", new DialogWithYesOrNoUtils.DialogCallBack() {
                                            @Override
                                            public void exectEvent(DialogInterface dialog) {
                                                mQgModel.updateRushOrderForm(mActivity,
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

                                                                setBtnState(binding.btnLeft, View.VISIBLE, "已提货", "#999999", "#FFBBBBBB");
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
                            setBtnState(binding.btnLeft, View.GONE, "我要提货", "#FF9800", "#FF9800");
                        }

                    } else if (data.getIsRevenue().equals("1")) { //已提货
                        //显示已完成
                        setBtnState(binding.btnLeft, View.VISIBLE, "已完成提货", "#999999", "#FFBBBBBB");
                        binding.btnRight.setVisibility(View.GONE);
                    }

                    break;
            }
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

}
