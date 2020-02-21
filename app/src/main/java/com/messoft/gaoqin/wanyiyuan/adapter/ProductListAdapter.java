package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.ProductInfo;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemProductBinding;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;

/**
 * 商品列表
 */
public class ProductListAdapter extends BaseRecyclerViewAdapter<ProductInfo> {

    private Context mContext;

    public ProductListAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_product);
    }


    class ViewHolder extends BaseRecyclerViewHolder<ProductInfo, ItemProductBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final ProductInfo data, final int position) {
            if (data != null) {
                /**
                 * 当数据改变时，binding会在下一帧去改变数据，如果我们需要立即改变，就去调用executePendingBindings方法。
                 */
                binding.executePendingBindings();

                RelativeLayout.LayoutParams bannerParams = (RelativeLayout.LayoutParams) binding.iv.getLayoutParams();
                bannerParams.width = DensityUtils.getScreenWidth(mContext) / 2 - 20;
                bannerParams.height = DensityUtils.getScreenWidth(mContext) / 2 - 20;
                binding.iv.setLayoutParams(bannerParams);

                // http://mstest-image.mesandbox.com/wyy/img/pimg/22/p1_200_200.jpg   商品列表
                String img = Constants.MASTER_URL + "wyy/img/pimg/" + data.getId() + "/p1_600_600.jpg";

                ImgLoadUtil.displayEspImage(img, binding.iv, 1, TimeUtils.date2TimeStamp(data.getUpdateTime()));

                binding.tvName.setText(data.getName());
//                binding.tvPrice.setText(data.getPrice());
                BusinessUtils.setPriceAndPoints(binding.tvPrice,13,data.getPoints(),Double.parseDouble(data.getPrice()));
                binding.tvNumber.setText("月销" + data.getBuys() + "件");
            }
        }
    }
}
