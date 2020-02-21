package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.ReturnApplyList;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityOrderShSqInfoBinding;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 售后申请列表详情
 */
public class OrderShSqInfoActivity extends BaseActivity<ActivityOrderShSqInfoBinding> {

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sh_sq_info);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initSetting() {
        showContentView();
        if (getIntent() != null) {
            ReturnApplyList data = (ReturnApplyList) getIntent().getSerializableExtra("data");
            //商品
            String img = Constants.MASTER_URL + "wyy/img/pimg/" + data.getProductId() + "/p1_200_200.jpg";
            ImgLoadUtil.displayEspImage(img, bindingView.iv, 1);
            bindingView.tvTime.setText(data.getCreateTime());
            bindingView.tvTitle.setText(data.getProductName());
            bindingView.tvNumber.setText("x" + data.getReturnQty());
            bindingView.tvPrice.setText("¥ "+data.getSkuPrice());
            bindingView.etOrderNo.setText(data.getOrderCode());
            bindingView.etMoney.setText("¥ " + data.getAmount());

            bindingView.etReason.setText(data.getReason());
            bindingView.etMsg.setText(data.getDescription());

            //图片
            ArrayList<String> listImgs = new ArrayList<>();
            List<ReturnApplyList.ApplyImgsBean> applyImgs = data.getApplyImgs();
            for (ReturnApplyList.ApplyImgsBean applyImg : applyImgs) {
                listImgs.add(applyImg.getUrl());
            }
            bindingView.bagPhoto.setEditable(false);
            bindingView.bagPhoto.setData(listImgs);

            //subStatus	Long 售后处理状态 0:未申请 1:审核中 2:审核驳回, 3:退款中 4.已退款
            String subStatus = data.getSubStatus();
            switch (subStatus) {
                case "0":
                    bindingView.tvStatus.setText("未申请");
                    break;
                case "1":
                    bindingView.tvStatus.setText("审核中");
                    break;
                case "2":
                    bindingView.rlReplay.setVisibility(View.VISIBLE);
                    bindingView.etReplay.setText(data.getAuditRemarks());
                    bindingView.tvStatus.setText("审核驳回");
                    break;
                case "3":
                    bindingView.tvStatus.setText("退款中");
                    break;
                case "4":
                    bindingView.tvStatus.setText("已退款");
                    break;
                case "5":
                    bindingView.tvStatus.setText("换货完成");
                    break;
            }

            String type = data.getType(); //申请类型 0:退货退款 1.换货
            String title = "退货";
            if (type.equals("0")) {
                title = "退货";
                bindingView.rlTkMoney.setVisibility(View.VISIBLE);
            }else if(type.equals("1")){
                title = "换货";
            }
            bindingView.tv2.setText(title+"编号");
            bindingView.tvReason.setText(title+"原因");
            bindingView.tvMsg.setText(title+"说明");
        }
    }

    @Override
    protected void initListener() {
//        bindingView.ivBack.setOnClickListener(new PerfectClickListener() {
//            @Override
//            protected void onNoDoubleClick(View v) {
//                onBackPressed();
//            }
//        });
    }

    public static void goPage(Context context, ReturnApplyList data) {
        Intent intent = new Intent(context, OrderShSqInfoActivity.class);
        intent.putExtra("data", data);
        context.startActivity(intent);
    }
}
