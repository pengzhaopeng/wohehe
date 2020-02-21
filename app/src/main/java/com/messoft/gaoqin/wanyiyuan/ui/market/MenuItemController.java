package com.messoft.gaoqin.wanyiyuan.ui.market;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.bean.BeanOrderInfoList;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.GoldModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

import java.util.List;


/**
 * Created by ${chenyn} on 2017/4/9.
 */

public class MenuItemController implements View.OnClickListener {

    private GoldModel mGoldModel;
    private MarketFragment mFragment;

    public MenuItemController(MarketFragment fragment) {
        this.mFragment = fragment;
        mGoldModel = new GoldModel();
    }

    //会话界面的加号
    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_sm: //售卖
                //先查询是否存在未完成的订单
                mFragment.dismissPopWindow();
                checkWwcOrder();
                break;
            case R.id.ll_cg: //采购
                mFragment.dismissPopWindow();
                SysUtils.startActivity(mFragment.getActivity(), MyCaiGouListActivity.class);
                break;
            case R.id.ll_gs: //挂售
                mFragment.dismissPopWindow();
                SysUtils.startActivity(mFragment.getActivity(), MyGuaShouListActivity.class);
                break;
            default:
                break;
        }

    }

    /**
     * 查询本人未完成的订单
     */
    private void checkWwcOrder() {
        mGoldModel.getMySaleBeanOrderList(mFragment.getActivityOne(),
                BusinessUtils.getMobile(), null, "0,1,2", null, null,
                0, HttpUtils.per_page_20, new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        List<BeanOrderInfoList> data = (List<BeanOrderInfoList>) object;
                        if (data != null && data.size() > 0) {
                            DialogWithYesOrNoUtils.getInstance().showDialog(mFragment.getActivityOne(),
                                    "", "您当前有未完成的订单，不能新增挂售，是否前往操作？",
                                    "前往操作", "取消", new DialogWithYesOrNoUtils.DialogCallBack() {
                                        @Override
                                        public void exectEvent(DialogInterface dialog) {
                                            SysUtils.startActivity(mFragment.getActivityOne(), MyGuaShouListActivity.class);
                                        }

                                        @Override
                                        public void exectCancel(DialogInterface dialog) {
                                            dialog.dismiss();
                                        }
                                    });
                        } else {
                            SysUtils.startActivity(mFragment.getActivity(), WoYaoShouMaiActivity.class);
                        }
                    }

                    @Override
                    public void loadFailed(int errorCode, String errorMessage) {
                        ToastUtil.showShortToast(errorMessage);

                    }
                });
    }
}
