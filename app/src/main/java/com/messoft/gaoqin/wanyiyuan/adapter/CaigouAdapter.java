package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.BeanOrderInfoList;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemCaigouBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.GoldModel;
import com.messoft.gaoqin.wanyiyuan.ui.market.WoYaoShenShuActivity;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

/**
 * 采购
 */
public class CaigouAdapter extends BaseRecyclerViewAdapter<BeanOrderInfoList> {

    private BaseActivity activity;
    private GoldModel mGoldModel;


    public CaigouAdapter(BaseActivity activity) {
        this.activity = activity;
        mGoldModel = new GoldModel();
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_caigou);
    }


    class ViewHolder extends BaseRecyclerViewHolder<BeanOrderInfoList, ItemCaigouBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final BeanOrderInfoList data, final int position) {
            binding.tvTitle.setText(data.getTitle());
            //订单号
            binding.tvOrderNumber.setText("订单号：" + data.getOrderNo());
            //金额
            binding.tvPayNumber.setText("￥" + data.getAmount());
            //买房账号
            binding.tvAccount.setText("对方账号：" + data.getAccount());
            //买房姓名
            binding.tvName.setText("对方姓名：" + data.getName());
            //(0:微信,1:支付宝,2:银行卡)
            String payType = data.getPayType();
            switch (payType) {
                case "0":
                    binding.tvPayWay.setText("收款方式：微信");
                    break;
                case "1":
                    binding.tvPayWay.setText("收款方式：支付宝");
                    break;
                case "2":
                    binding.tvPayWay.setText("收款方式：银行卡");
                    break;
            }
            //状态(0:待交易,1:待付款,2:已付款待发货,3:已发货已完成,9:已取消)
            String state = data.getState();
            switch (state) {
                case "1":
                    dfk(binding);
                    break;
                case "2":
                    yfk(binding);
                    break;
                case "3":
                    ywc(binding);
                    break;
            }
            //取消订单
            binding.btnQxdd.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    DialogWithYesOrNoUtils.getInstance().showDialog(activity,
                            "", "确认取消此订单？",
                            "确认取消", "我再想想", new DialogWithYesOrNoUtils.DialogCallBack() {
                                @Override
                                public void exectEvent(DialogInterface dialog) {
                                    mGoldModel.cancelBeanOrderForm(activity, data.getId(), new RequestImpl() {
                                        @Override
                                        public void loadSuccess(Object object) {
                                            remove(position);
                                            notifyDataSetChanged();
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
            binding.btnYfk.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    DialogWithYesOrNoUtils.getInstance().showDialog(activity,
                            "", "确定在线下已付款？",
                            "确定", "取消", new DialogWithYesOrNoUtils.DialogCallBack() {
                                @Override
                                public void exectEvent(DialogInterface dialog) {
                                    mGoldModel.payBeanOrderForm(activity, data.getId(), new RequestImpl() {
                                        @Override
                                        public void loadSuccess(Object object) {
                                            remove(position);
                                            notifyDataSetChanged();
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
            //我要申述
            binding.btnWyss.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    WoYaoShenShuActivity.goPage(activity, 0,data.getId(), data.getOrderNo());
                }
            });
        }
    }

    //待付款
    private void dfk(ItemCaigouBinding binding) {
        binding.btnQxdd.setVisibility(View.VISIBLE);
        binding.btnYfk.setVisibility(View.VISIBLE);
        binding.btnWyss.setVisibility(View.GONE);
    }

    //已付款
    private void yfk(ItemCaigouBinding binding) {
        binding.btnQxdd.setVisibility(View.GONE);
        binding.btnYfk.setVisibility(View.GONE);
        binding.btnWyss.setVisibility(View.VISIBLE);
    }

    //已完成
    private void ywc(ItemCaigouBinding binding) {
        binding.btnQxdd.setVisibility(View.GONE);
        binding.btnYfk.setVisibility(View.GONE);
        binding.btnWyss.setVisibility(View.GONE);
    }
}
