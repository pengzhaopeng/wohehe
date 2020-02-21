package com.messoft.gaoqin.wanyiyuan.utils;

import android.content.DialogInterface;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.AvailableLimitAmount;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.ui.my.SjhyActivity;

import org.json.JSONObject;

/**
 * 购买下单前的校验规则
 */
public class VerifyOrderUtils {

    //会员区商品 classCode
    private static String hyq = "hyzmq";

    public static void verifyOrder(BaseActivity activity,
                                   double totalMoney,
                                   String classCode,
                                   String skuId,
                                   String classId,
                                   VerifyOrderImpl verifyOrder) {
        //会员区的单独判断，交了钱才能买
        if (classCode.equals(hyq) &&
                BusinessUtils.getUserData() != null &&
                BusinessUtils.getUserData().getIsVip().equals("0")) {
            DialogWithYesOrNoUtils.getInstance().showDialog(activity, "温馨提示", "你还不是正式会员，成为正式会员才能购买", "成为会员", "取消", new DialogWithYesOrNoUtils.DialogCallBack() {
                @Override
                public void exectEvent(DialogInterface dialog) {
                    SysUtils.startActivity(activity, SjhyActivity.class);
                }

                @Override
                public void exectCancel(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
        } else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("memberId", BusinessUtils.getBindAccountId());
                jsonObject.put("skuId", skuId);
                jsonObject.put("classId", classId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            HttpClient.Builder.getWYYServer().getAvailableLimitAmount(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                    .compose(RxSchedulers.compose())
                    .compose(activity.bindToLifecycle())
                    .subscribe(new BaseObserver<AvailableLimitAmount>(activity, true, "校验规则中...") {
                        @Override
                        protected void onSuccess(AvailableLimitAmount login) {
                            if (login != null &&
                                    login.getTotalLimitAmount() != null &&
                                    login.getAvailableTotalLimitAmount() != null &&
                                    login.getSingleLimitAmount() != null &&
                                    login.getAvailableSingleLimitAmount() != null) {
                                // 总限额(迁移模块删除)
                                double totalLimitAmount = Double.parseDouble(login.getTotalLimitAmount());
                                // 可用总限额(迁移模块删除)
                                double availableTotalLimitAmount = Double.parseDouble(login.getAvailableTotalLimitAmount());
                                // 单品限额(迁移模块删除)
                                double singleLimitAmount = Double.parseDouble(login.getSingleLimitAmount());
                                // 可用单品限额(迁移模块删除)
                                double availableSingleLimitAmount = Double.parseDouble(login.getAvailableSingleLimitAmount());
                                //批发区额度
                                double wholesaleCapital = Double.parseDouble(login.getWholesaleCapital());
                                //会员区只要单独判断
                                if (classCode.equals(hyq)) {
                                    if (totalMoney > availableSingleLimitAmount) {
                                        // 忽略第一单校验
                                        if (singleLimitAmount != availableSingleLimitAmount) {
                                            ToastUtil.showShortToast("当日单品额度超过限制,请明天再来购买!");
                                            return;
                                        }
                                    }
                                    if (totalMoney > availableTotalLimitAmount) {
                                        // 忽略第一单校验
                                        if (totalLimitAmount != availableTotalLimitAmount) {
                                            ToastUtil.showShortToast("当日购买总额度超过限制,请明天再来购买!");
                                            return;
                                        }
                                    }
                                }else{
                                    if (totalMoney > availableSingleLimitAmount) {
                                        // 忽略第一单校验
                                        if (singleLimitAmount != availableSingleLimitAmount) {
                                            ToastUtil.showShortToast("当日单品额度超过限制,请明天再来购买!");
                                            return;
                                        }
                                    }
                                    if (totalMoney > availableTotalLimitAmount) {
                                        // 忽略第一单校验
                                        if (totalLimitAmount != availableTotalLimitAmount) {
                                            ToastUtil.showShortToast("当日购买总额度超过限制,请明天再来购买!");
                                            return;
                                        }
                                    }
                                    // 如果是零售区商品,并且有批发额度
                                    if (classCode.contains("lsq")) {
                                        if (wholesaleCapital > 0) {
                                            ToastUtil.showShortToast("您还有批发额度未使用!");
                                            return;
                                        }
                                    }

                                    if (classCode.contains("pfq")) {
                                        if (wholesaleCapital != totalMoney) {
                                            ToastUtil.showShortToast("当前订单总额与批发额度不一致!");
                                            return;
                                        }
                                    }
                                }

                                verifyOrder.verifyResult(true);

                            } else {
                                verifyOrder.verifyResult(false);
                            }
                        }

                        @Override
                        protected void onFailure(int errorCode, String msg) {
                            verifyOrder.loadFailed(errorCode, msg);
                        }
                    });
        }

    }
}

