package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.MemberCapitalWaitLogList;
import com.messoft.gaoqin.wanyiyuan.bean.MemberWholesaleCapitalList;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemMyBillListBinding;


public class PfedListAdapter extends BaseRecyclerViewAdapter<MemberCapitalWaitLogList> {

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_my_bill_list);
    }


    class ViewHolder extends BaseRecyclerViewHolder<MemberWholesaleCapitalList, ItemMyBillListBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final MemberWholesaleCapitalList data, final int position) {

            binding.tvName.setText(data.getTitle());
            binding.tvTime.setText(data.getCreateTime());
            String amount = data.getAmount();
            String payType = data.getPayType();
            String fh;
            if (payType.equals("0")) {
                fh = "+";
                binding.tvMoney.setTextColor(Color.parseColor("#ff0000"));
            } else {
                fh = "-";
                binding.tvMoney.setTextColor(Color.parseColor("#FF333333"));
            }
            binding.tvMoney.setText(fh + " " + amount);


        }
    }
}
