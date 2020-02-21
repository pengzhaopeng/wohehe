package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.OrderItemList;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemOrderBinding;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemOrderShBinding;
import com.messoft.gaoqin.wanyiyuan.model.OrderModel;
import com.messoft.gaoqin.wanyiyuan.ui.my.SqshActivity;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;


/**
 * 包括卖家和买家
 */
public class OrderSHAdapter extends BaseRecyclerViewAdapter<OrderItemList> {

    private BaseActivity mActivity;
    private OrderModel mModel;

    public OrderSHAdapter(BaseActivity activity) {
        mActivity = activity;
        mModel = new OrderModel();
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_order_sh);
    }

    class ViewHolder extends BaseRecyclerViewHolder<OrderItemList, ItemOrderShBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final OrderItemList data, final int position) {

            if (data != null) {
                //公共字段
                binding.tvTime.setText(data.getCreateTime());

                //订单号
                binding.tvOrderNumber.setText("订单号：" + data.getOrderCode());

                String img = Constants.MASTER_URL + "wyy/img/pimg/" + data.getProductId() + "/p1_200_200.jpg";
                ImgLoadUtil.displayEspImage(img, binding.iv, 1);
                binding.tvName.setText(data.getProductName());
                binding.tvNumber.setText("x" + data.getSkuQty());
                binding.tvPrice.setText(data.getSkuPrice());
                //subStatus	Long 售后处理状态 0:未申请 1:审核中 2:审核驳回, 3:退款中 4.已退款
                String subStatus = data.getSubStatus();
                switch (subStatus) {
                    case "0":
//                            binding.tvStatus.setText("");
                        binding.btnSqsh.setVisibility(View.VISIBLE);
                        binding.btnShz.setVisibility(View.GONE);
                        break;
                    case "1":
                        binding.btnSqsh.setVisibility(View.GONE);
                        binding.btnShz.setVisibility(View.VISIBLE);
                        binding.btnShz.setText("审核中");
                        break;
                    case "2":
                        binding.btnSqsh.setVisibility(View.GONE);
                        binding.btnShz.setVisibility(View.VISIBLE);
                        binding.btnShz.setText("审核驳回");
                        break;
                    case "3":
                        binding.btnSqsh.setVisibility(View.GONE);
                        binding.btnShz.setVisibility(View.VISIBLE);
                        binding.btnShz.setText("退款中");
                        break;
                    case "4":
                        binding.btnSqsh.setVisibility(View.GONE);
                        binding.btnShz.setVisibility(View.VISIBLE);
                        binding.btnShz.setText("已退款");
                        break;
                    case "5":
                        binding.btnSqsh.setVisibility(View.GONE);
                        binding.btnShz.setVisibility(View.VISIBLE);
                        binding.btnShz.setText("换货完成");
                        break;
                }

                //点击事件
                //申请售后
                binding.btnSqsh.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        SqshActivity.goPage(mActivity,data.getOrderId(),data.getId(),subStatus,data.getOrderCode(),
                                data.getProductId(),data.getProductName(),data.getSkuQty(),data.getSkuPrice(),data.getSkuAmount());
                    }
                });

            }
        }
    }

    //等待买家付款
    private void sellDfh2(ItemOrderBinding binding) {
        binding.btnDfh.setVisibility(View.VISIBLE);
        binding.btnSszr.setVisibility(View.GONE);
        binding.btnQxzr.setVisibility(View.GONE);
        binding.btnYzr.setVisibility(View.VISIBLE);

    }
}
