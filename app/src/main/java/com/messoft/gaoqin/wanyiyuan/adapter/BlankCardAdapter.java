package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.BlankCard;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemBlankCardBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.BlankCardModel;
import com.messoft.gaoqin.wanyiyuan.ui.my.EditBlankCardActivity;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;


public class BlankCardAdapter extends BaseRecyclerViewAdapter<BlankCard> {

    private BaseActivity activity;
    private BlankCardModel mModel;

    private boolean isEdit;

    public BlankCardAdapter(BaseActivity activity) {
        this.activity = activity;
        this.mModel = new BlankCardModel();
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_blank_card);
    }


    class ViewHolder extends BaseRecyclerViewHolder<BlankCard, ItemBlankCardBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final BlankCard data, final int position) {
            binding.cardName1.setText(data.getName());
            binding.cardType1.setText(data.getTypeName() + " (" + data.getBranchName() + ")");
            binding.tvCode.setText(data.getCardNo());
            //图标
            if (StringUtils.isNoEmpty(data.getTypeCode())) {
                int[] blankBgAndLog = BusinessUtils.getBlankBgAndLog(data.getTypeCode());
                binding.ivBg.setImageResource(blankBgAndLog[0]);
                binding.ivLogo.setImageResource(blankBgAndLog[1]);
            }
            final String dft = data.getIsDefault();
            if (dft.equals("1")) {
                binding.cbSetDefault.setChecked(true);
                binding.cbSetDefault.setText("默认银行卡");
            } else {
                binding.cbSetDefault.setChecked(false);
                binding.cbSetDefault.setText("设为默认银行卡");
            }

            if (isEdit) {
                //管理状态
                binding.cbSetDefault.setVisibility(View.GONE);
                binding.tvDelete.setVisibility(View.VISIBLE);
                binding.tvEdit.setVisibility(View.VISIBLE);
            } else {
                //完成状态
                binding.cbSetDefault.setVisibility(View.VISIBLE);
                binding.tvDelete.setVisibility(View.GONE);
                binding.tvEdit.setVisibility(View.GONE);
            }

            //默认
            binding.cbSetDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && (!dft.equals("1"))) {
                        setDefault(data.getId());
                    }
                }
            });
            //删除
            binding.tvDelete.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("确定删除此银行卡？");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleted(data.getId());
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            //编辑
            binding.tvEdit.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    EditBlankCardActivity.goPage(activity, 1, data);

                }
            });
        }
    }


    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
    }

    /**
     * 删除
     *
     * @param id
     */
    private void deleted(String id) {
        mModel.delMemberBankForm(activity, id, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                ToastUtil.showShortToast("删除成功");
                RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_BLANK_CARD, 0));
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    /**
     * 设置默认
     *
     * @param id
     */
    private void setDefault(String id) {
        mModel.updateMemberBankForm(activity, id, null, null, null, null, null,
                "1",
                new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        ToastUtil.showShortToast("设置成功");
                        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_BLANK_CARD, 0));
                    }

                    @Override
                    public void loadFailed(int errorCode, String errorMessage) {
                        ToastUtil.showShortToast(errorMessage);
                    }
                });

    }
}
