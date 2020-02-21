package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.MemberCapitalLogList;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemWalletDetailBinding;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;

public class WalletDetailAdapter extends BaseRecyclerViewAdapter<MemberCapitalLogList> {

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_wallet_detail);
    }


    class ViewHolder extends BaseRecyclerViewHolder<MemberCapitalLogList, ItemWalletDetailBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final MemberCapitalLogList data, final int position) {
            binding.tv1.setText(BusinessUtils.capitalType(data.getType()));
            String payType = data.getPayType();
            String fh;
            if (payType.equals("0")) {
                fh = "+";
                binding.tvMoney.setTextColor(Color.parseColor("#ff0000"));
            } else {
                fh = "-";
                binding.tvMoney.setTextColor(Color.parseColor("#FF333333"));
            }
            binding.tvMoney.setText(fh + " " + data.getAmount());

            binding.tv3.setText(data.getBalance());
            binding.tv4.setText(data.getCreateTime());
        }
    }
}
