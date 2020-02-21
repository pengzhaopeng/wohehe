package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.GetMemberPointsLogList;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemJfDetailBinding;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;

/**
 * 积分明细
 */
public class JfAdapter extends BaseRecyclerViewAdapter<GetMemberPointsLogList> {

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_jf_detail);
    }


    class ViewHolder extends BaseRecyclerViewHolder<GetMemberPointsLogList, ItemJfDetailBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final GetMemberPointsLogList data, final int position) {
            binding.tvTitle.setText(BusinessUtils.subType(data.getSubType()));
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
            binding.tvTime.setText(data.getCreateTime());
            binding.tvNumber.setText("余额："+data.getBalance());
        }
    }
}
