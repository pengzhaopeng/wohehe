package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.OrderItemList;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;


import com.messoft.gaoqin.wanyiyuan.databinding.ItemPfqOrderBinding;
public class PFQOrderAdapter extends BaseRecyclerViewAdapter<OrderItemList> {

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_pfq_order);
    }


    class ViewHolder extends BaseRecyclerViewHolder<OrderItemList, ItemPfqOrderBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final OrderItemList data, final int position) {
            String img = Constants.MASTER_URL + "wyy/img/pimg/" + data.getProductId() + "/p1_200_200.jpg";
            ImgLoadUtil.displayEspImage(img, binding.iv, 1);
            binding.tvOrderNumber.setText(data.getOrderCode());
            binding.tvName.setText(data.getProductName());
            binding.tvNumber.setText("x" + data.getSkuQty());
            binding.tvPrice.setText(data.getSkuPrice());
            binding.tvTime.setText("下单时间："+data.getCreateTime());
        }
    }
}
