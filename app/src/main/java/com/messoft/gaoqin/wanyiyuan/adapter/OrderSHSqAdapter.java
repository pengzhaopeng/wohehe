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
import com.messoft.gaoqin.wanyiyuan.bean.ReturnApplyList;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemOrderBinding;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemOrderShSqBinding;
import com.messoft.gaoqin.wanyiyuan.model.OrderModel;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;


/**
 *
 */
public class OrderSHSqAdapter extends BaseRecyclerViewAdapter<ReturnApplyList> {

    private BaseActivity mActivity;
    private OrderModel mModel;

    public OrderSHSqAdapter(BaseActivity activity) {
        mActivity = activity;
        mModel = new OrderModel();
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_order_sh_sq);
    }

    class ViewHolder extends BaseRecyclerViewHolder<ReturnApplyList, ItemOrderShSqBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final ReturnApplyList data, final int position) {

            if (data != null) {
                //公共字段
                binding.tvTime.setText(data.getCreateTime());

                //订单号
                binding.tvOrderNumber.setText("订单号：" + data.getOrderCode());

                String img = Constants.MASTER_URL + "wyy/img/pimg/" + data.getProductId() + "/p1_200_200.jpg";
                ImgLoadUtil.displayEspImage(img, binding.iv, 1);
                binding.tvName.setText(data.getProductName());
                binding.tvNumber.setText("x" + data.getReturnQty());
                //类型
                //金额
                if (data.getType().equals("0")) {
                    binding.tvPrice.setText("退货退款 ");
                    binding.tvMoney.setVisibility(View.VISIBLE);
                    binding.tvMoney.setText("退款金额：¥"+data.getAmount());
                }else{
                    binding.tvPrice.setText("换货");
                    binding.tvMoney.setVisibility(View.GONE);
                }
                //subStatus	Long 售后处理状态 0:未申请 1:审核中 2:审核驳回, 3:退款中 4.已退款
                String subStatus = data.getSubStatus();
                switch (subStatus) {
                    case "0":
                        binding.tvStatus.setText("未申请");
                        break;
                    case "1":
                        binding.tvStatus.setText("审核中");
                        break;
                    case "2":
                        binding.tvStatus.setText("审核驳回");
                        break;
                    case "3":
                        binding.tvStatus.setText("退款中");
                        break;
                    case "4":
                        binding.tvStatus.setText("已退款");
                        break;
                    case "5":
                        binding.tvStatus.setText("换货完成");
                        break;

                }
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
