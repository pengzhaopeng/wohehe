package com.messoft.gaoqin.wanyiyuan.model;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.GetRushGoodsList;
import com.messoft.gaoqin.wanyiyuan.bean.GetRushGoodsTypeList;
import com.messoft.gaoqin.wanyiyuan.bean.GetRushOrderList;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抢购模块
 */
public class QgModel {
    /**
     * 抢购列表
     */
    public void getRushGoodsTypeList(BaseActivity context,
                                     String userId,
                                     ArrayList statusAll,
                                     int pageNo,
                                     int pageSize, final RequestImpl listener) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("statusAll", statusAll);
        JSONObject jsonObject = new JSONObject(map);
        HttpClient.Builder.getJavaQgServer().getRushGoodsTypeList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<GetRushGoodsTypeList>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<GetRushGoodsTypeList> login) {
                        if (login != null) {
                            listener.loadSuccess(login);
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 抢购详情
     */
    public void getRushGoodsTypeById(BaseActivity context,
                                     String id,
                                     String userId,
                                     final RequestImpl listener) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("userId", userId);
        JSONObject jsonObject = new JSONObject(map);
        HttpClient.Builder.getJavaQgServer().getRushGoodsTypeById(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<GetRushGoodsTypeList>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(GetRushGoodsTypeList login) {
                        if (login != null) {
                            listener.loadSuccess(login);
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 立即抢购
     */
    @SuppressWarnings("unchecked")
    public void autoAllotGoodsOrder(BaseActivity context,
                                    String userId,
                                    String goodsTypeId,
                                    String mobile,
                                    String goodsIntegral,
                                    String medal,
                                    final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("goodsTypeId", goodsTypeId);
            jsonObject.put("mobile", mobile);
            jsonObject.put("goodsIntegral", goodsIntegral);//生态分
            jsonObject.put("medal", medal);//信用分
            if (BusinessUtils.getUserData() != null) {
                jsonObject.put("partner", BusinessUtils.getUserData().getWechat());//微信
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getJavaQgServer().autoAllotGoodsOrder(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, true, "排队抢购中...") {
                    @Override
                    protected void onSuccess(Object login) {
                        listener.loadSuccess(login);
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 新增申请预约抢购
     */
    @SuppressWarnings("unchecked")
    public void addRushOrderForm(BaseActivity context,
                                 String userId,
                                 String goodsTypeId,
                                 String mobile,
                                 String goodsIntegral,
                                 String medal,
                                 final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("goodsTypeId", goodsTypeId);
            jsonObject.put("mobile", mobile);
            jsonObject.put("goodsIntegral", goodsIntegral);//生态分
            jsonObject.put("medal", medal);//信用分

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getJavaQgServer().addRushOrderForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(Object login) {
                        listener.loadSuccess(login);
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 我的抢购订单列表
     */
    public void getRushOrderList(BaseActivity context,
                                 int flagType,
                                 String userId,
                                 ArrayList statusAll,
                                 int pageNo,
                                 int pageSize,
                                 final RequestImpl listener) {
        Map<String, Object> map = new HashMap<>();
        if (flagType == 0) {
            map.put("userId", userId);
        } else {
            map.put("createBy", userId);
        }
        map.put("statusAll", statusAll);
        JSONObject jsonObject = new JSONObject(map);
        HttpClient.Builder.getJavaQgServer().getRushOrderList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<GetRushOrderList>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<GetRushOrderList> login) {
                        if (login != null) {
                            listener.loadSuccess(login);
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 我的抢购订单详情
     */
    public void getRushOrderById(BaseActivity context,
                                 String id,
                                 String userId,
                                 final RequestImpl listener) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("userId", userId);
        JSONObject jsonObject = new JSONObject(map);
        HttpClient.Builder.getJavaQgServer().getRushOrderById(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<GetRushOrderList>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(GetRushOrderList login) {
                        if (login != null) {
                            listener.loadSuccess(login);
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 抢购订单编辑（更新地址）+ （新增我要寄售）+ 我要提货等
     */
    @SuppressWarnings("unchecked")
    public void updateRushOrderForm(BaseActivity context,
                                    int flagType, //0-买家  1-卖家
                                    String userId,
                                    String orderId,
                                    String status,
                                    String auditStatus,
                                    String consigneeName,
                                    String consigneeMobile,
                                    String province,
                                    String provinceText,
                                    String city,
                                    String cityText,
                                    String district,
                                    String districtText,
                                    String address,
                                    String isRevenue,
                                    final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (flagType == 0) {
                jsonObject.put("userId", userId);
            } else {
                jsonObject.put("createBy", userId);
            }
            jsonObject.put("orderId", orderId);
            if (status != null) {
                jsonObject.put("status", status);
            }
            if (auditStatus != null) {
                jsonObject.put("auditStatus", auditStatus);
            }
            if (consigneeName != null) {
                jsonObject.put("consigneeName", consigneeName);
            }
            if (consigneeMobile != null) {
                jsonObject.put("consigneeMobile", consigneeMobile);
            }
            if (province != null) {
                jsonObject.put("province", province);
            }
            if (provinceText != null) {
                jsonObject.put("provinceText", provinceText);
            }
            if (city != null) {
                jsonObject.put("city", city);
            }
            if (cityText != null) {
                jsonObject.put("cityText", cityText);
            }
            if (district != null) {
                jsonObject.put("district", district);
            }
            if (districtText != null) {
                jsonObject.put("districtText", districtText);
            }
            if (address != null) {
                jsonObject.put("address", address);
            }
            if (isRevenue != null) {
                jsonObject.put("isRevenue", isRevenue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getJavaQgServer().updateRushOrderForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(Object login) {
                        listener.loadSuccess(login);
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 补录收款地址
     */
    @SuppressWarnings("unchecked")
    public void updateRushOrderForm1(BaseActivity context,
                                     int flagType, //0-买家  1-卖家
                                     String goodsId, //0-买家  1-卖家
                                     String userId,
                                     String name,
                                     String mobile,
                                     String wechat,
                                     String orderId,
                                     String goodsTypeId,
                                     String payType,
                                     String payCode,
                                     String cardId,
                                     String cardNo,
                                     String cardName,
                                     String bankName,
                                     String banckBranchName,
                                     String auditStatus,
                                     final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (flagType == 0) {
                jsonObject.put("userId", userId);
            } else {
                jsonObject.put("createBy", userId);
            }
            if (name != null) {
                jsonObject.put("shopName", name);
            }
            if (goodsId != null) {
                jsonObject.put("goodsId", goodsId);
            }
            if (mobile != null) {
                jsonObject.put("shopMobile", mobile);
            }
            if (wechat != null) {
                jsonObject.put("wechat", wechat);
            }
            jsonObject.put("orderId", orderId);
            if (goodsTypeId != null) {
                jsonObject.put("goodsTypeId", goodsTypeId);
            }
            if (payType != null) {
                jsonObject.put("payType", payType);
            }
            if (payCode != null) {
                jsonObject.put("payCode", payCode);
            }
            if (cardId != null) {
                jsonObject.put("cardId", cardId);
            }
            if (cardNo != null) {
                jsonObject.put("cardNo", cardNo);
            }
            if (cardName != null) {
                jsonObject.put("cardName", cardName);
            }
            if (bankName != null) {
                jsonObject.put("bankName", bankName);
            }
            if (banckBranchName != null) {
                jsonObject.put("banckBranchName", banckBranchName);
            }
            if (auditStatus != null) {
                jsonObject.put("auditStatus", auditStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getJavaQgServer().updateRushOrderForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(Object login) {
                        listener.loadSuccess(login);
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 寄售 (抢购订单申请置换)+(申请置换)
     */
    @SuppressWarnings("unchecked")
    public void autoAllotOrder2Goods(BaseActivity context,
                                     int flagType, //0-买家  1-卖家
                                     String userId,
                                     String name,
                                     String mobile,
                                     String wechat,
                                     String orderId,
                                     String goodsTypeId,
                                     String payType,
                                     String payCode,
                                     String cardId,
                                     String cardNo,
                                     String cardName,
                                     String bankName,
                                     String banckBranchName,
                                     final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (flagType == 0) {
                jsonObject.put("userId", userId);
            } else {
                jsonObject.put("createBy", userId);
            }
            if (name != null) {
                jsonObject.put("shopName", name);
            }
            if (mobile != null) {
                jsonObject.put("shopMobile", mobile);
            }
            if (wechat != null) {
                jsonObject.put("wechat", wechat);
            }
            jsonObject.put("orderId", orderId);
            if (goodsTypeId != null) {
                jsonObject.put("goodsTypeId", goodsTypeId);
            }
            if (payType != null) {
                jsonObject.put("payType", payType);
            }
            if (payCode != null) {
                jsonObject.put("payCode", payCode);
            }
            if (cardId != null) {
                jsonObject.put("cardId", cardId);
            }
            if (cardNo != null) {
                jsonObject.put("cardNo", cardNo);
            }
            if (cardName != null) {
                jsonObject.put("cardName", cardName);
            }
            if (bankName != null) {
                jsonObject.put("bankName", bankName);
            }
            if (banckBranchName != null) {
                jsonObject.put("banckBranchName", banckBranchName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getJavaQgServer().autoAllotOrder2Goods(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(Object login) {
                        listener.loadSuccess(login);
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 寄售 (抢购订单申请置换)+(申请置换)
     */
    @SuppressWarnings("unchecked")
    public void updateRushGoodsForm(BaseActivity context,
                                    int flagType, //0-买家  1-卖家
                                    String goodsId, //0-买家  1-卖家
                                    String userId,
                                    String name,
                                    String mobile,
                                    String wechat,
                                    String orderId,
                                    String goodsTypeId,
                                    String payType,
                                    String payCode,
                                    String cardId,
                                    String cardNo,
                                    String cardName,
                                    String bankName,
                                    String banckBranchName,
                                    String auditStatus,
                                    final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (flagType == 0) {
                jsonObject.put("userId", userId);
            } else {
                jsonObject.put("createBy", userId);
            }
            if (name != null) {
                jsonObject.put("shopName", name);
            }
            if (goodsId != null) {
                jsonObject.put("goodsId", goodsId);
            }
            if (mobile != null) {
                jsonObject.put("shopMobile", mobile);
            }
            if (wechat != null) {
                jsonObject.put("wechat", wechat);
            }
            jsonObject.put("orderId", orderId);
            if (goodsTypeId != null) {
                jsonObject.put("goodsTypeId", goodsTypeId);
            }
            if (payType != null) {
                jsonObject.put("payType", payType);
            }
            if (payCode != null) {
                jsonObject.put("payCode", payCode);
            }
            if (cardId != null) {
                jsonObject.put("cardId", cardId);
            }
            if (cardNo != null) {
                jsonObject.put("cardNo", cardNo);
            }
            if (cardName != null) {
                jsonObject.put("cardName", cardName);
            }
            if (bankName != null) {
                jsonObject.put("bankName", bankName);
            }
            if (banckBranchName != null) {
                jsonObject.put("banckBranchName", banckBranchName);
            }
            if (auditStatus != null) {
                jsonObject.put("auditStatus", auditStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getJavaQgServer().updateRushGoodsForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(Object login) {
                        listener.loadSuccess(login);
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }


    /**
     * 我的寄售订单列表
     */
    public void getRushGoodsList(BaseActivity context,
                                 String createBy,
                                 String createName,
                                 String status,
                                 String auditStatus,
                                 int pageNo,
                                 int pageSize,
                                 final RequestImpl listener) {
        Map<String, Object> map = new HashMap<>();
        map.put("createBy", createBy);
        map.put("createName", createName);
        map.put("status", status);
        map.put("auditStatus", auditStatus);
        JSONObject jsonObject = new JSONObject(map);
        HttpClient.Builder.getJavaQgServer().getRushGoodsList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<GetRushGoodsList>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<GetRushGoodsList> login) {
                        if (login != null) {
                            listener.loadSuccess(login);
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 我的待寄售详情
     */
    public void getRushGoodsById(BaseActivity context,
                                 String goodsId,
                                 final RequestImpl listener) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", goodsId);
        JSONObject jsonObject = new JSONObject(map);
        HttpClient.Builder.getJavaQgServer().getRushGoodsById(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<GetRushGoodsList>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(GetRushGoodsList login) {
                        if (login != null) {
                            listener.loadSuccess(login);
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }

    /**
     * 转让订单
     */
    @SuppressWarnings("unchecked")
    public void updateRushOrderCall(BaseActivity context,
                                    String goodsTypeId,
                                    String orderId,
                                    String memberId,
                                    String memberAccount,
                                    String distributionStatus,
                                    final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderId", orderId);
            jsonObject.put("goodsTypeId", goodsTypeId);
            jsonObject.put("memberId", memberId);
            jsonObject.put("memberAccount", memberAccount);
            jsonObject.put("distributionStatus", distributionStatus);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getJavaQgServer().updateRushOrderCall(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(Object login) {
                        listener.loadSuccess(login);
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }


}
