package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.GetAreaList;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.AddressModel;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.RegexUtil;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityAddAddressBinding;

import java.util.ArrayList;
import java.util.List;

import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.DataProvider;
import chihane.jdaddressselector.ISelectAble;
import chihane.jdaddressselector.SelectedListener;
import chihane.jdaddressselector.Selector;

import static android.text.TextUtils.isEmpty;

/**
 * @作者 Administrator
 * 新增收货地址
 * @创建日期 2018/3/14 0014 17:35
 */

public class AddAddressActivity extends BaseActivity<ActivityAddAddressBinding> {

    private Selector mSelector;
    private BottomDialog mDialog;
    private String mProvince;
    private String mProvinceText;
    private String mCity;
    private String mCityText;
    private String mDistrict;
    private String mDistrictText;


    private AddressModel mAddressModel;
    private int mType; //0-普通 1-下单界面 2-我的抢购订单补录地址
//    private int postition; //标志从列表那个位置点进来选中地址 比如我的抢购订单补录地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
    }

    @Override
    protected void initSetting() {
        setTitle("新增地址");
        showContentView();

        mAddressModel = new AddressModel();

        if (getIntent() != null) {
            //1-- 下单界面过来
            mType = getIntent().getIntExtra("type", -1);
//            postition = getIntent().getIntExtra("position", -1);
        }
    }

    @Override
    protected void initListener() {
        //选地址
        bindingView.rlSelectCity.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                showAddressDialog();
            }
        });

        //保存
        bindingView.tvSure.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                saveAddress();
            }
        });
    }

    /**
     * 选择地址
     */
    private void showAddressDialog() {
        mSelector = new Selector(getActivity(), 3);
        setAddressData();
        mDialog = new BottomDialog(getActivity());
        mDialog.init(getActivity(), mSelector);
        mDialog.show();
    }

    private void setAddressData() {

        mSelector.setDataProvider(new DataProvider() {
            @Override
            public void provideData(int currentDeep, int preId, final DataReceiver receiver) {
                //根据tab的深度和前一项选择的id，获取下一级菜单项
//                Log.i(TAG, "provideData: currentDeep >>> " + currentDeep + " preId >>> " + preId);
                if (currentDeep == 0) {
                    preId = 0;
                }
                mAddressModel.getAreaList(AddAddressActivity.this, preId + "", new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        List<GetAreaList> streetList = (List<GetAreaList>) object;
                        receiver.send(getDatas(streetList));
                    }

                    @Override
                    public void loadFailed(int errorCode, String errorMessage) {
                        ToastUtil.showShortToast(errorMessage);
                    }
                });
            }
        });
        mSelector.setSelectedListener(new SelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onAddressSelected(ArrayList<ISelectAble> selectAbles) {
                if (selectAbles != null && selectAbles.size() == 3) {
                    if (null != selectAbles.get(0)) {
                        mProvince = selectAbles.get(0).getId() + "";
                        mProvinceText = selectAbles.get(0).getName() + "";
                    }
                    if (null != selectAbles.get(1)) {
                        mCity = selectAbles.get(1).getId() + "";
                        mCityText = selectAbles.get(1).getName() + "";
                    }
                    if (null != selectAbles.get(2)) {
                        mDistrict = selectAbles.get(2).getId() + "";
                        mDistrictText = selectAbles.get(2).getName() + "";
                    } else {
                        mDistrict = null;
                        mDistrictText = null;
                    }
                    bindingView.tvCity.setText(mProvinceText + mCityText + mDistrictText);

                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                }
            }
        });
    }

    private ArrayList<ISelectAble> getDatas(final List<GetAreaList> list) {

        ArrayList<ISelectAble> data = new ArrayList<>();
        for (int j = 0; j < list.size(); j++) {
            final int finalJ = j;
            data.add(new ISelectAble() {
                @Override
                public String getName() {
                    return list.get(finalJ).getTitle();
                }

                @Override
                public int getId() {
                    return list.get(finalJ).getId();
                }

                @Override
                public Object getArg() {
                    return this;
                }
            });
        }
        return data;
    }

    /**
     * 保存地址
     */
    private void saveAddress() {
        String linkName = bindingView.etLinkName.getText().toString().trim();
        String linkPhone = bindingView.etLinkPhone.getText().toString().trim();
        String city = bindingView.tvCity.getText().toString().trim();
//        String street = bindingView.tvStreet.getText().toString().trim();
        String address = bindingView.etAddress.getText().toString().trim();
        if (isEmpty(linkName)) {
            ToastUtil.showShortToast("请填写联系人");
            return;
        }
        if (isEmpty(linkPhone)) {
            ToastUtil.showShortToast("请先填写联系电话");
            return;
        }
        if (!RegexUtil.checkMobile(linkPhone)) {
            ToastUtil.showShortToast("请先填写正确的电话");
            return;
        }

        if (isEmpty(city) || isEmpty(mProvince) ||
                isEmpty(mCity) || isEmpty(mDistrict)) {
            ToastUtil.showShortToast("省市区不能为空");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            ToastUtil.showShortToast("请填写详细地址");
            return;
        }
        int setDefaultAddress = bindingView.cbSetDefault.isChecked() ? 1 : 0;
        mAddressModel.addAddress(AddAddressActivity.this,
                linkName, linkPhone,
                String.valueOf(setDefaultAddress),
                mProvince, mProvinceText,
                mCity, mCityText,
                mDistrict, mDistrictText,
                address,
                new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        ToastUtil.showShortToast("保存成功");
                        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_ADDRESS_LIST, 0));
                        if (mType == 1) {
                            //下单界面过来
                            RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_ORDER_ADDRESS, 1));
                        }
//                        else if(mType==2){//补录地址
//
//                        }
                        finish();
                    }

                    @Override
                    public void loadFailed(int errorCode, String errorMessage) {
                        ToastUtil.showShortToast(errorMessage);
                    }
                });
    }

    /**
     * 是否修改了信息
     *
     * @return
     */
    public boolean isChangeInfo() {
        String etZsName = bindingView.etLinkName.getText().toString().trim();
        String etPhone = bindingView.etLinkPhone.getText().toString().trim();
        String tvCity = bindingView.tvCity.getText().toString().trim();
        String etAddress = bindingView.etAddress.getText().toString().trim();
        if (TextUtils.isEmpty(etZsName) && TextUtils.isEmpty(etPhone) && TextUtils.isEmpty(tvCity)
                && TextUtils.isEmpty(etAddress)) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!isChangeInfo()) {
            super.onBackPressed();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("修改了信息还未保存，确认现在返回吗？");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public static void goPage(Context context, int type) {
        Intent intent = new Intent(context, AddAddressActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    public static void goPage(Context context, int type,int position) {
        Intent intent = new Intent(context, AddAddressActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }
}
