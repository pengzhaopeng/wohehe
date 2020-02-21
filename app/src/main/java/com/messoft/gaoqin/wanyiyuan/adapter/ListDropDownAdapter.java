package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;


import com.messoft.gaoqin.wanyiyuan.databinding.ItemDefaultDropDownBinding;

public class ListDropDownAdapter extends BaseRecyclerViewAdapter<String> {

    private Context context;
    private int checkItemPosition = 0;

    public ListDropDownAdapter(Context context) {
        this.context = context;
    }

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_default_drop_down);
    }


    class ViewHolder extends BaseRecyclerViewHolder<String, ItemDefaultDropDownBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final String data, final int position) {
            if (checkItemPosition != -1) {
                if (checkItemPosition == position) {
                    binding.text.setTextColor(context.getResources().getColor(R.color.drop_down_selected));
                    binding.text .setBackgroundResource(R.color.check_bg);
                } else {
                    binding.text.setTextColor(context.getResources().getColor(R.color.drop_down_unselected));
                    binding.text.setBackgroundResource(R.color.white);
                }
            }
        }
    }
}
