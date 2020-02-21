package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.BeanOrderInfoList;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemGuaShouBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.GoldModel;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

/**
 * 挂售
 */
public class GuaShouAdapter extends BaseRecyclerViewAdapter<BeanOrderInfoList> {

    private BaseActivity activity;
    private GoldModel mGoldModel;


    public GuaShouAdapter(BaseActivity activity) {
        this.activity = activity;
        mGoldModel = new GoldModel();
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_gua_shou);
    }


    class ViewHolder extends BaseRecyclerViewHolder<BeanOrderInfoList, ItemGuaShouBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final BeanOrderInfoList data, final int position) {
            //时间
            binding.tvOrderTime.setText("下单时间："+data.getOrderTime());
            //状态(0:待交易,1:待付款,2:已付款待发货,3:已发货已完成,9:已取消)
            String state = data.getState();
            if (state.equals("0")) { //待交易
                binding.tvName1.setText(data.getTitle());
                binding.rlDjy.setVisibility(View.VISIBLE);
                binding.tvSaleMoney.setText(data.getBeanAmount());
                //(0:微信,1:支付宝,2:银行卡)
                String payType = data.getPayType();
                switch (payType){
                    case "0":
                        binding.ivZfb.setImageResource(R.drawable.wx_36);
                        binding.payWay.setText("微信");
                        break;
                    case "1":
                        binding.ivZfb.setImageResource(R.drawable.zfb_36);
                        binding.payWay.setText("支付宝");
                        break;
                    case "2":
                        binding.ivZfb.setImageResource(R.drawable.yhk_36);
                        binding.payWay.setText("银行卡");
                        break;
                }
                //取消挂售
                binding.btnQxgs.setVisibility(View.VISIBLE);
            }else{//非待交易
                binding.tvOrderNumber.setText(data.getTitle());
                binding.llNoDjy.setVisibility(View.VISIBLE);
                //订单号
                binding.tvOrderNumber.setText("订单号："+data.getOrderNo());
                //金额
                binding.tvGetMoney.setText("￥"+data.getAmount());
                //买房账号
                binding.tvAccount.setText("买方账号："+data.getPurchaserAccount());
                //买房姓名
                binding.tvSaleName.setText("买方姓名："+data.getPurchaserName());

                if (state.equals("2")){ //待放行
                    binding.btnFx.setVisibility(View.VISIBLE);
                }
            }
            binding.btnQxgs.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    mGoldModel.cancelBeanOrderForm(activity, data.getId(), new RequestImpl() {
                        @Override
                        public void loadSuccess(Object object) {
                            remove(position);
                            notifyDataSetChanged();
                            RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_MY_GS_ORDER, 0));
                        }

                        @Override
                        public void loadFailed(int errorCode, String errorMessage) {
                            ToastUtil.showShortToast(errorMessage);
                        }
                    });
                }
            });
            binding.btnFx.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    mGoldModel.deliverBeanOrderForm(activity, data.getId(), new RequestImpl() {
                        @Override
                        public void loadSuccess(Object object) {
                            remove(position);
                            notifyDataSetChanged();
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

    }
}
