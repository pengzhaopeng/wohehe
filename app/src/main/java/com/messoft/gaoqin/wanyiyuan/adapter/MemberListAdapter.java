package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.MemberInfoList;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemMemberListBinding;

public class MemberListAdapter extends BaseRecyclerViewAdapter<MemberInfoList> {

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_member_list);
    }


    class ViewHolder extends BaseRecyclerViewHolder<MemberInfoList, ItemMemberListBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final MemberInfoList data, final int position) {
            ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getImgName()), binding.iv, 0);
            binding.tvPhone.setText(data.getAccount());
            binding.tvName.setText(data.getName());
            binding.tvLevel.setText(BusinessUtils.memberLevel(data.getRoleId()));
        }
    }
}
