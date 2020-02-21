package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.MemberSettlementLis;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemJsBinding;

/**
 * 代理结算
 */
public class JsAdapter extends BaseRecyclerViewAdapter<MemberSettlementLis> {

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_js);
    }


    class ViewHolder extends BaseRecyclerViewHolder<MemberSettlementLis, ItemJsBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final MemberSettlementLis data, final int position) {
            binding.tvTitle.setText(data.getTitle());
            String state = null;
            //状态(0:未激活,1:未结算,2:已结算)
            switch ( data.getState()){
                case "0":
                    state = "未激活";
                    break;
                case "1":
                    state = "未结算";
                    break;
                case "2":
                    state = "已结算";
                    break;
            }
            binding.tvStatus.setText(state);
            binding.tvDate.setText(data.getBeginTime()+"-"+data.getEndTime());
            binding.tvMoney.setText("¥"+data.getSettlemtnAmount());
            binding.tvTime.setText("创建时间"+data.getCreateTime());
        }
    }
}
