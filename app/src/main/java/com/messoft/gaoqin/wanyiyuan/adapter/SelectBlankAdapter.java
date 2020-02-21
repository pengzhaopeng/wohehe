package com.messoft.gaoqin.wanyiyuan.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.DictionaryList;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemSelectStreetBinding;


/**
 * Created by Administrator on 2017/7/26 0026.
 *
 */

public class SelectBlankAdapter extends BaseRecyclerViewAdapter<DictionaryList> {
    private Activity activity;

    public SelectBlankAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_select_street);
    }

    class ViewHolder extends BaseRecyclerViewHolder<DictionaryList, ItemSelectStreetBinding> {


        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(final DictionaryList object, final int position) {
            if (object != null) {
                /**
                 * 当数据改变时，binding会在下一帧去改变数据，如果我们需要立即改变，就去调用executePendingBindings方法。
                 */
                binding.executePendingBindings();
                binding.tvTitle.setText(object.getName());
            }
        }
    }
}
