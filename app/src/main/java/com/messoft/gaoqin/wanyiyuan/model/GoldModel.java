package com.messoft.gaoqin.wanyiyuan.model;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.BeanOrderInfoById;
import com.messoft.gaoqin.wanyiyuan.bean.BeanOrderInfoList;
import com.messoft.gaoqin.wanyiyuan.bean.GetBeanOrderAppealList;
import com.messoft.gaoqin.wanyiyuan.bean.GetMemberWholesaleRushListByCache;
import com.messoft.gaoqin.wanyiyuan.bean.WholesaleRush;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * 金豆
 */
public class GoldModel {
    /**
     * 金豆挂售列表查询
     */
    public void getBeanOrderInfoList(BaseActivity context,
                                     String account,
                                     String state,
                                     String beanAmountBegin,
                                     String beanAmountEnd,
                                     int pageNo,
                                     int pageSize, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (account != null) {
                jsonObject.put("account", account);
            }
            if (state != null) {
                jsonObject.put("state", state);
            }
            if (beanAmountBegin != null) {
                jsonObject.put("beanAmountBegin", beanAmountBegin);
            }
            if (beanAmountBegin != null) {
                jsonObject.put("beanAmountEnd", beanAmountEnd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getBeanOrderInfoList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<BeanOrderInfoList>>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(List<BeanOrderInfoList> login) {
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
     * 我挂售的金豆列表查询
     */
    public void getMySaleBeanOrderList(BaseActivity context,
                                       String account,
                                       String state,
                                       String states,
                                       String beanAmountBegin,
                                       String beanAmountEnd,
                                       int pageNo,
                                       int pageSize, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (account != null) {
                jsonObject.put("account", account);
            }
            if (state != null) {
                jsonObject.put("state", state);
            }
            if (states != null) {
                jsonObject.put("states", states);
            }
            if (beanAmountBegin != null) {
                jsonObject.put("beanAmountBegin", beanAmountBegin);
            }
            if (beanAmountBegin != null) {
                jsonObject.put("beanAmountEnd", beanAmountEnd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getMySaleBeanOrderList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<BeanOrderInfoList>>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(List<BeanOrderInfoList> login) {
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
     * 我采购的金豆列表查询
     */
    public void getMyPurchaserBeanOrderList(BaseActivity context,
                                            String account,
                                            String state,
                                            String states,
                                            String beanAmountBegin,
                                            String beanAmountEnd,
                                            int pageNo,
                                            int pageSize, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (account != null) {
                jsonObject.put("account", account);
            }
            if (state != null) {
                jsonObject.put("state", state);
            }
            if (states != null) {
                jsonObject.put("states", states);
            }
            if (beanAmountBegin != null) {
                jsonObject.put("beanAmountBegin", beanAmountBegin);
            }
            if (beanAmountBegin != null) {
                jsonObject.put("beanAmountEnd", beanAmountEnd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getMyPurchaserBeanOrderList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<BeanOrderInfoList>>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(List<BeanOrderInfoList> login) {
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
     * 金豆详情查询
     */
    public void getBeanOrderInfoById(BaseActivity context,
                                     String id,
                                     final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getBeanOrderInfoById(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<BeanOrderInfoById>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(BeanOrderInfoById login) {
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
     * 金豆充值
     */
    @SuppressWarnings("unchecked")
    public void rechargeBeanOrderForm(BaseActivity context,
                                      String id,
                                      final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().rechargeBeanOrderForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
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
     * 金豆挂售新增
     */
    @SuppressWarnings("unchecked")
    public void addBeanOrderForm(BaseActivity context,
                                 String title,
                                 String payPassword,
                                 String name,
                                 String contactMobile,
                                 String wechat,
                                 String beanAmount,
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
            jsonObject.put("title", title);
            jsonObject.put("payPassword", payPassword);
            jsonObject.put("name", name);
            jsonObject.put("contactMobile", contactMobile);
            jsonObject.put("wechat", wechat);
            jsonObject.put("beanAmount", beanAmount);
            jsonObject.put("payType", payType);

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
        HttpClient.Builder.getWYYServer().addBeanOrderForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
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
     * 取消金豆订单
     */
    @SuppressWarnings("unchecked")
    public void cancelBeanOrderForm(BaseActivity context,
                                    String id,
                                    final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().cancelBeanOrderForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
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
     * 金豆发货
     */
    @SuppressWarnings("unchecked")
    public void deliverBeanOrderForm(BaseActivity context,
                                     String id,
                                     final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().deliverBeanOrderForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
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
     * 金豆支付
     */
    @SuppressWarnings("unchecked")
    public void payBeanOrderForm(BaseActivity context,
                                 String id,
                                 final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().payBeanOrderForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
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
     * 金豆订单投诉新增
     */
    @SuppressWarnings("unchecked")
    public void addBeanOrderAppealForm(BaseActivity context,
                                       int flagType,
                                       String orderId,
                                       String orderNo,
                                       String contactMobile,
                                       String content,
                                       String certificateUrl,
                                       String ext,
                                       final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (flagType == 1) { //寄售
                jsonObject.put("createBy", BusinessUtils.getBindAccountId());
                jsonObject.put("isVipShop", 1);
            } else if (flagType == 2) { //抢购
                jsonObject.put("userId", BusinessUtils.getBindAccountId());
                jsonObject.put("isVipMember", 1);
            }
            jsonObject.put("orderId", orderId);
            jsonObject.put("orderNo", orderNo);
            jsonObject.put("contactMobile", contactMobile);
            jsonObject.put("content", content);
            jsonObject.put("certificateUrl", certificateUrl);
            if (ext != null) {
                jsonObject.put("ext", ext);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().addBeanOrderAppealForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
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
     * 金豆投诉单列表查询
     */
    public void getBeanOrderAppealList(BaseActivity context,
                                       String memberId,
                                       String ext,
                                       int pageNo,
                                       int pageSize, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (memberId != null) {
                jsonObject.put("memberId", memberId);
            }
            if (ext != null) {
                jsonObject.put("ext", ext);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getBeanOrderAppealList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<GetBeanOrderAppealList>>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(List<GetBeanOrderAppealList> login) {
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
     * 投诉单更新
     */
    @SuppressWarnings("unchecked")
    public void updateAuditBeanOrderAppealForm(BaseActivity context,
                                               String id,
                                               String state,
                                               String isReal,
                                               String updateBy,
                                               String updateName,
                                               final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("state", state);
            jsonObject.put("isReal", isReal);
            jsonObject.put("updateBy", updateBy);
            jsonObject.put("updateName", updateName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().updateAuditBeanOrderAppealForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
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
     * 批发额度抢购列表
     */
    public void getMemberWholesaleRushListByCache(BaseActivity context,
//                                                  int pageNo,
//                                                  int pageSize,
                                                  final RequestImpl listener) {
        HttpClient.Builder.getWYYServer().getMemberWholesaleRushListByCache("")
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<GetMemberWholesaleRushListByCache>>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(List<GetMemberWholesaleRushListByCache> login) {
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
     * 批发额度抢购
     */
    @SuppressWarnings("unchecked")
    public void wholesaleRush(BaseActivity context,
                              String id,
                              final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().wholesaleRush(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<WholesaleRush>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(WholesaleRush login) {
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

}
