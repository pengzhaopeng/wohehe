package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.MemberGainsWaitLogList;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemMyBillListBinding;
import com.messoft.gaoqin.wanyiyuan.utils.RegexUtil;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;


public class DrzListRightAdapter extends BaseRecyclerViewAdapter<MemberGainsWaitLogList> {

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_my_bill_list);
    }


    class ViewHolder extends BaseRecyclerViewHolder<MemberGainsWaitLogList, ItemMyBillListBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final MemberGainsWaitLogList data, final int position) {
            String title = TimeUtils.timeFormat(data.getCreateTime(),"yyyy年MM月dd号")+ RegexUtil.setFourHideMobile(data.getAccount())+"购买了商品";
            binding.tvName.setText(title);
            binding.tvTime.setText("预计到账时间"+data.getArrivalTime());
            String amount = data.getAmount();
            if (StringUtils.isNoEmpty(amount)) {
                double v = Double.parseDouble(amount);
                String fh = (v > 0) ? "+" : "-";
                binding.tvMoney.setText(fh + " " + v);
            }

        }
    }
}
