package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.MemberSettlementLineList;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemMyBillListBinding;

public class FwmxListAdapter extends BaseRecyclerViewAdapter<MemberSettlementLineList> {

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_my_bill_list);
    }


    class ViewHolder extends BaseRecyclerViewHolder<MemberSettlementLineList, ItemMyBillListBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final MemberSettlementLineList data, final int position) {
            //类型：0.商品销售 1.管理费
            String title = (data.getType().equals("0"))?"商品销售":"管理费";
            binding.tvName.setText(title);
            binding.tvTime.setText(data.getCreateTime());
            String amount = data.getAmount();
            if (StringUtils.isNoEmpty(amount)) {
                double v = Double.parseDouble(amount);
                String fh = (v > 0) ? "+" : "-";
                binding.tvMoney.setText(fh + " " + v);
            }
        }
    }
}
