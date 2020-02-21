package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.Html;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.GetMemberNotifyList;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemLifeNewsListBinding;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;


/**
 * Created by Administrator on 2017/7/26 0026.
 * 民生头条消息
 */

public class LifeNewsListAdapter extends BaseRecyclerViewAdapter<GetMemberNotifyList> {
    private Activity activity;

    public LifeNewsListAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_life_news_list);
    }

    class ViewHolder extends BaseRecyclerViewHolder<GetMemberNotifyList, ItemLifeNewsListBinding> {


        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final GetMemberNotifyList object, final int position) {
            if (object != null) {
                /**
                 * 当数据改变时，binding会在下一帧去改变数据，如果我们需要立即改变，就去调用executePendingBindings方法。
                 */
                binding.executePendingBindings();
                String type = object.getType(); //消息类型 1:公告 Announce，2:提醒 Remind,3:信息 Message

                if (type.equals("1")) {
                    String title = String.format("<font color=\"#969595\">%s</font>", "公告");
                    binding.tvName.setText("[" + Html.fromHtml(title) + "]");
//                    ImgLoadUtil.displayEspImage(R.drawable.inform_48, binding.ivHeader);
                } else {
//                    ImgLoadUtil.displayCircle(binding.ivHeader,object.getImgName());
//                    String title = String.format("<font><big>%s</big></font> <font color=\"#969595\">%s</font>",
//                            object.getNickName(), "·"+object.getActionName());
                    String title = String.format("<font><big>%s</big></font>",
                            object.getActionName());
                    binding.tvName.setText("[" + Html.fromHtml(title) + "]");
                }
                binding.tvContent.setText(object.getContent());
                binding.tvTime.setText(TimeUtils.friendly_time(object.getNotifyCreateTime()));
            }
        }
    }
}
