package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemTestBinding;

public class TestAdapter extends BaseRecyclerViewAdapter<String> {

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_test);
    }


    class ViewHolder extends BaseRecyclerViewHolder<String, ItemTestBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final String data, final int position) {
            binding.tv.setText(data);
        }
    }
}
