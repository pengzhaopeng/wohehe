package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.AddressBean;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemAddressListBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.AddressModel;
import com.messoft.gaoqin.wanyiyuan.ui.my.EditAddressActivity;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;


/**
 * Created by Administrator on 2017/7/26 0026.
 */

public class AddressListAdapter extends BaseRecyclerViewAdapter<AddressBean> {
    private BaseActivity activity;
    private AddressModel mModel;

//    private int old = -1;
//    private SparseBooleanArray selected;

    public AddressListAdapter(BaseActivity activity) {
        this.activity = activity;
//        selected = new SparseBooleanArray();
        mModel = new AddressModel();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_address_list);
    }

    class ViewHolder extends BaseRecyclerViewHolder<AddressBean, ItemAddressListBinding> {


        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(final AddressBean object, final int position) {
            if (object != null) {

                binding.executePendingBindings();
                binding.tvName.setText(object.getContactName());
                binding.tvPhone.setText(object.getContactMobile());
                @SuppressLint("StringFormatMatches") String address = String.format(activity.getResources().getString(R.string.address_info),
                        object.getProvinceText(),
                        object.getCityText(),
                        object.getDistrictText(),
                        object.getAddress());
                binding.tvAddressDesc.setText(address);
                final String dft = object.getIsDefault();
                if (dft.equals("1")) {
                    binding.cbSetDefault.setChecked(true);
                    binding.cbSetDefault.setText("默认收货地址");
                } else {
                    binding.cbSetDefault.setChecked(false);
                    binding.cbSetDefault.setText("设为默认地址");
                }
//                if (selected.get(position)) {
//                    binding.cbSetDefault.setChecked(true);
//                    binding.cbSetDefault.setText("默认收货地址");
//                } else {
//                    binding.cbSetDefault.setChecked(false);
//                    binding.cbSetDefault.setText("设为默认地址");
//                }

                //默认
                binding.cbSetDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked && (!dft.equals("1"))) {
                            setDefaultAddress(object.getId());
                        }
                    }
                });
                //删除
                binding.tvAddressDelete.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setMessage("确定删除此地址？");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletedAddress(object.getId());
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
                //编辑
                binding.tvAddressEdit.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", object);
                        SysUtils.startActivity(activity, EditAddressActivity.class, bundle);
                    }
                });
            }
        }
    }


//    public void setSelectedItem(int item) {
//        if (old != -1) {
//            this.selected.put(old, false);
//        }
//        this.selected.put(item, true);
//        old = item;
//    }

    private void deletedAddress(String aid) {
        mModel.deleteAddress(activity, aid, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                ToastUtil.showShortToast("删除成功");
//                setSelectedItem(position);
//                notifyDataSetChanged();
                RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_ADDRESS_LIST, 0));
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    private void setDefaultAddress(String aid) {
        mModel.updateMemberAddressForm(activity,
                aid,
                null,null,
                "1",
                null,null,null,null,null,null,null,
                new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                ToastUtil.showShortToast("设置成功");
//                setSelectedItem(position);
//                notifyDataSetChanged();
                RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_ADDRESS_LIST, 0));
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

}
