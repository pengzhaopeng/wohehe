package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.EvaluationList;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.view.WarpLinearLayout;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemProductCommentBinding;


/**
 * 商品评价
 */
public class ProductCommentAdapter extends BaseRecyclerViewAdapter<EvaluationList> {

    private Context mContext;

    public ProductCommentAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_product_comment);
    }


    class ViewHolder extends BaseRecyclerViewHolder<EvaluationList, ItemProductCommentBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final EvaluationList data, final int position) {
            ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getMemberImgName()),
                    binding.iv, 0);
            binding.tvName.setText(data.getMemberNickName());
            binding.tvTime.setText(data.getCreateTime());
            //评分
            binding.starBar.setIntegerMark(true);
            binding.starBar.setStarMark(5f);
            binding.starBar.setStarBarUnClickable(false);
            if (StringUtils.isNoEmpty(data.getScore())) {
                binding.starBar.setStarMark(Float.parseFloat(data.getScore()));
            }
            //标签
            setTags(binding.tag, data.getEvaluationTagIds(), data.getEvaluationTagNames());
            //内容
            binding.tvContent.setText(data.getContent());
        }
    }

    /**
     * 设置标签
     */
    private void setTags(WarpLinearLayout tag, String evaluationTagIds, String evaluationTagNames) {
        if (!StringUtils.isNoEmpty(evaluationTagNames)) return;
        String[] split = evaluationTagNames.split(",");
        tag.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 3, 6, 3);
        int length = split.length;
        for (int i = 0; i < length; i++) {
            TextView textView = new TextView(mContext);
            textView.setTextSize(12);
            textView.setPadding(12, 4, 12, 4);
            textView.setBackgroundResource(R.drawable.shape_label);
            textView.setTextColor(textView.getResources().getColor(R.color.title1));
            textView.setText(split[i]);
            textView.setLayoutParams(params);
            tag.addView(textView);
        }
    }
}
