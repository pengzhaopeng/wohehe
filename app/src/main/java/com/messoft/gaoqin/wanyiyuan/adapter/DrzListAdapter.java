package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.MemberCapitalWaitLogList;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemMyBillListBinding;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;


public class DrzListAdapter extends BaseRecyclerViewAdapter<MemberCapitalWaitLogList> {

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_my_bill_list);
    }


    class ViewHolder extends BaseRecyclerViewHolder<MemberCapitalWaitLogList, ItemMyBillListBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final MemberCapitalWaitLogList data, final int position) {
            String title = data.getType().equals("0") ? "批发额度返现" : "提货权转让 ";
            binding.tvName.setText(title);
            binding.tvTime.setText(data.getArrivalTime());
            String amount = data.getAmount();
            if (StringUtils.isNoEmpty(amount)) {
                double v = Double.parseDouble(amount);
                String fh = (v > 0) ? "+" : "-";
                binding.tvMoney.setText(fh + " " + v);
            }

        }
    }
}
