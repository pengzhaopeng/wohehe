package com.messoft.gaoqin.wanyiyuan.adapter;

import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.HomeMenu;
import com.messoft.gaoqin.wanyiyuan.utils.DebugUtil;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemHomeMenuBinding;

/**
 * Created by Administrator on 2017/7/26 0026.
 *
 */

public class HomeMenuAdapter extends BaseRecyclerViewAdapter<HomeMenu> {

    private BaseActivity activity;

    public HomeMenuAdapter(BaseActivity activity) {
        DebugUtil.debug("HomeMenuAdapter","new 了个ServeInServeAdapter");
        this.activity = activity;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_home_menu);
    }

    class ViewHolder extends BaseRecyclerViewHolder<HomeMenu, ItemHomeMenuBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(final HomeMenu object, final int position) {
            if (object != null) {
                /**
                 * 当数据改变时，binding会在下一帧去改变数据，如果我们需要立即改变，就去调用executePendingBindings方法。
                 */
                binding.executePendingBindings();
                binding.tv.setText(object.getMenuName());
                ImgLoadUtil.displayEspImage(object.getMenuRsid(),binding.iv,0);
            }
        }
    }
}
