package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.GetBeanOrderAppealList;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemOrderTsBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.GoldModel;
import com.messoft.gaoqin.wanyiyuan.model.QgModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

/**
 * 我的投诉列表
 */
public class MyTsOrderAdapter extends BaseRecyclerViewAdapter<GetBeanOrderAppealList> {

    private BaseActivity mActivity;
    private QgModel mQgModel;
    private GoldModel mGoldModel;

    public MyTsOrderAdapter(BaseActivity activity) {
        this.mActivity = activity;
        mQgModel = new QgModel();
        mGoldModel = new GoldModel();
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_order_ts);
    }

    class ViewHolder extends BaseRecyclerViewHolder<GetBeanOrderAppealList, ItemOrderTsBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final GetBeanOrderAppealList data, final int position) {
            if (data == null) return;
            binding.tvOrderNumber.setText("订单号 " + data.getOrderNo());
            if (data.getOrderList() != null) {
                binding.tvName.setText(data.getOrderList().getGoodsName());
                binding.tvPrice.setText("￥" + data.getOrderList().getGoodsPrice());
                binding.tv1.setText("产品编号：" + data.getOrderList().getGoodsId());
                if (data.getOrderList().getImgsList() != null && data.getOrderList().getImgsList().size() > 0) {
                    ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getOrderList().getImgsList().get(0)), binding.iv, 1, TimeUtils.date2TimeStamp(data.getUpdateTime()));
                }
            }
            binding.tvMsg.setText(data.getContent());
            binding.tvTime.setText("申诉时间：" + data.getCreateTime());
            String state = data.getState(); //状态(0:待解决,1:已完成)
            if (state.equals("0")) {
                binding.ivStatus.setImageResource(R.drawable.wjj);
                binding.btnRight.setVisibility(View.VISIBLE);
                binding.btnRight.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        DialogWithYesOrNoUtils.getInstance().showDialog(mActivity, "提示", "问题已解决了吗？", "确定","取消", new DialogWithYesOrNoUtils.DialogCallBack() {
                            @Override
                            public void exectEvent(DialogInterface dialog) {
                                dialog.dismiss();
                                mGoldModel.updateAuditBeanOrderAppealForm(mActivity, data.getId(), "1", "1", BusinessUtils.getBindAccountId(),
                                        BusinessUtils.getMobile(), new RequestImpl() {
                                            @Override
                                            public void loadSuccess(Object object) {
                                                data.setState("1");
                                                binding.btnRight.setVisibility(View.GONE);
                                                binding.ivStatus.setImageResource(R.drawable.yjj);
                                            }

                                            @Override
                                            public void loadFailed(int errorCode, String errorMessage) {
                                                ToastUtil.showShortToast(errorMessage);
                                            }
                                        });
                            }

                            @Override
                            public void exectCancel(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });


                    }
                });
            }else{
                binding.ivStatus.setImageResource(R.drawable.yjj);
                binding.btnRight.setVisibility(View.GONE);
            }
        }

    }


}
