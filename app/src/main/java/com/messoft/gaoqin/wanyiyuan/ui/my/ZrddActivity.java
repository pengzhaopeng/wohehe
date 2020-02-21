package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.GetRushOrderList;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.bean.TransferMemberInfo;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityZrddBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.QgModel;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

/**
 * 转让订单
 */
public class ZrddActivity extends BaseActivity<ActivityZrddBinding> {

    private TransferMemberInfo mPersonInfo;
    private GetRushOrderList mOrderBean;
    private QgModel mQgModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zrdd);
    }

    @Override
    protected void initSetting() {
        setTitle("转让信息");
        showContentView();
        mQgModel = new QgModel();
        if (getIntent() != null) {
            mOrderBean = (GetRushOrderList) getIntent().getSerializableExtra("bean");
            mPersonInfo = (TransferMemberInfo) getIntent().getSerializableExtra("login");
            if (mPersonInfo != null) {
                ImgLoadUtil.displayEspImage(SysUtils.getImgURL(mPersonInfo.getImgName()), bindingView.iv, 0);
                bindingView.tvName.setText(mPersonInfo.getName());
                bindingView.tvPhone.setText(mPersonInfo.getAccount());
            }

            if (mOrderBean == null) return;
            bindingView.tvOrderNo.setText(mOrderBean.getOrderNo());
            bindingView.tvProductName.setText(mOrderBean.getGoodsName());
        }
    }

    @Override
    protected void initListener() {
        bindingView.tvNext.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                mQgModel.updateRushOrderCall(getActivity(),
                        mOrderBean.getGoodsTypeId(),
                        mOrderBean.getOrderId(),
                        mPersonInfo.getMemberId(),
                        mPersonInfo.getAccount(),
                        "1",
                        new RequestImpl() {
                            @Override
                            public void loadSuccess(Object object) {
                                ToastUtil.showShortToast("转增成功");
                                //刷新列表
                                RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_QG_LIST, 0));
                                finish();
                            }

                            @Override
                            public void loadFailed(int errorCode, String errorMessage) {
                                ToastUtil.showShortToast(errorMessage);
                            }
                        });
            }
        });
    }

    public static void goPage(Context context, GetRushOrderList bean, TransferMemberInfo login) {
        Intent intent = new Intent(context, ZrddActivity.class);
        intent.putExtra("bean",bean);
        intent.putExtra("login",login);
        context.startActivity(intent);
    }
}
