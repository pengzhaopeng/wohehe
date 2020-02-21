package com.messoft.gaoqin.wanyiyuan.model;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.EvaluationTagList;
import com.messoft.gaoqin.wanyiyuan.bean.OrderInfo;
import com.messoft.gaoqin.wanyiyuan.bean.OrderItemList;
import com.messoft.gaoqin.wanyiyuan.bean.ReturnApplyList;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;

import org.json.JSONObject;

import java.util.List;

public class OrderModel {
    /**
     * 订单列表查询
     */
    public void getOrderList(BaseActivity context,
                             int handleStatus,
                             String mHandleStatuses,
                             String memberId,
                             String companyId,
                             int mIsComment,
                             int pageNo, int pageSize, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (handleStatus != -1) {
                jsonObject.put("handleStatus", handleStatus); //状态
            }
            if (mHandleStatuses != null) {
                jsonObject.put("handleStatuses", mHandleStatuses); //状态
            }
            if (mIsComment != -1) {
                jsonObject.put("isComment", mIsComment); //是否评价
            }
            if (StringUtils.isNoEmpty(memberId)) {
                jsonObject.put("memberId", memberId); //买家
            }
            if (StringUtils.isNoEmpty(companyId)) {
                jsonObject.put("companyId", companyId); //卖家
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getOrderList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<OrderInfo>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<OrderInfo> login) {
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
     * 订单标识查询
     */
    public void getOrderById(BaseActivity context,
                             String id, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getOrderById(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<OrderInfo>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(OrderInfo login) {
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
     * 订单取消 100
     * 订单确认收货 4
     * 订单发货 2
     */
    @SuppressWarnings("unchecked")
    public void updateOrderStatus(BaseActivity context,
                                  String id,
                                  int handleStatus,
                                  String logisticsId,
                                  String logisticsName,
                                  String waybill,
                                  final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("handleStatus", handleStatus);
            if (logisticsId != null) {
                jsonObject.put("logisticsId", logisticsId);
            }
            if (logisticsName != null) {
                jsonObject.put("logisticsName", logisticsName);
            }
            if (logisticsName != null) {
                jsonObject.put("waybill", waybill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().updateOrderStatus(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
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
     * 付款
     */

    /**
     * 订单项提货券转让
     */
    @SuppressWarnings("unchecked")
    public void updateOrderItemForm(BaseActivity context,
                                    String id,
                                    int transferState,
                                    final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("transferState", transferState); //装让状态 0:不转让 1:转让 2.完成
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().updateOrderItemForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
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
     * 商品评价新增
     */
    @SuppressWarnings("unchecked")
    public void addEvaluationForm(BaseActivity context,
                                  String type,
                                  String memberId,
                                  String evaluationTagIds,
                                  String orderId,
                                  String productId,
                                  String shopId,
                                  String skuId,
                                  int score,
                                  String content,
                                  final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", type); //评价类型(1:商品评价类别,2:店铺评价类别)
            jsonObject.put("memberId", memberId);
            jsonObject.put("evaluationTagIds", evaluationTagIds);
            jsonObject.put("orderId", orderId);
            jsonObject.put("productId", productId);
            jsonObject.put("shopId", shopId);//0
            jsonObject.put("skuId", skuId);
            jsonObject.put("score", score);
            jsonObject.put("content", content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().addEvaluationForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
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
     * 评价标签列表查询
     */
    public void getEvaluationTagList(BaseActivity context,
                                     String classId, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("classId", classId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getEvaluationTagList(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<EvaluationTagList>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<EvaluationTagList> login) {
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
     * 订单项列表查询
     */
    public void getOrderItemList(BaseActivity context,
                                 String memberId,
                                 String lsq,
                                 String classCode,
                                 String handleStatus,
                                 String handleStatuses,
                                 String transferState,
                                 int pageNo, int pageSize, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("memberId", memberId); //状态
            if (lsq != null) {
                jsonObject.put("lsq", lsq);
            }
            if (classCode != null) {
                jsonObject.put("classCode", classCode);
            }
            if (handleStatus != null) {
                jsonObject.put("handleStatus", handleStatus);
            }
            if (handleStatuses != null) {
                jsonObject.put("handleStatuses", handleStatuses);
            }
            if (transferState != null) {
                jsonObject.put("transferStates", transferState);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getOrderItemList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<OrderItemList>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<OrderItemList> login) {
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
     * 退换货申请列表查询
     */
    public void getReturnApplyList(BaseActivity context,
                                   String memberId,
                                   String lsq,
                                   String classCode,
                                   String handleStatus,
                                   int pageNo, int pageSize, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("memberId", memberId); //状态
            if (lsq != null) {
                jsonObject.put("lsq", lsq);
            }
            if (classCode != null) {
                jsonObject.put("classCode", classCode);
            }
            if (handleStatus != null) {
                jsonObject.put("handleStatus", handleStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getReturnApplyList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<ReturnApplyList>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<ReturnApplyList> login) {
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
     * 退换货申请新增
     */
    @SuppressWarnings("unchecked")
    public void addReturnApplyForm(BaseActivity context,
                                   int type,
                                   String orderId,
                                   String orderItemId,
                                   String reason,
                                   String amount,
                                   String description,
                                   String applyImgs,
                                   final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", type);
            jsonObject.put("orderId", orderId);
            jsonObject.put("orderItemId", orderItemId);
            if (reason != null) {
                jsonObject.put("reason", reason);
            }
            if (amount != null) {
                jsonObject.put("amount", amount);
            }
            if (description != null) {
                jsonObject.put("description", description);
            }
            if (applyImgs != null) {
                jsonObject.put("applyImgs", applyImgs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().addReturnApplyForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
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
