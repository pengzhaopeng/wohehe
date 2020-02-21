package com.messoft.gaoqin.wanyiyuan.model;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.ConfigValue;
import com.messoft.gaoqin.wanyiyuan.bean.GetMemberPointsLogList;
import com.messoft.gaoqin.wanyiyuan.bean.MemberAvailableAmount;
import com.messoft.gaoqin.wanyiyuan.bean.MemberBillList;
import com.messoft.gaoqin.wanyiyuan.bean.MemberCapitalLogList;
import com.messoft.gaoqin.wanyiyuan.bean.MemberCapitalWaitLogList;
import com.messoft.gaoqin.wanyiyuan.bean.MemberCapitalWaitLogSummary;
import com.messoft.gaoqin.wanyiyuan.bean.MemberSettlementLineList;
import com.messoft.gaoqin.wanyiyuan.bean.MemberSettlementLineStatistics;
import com.messoft.gaoqin.wanyiyuan.bean.MemberSettlementLis;
import com.messoft.gaoqin.wanyiyuan.bean.MemberWholesaleCapitalList;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;

import org.json.JSONObject;

import java.util.List;

public class WalletModel {
    /**
     * 会员余额明细列表查询
     */
    public void getMemberCapitalLogList(BaseActivity context,
                                        String type,
                                        int pageNo,
                                        int pageSize, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (type != null) {
                jsonObject.put("type", type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getMemberCapitalLogList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<MemberCapitalLogList>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<MemberCapitalLogList> login) {
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
     * 提现手续费率查询
     */
    public void getConfigValue(BaseActivity context,
                               String configType,
                               String key,
                               final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("configType", configType);
            jsonObject.put("key", key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getJavaCommonServer().getConfigValue(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<ConfigValue>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(ConfigValue login) {
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
     * 会员可用金额查询
     */
    public void getMemberAvailableAmount(BaseActivity context,
                                         String type,
                                         final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getMemberAvailableAmount(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<MemberAvailableAmount>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(MemberAvailableAmount login) {
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
     * 会员提现新增
     */
    @SuppressWarnings("unchecked")
    public void addMemberWithdrawForm(BaseActivity context,
                                      Double amount,
                                      String payPassword,
                                      String bankId,
                                      final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("amount", amount);
            jsonObject.put("payPassword", payPassword);
            jsonObject.put("bankId", bankId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().addMemberWithdrawForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
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
     * 会员转账新增
     */
    @SuppressWarnings("unchecked")
    public void addMemberTransferForm(BaseActivity context,
                                      Double amount,
                                      String payPassword,
                                      String payeeAccount,
                                      final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("amount", amount);
            jsonObject.put("payPassword", payPassword);
            jsonObject.put("payeeAccount", payeeAccount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().addMemberTransferForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
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
     * 会员账单列表查询
     */
    public void getMemberBillList(BaseActivity context,
                                  String type,
                                  String beginTime,
                                  String endTime,
                                  int pageNo,
                                  int pageSize, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (type != null) {
                jsonObject.put("type", type);
            }
            if (beginTime != null) {
                jsonObject.put("beginTime", beginTime);
            }
            if (endTime != null) {
                jsonObject.put("endTime", endTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getMemberBillList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<MemberBillList>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<MemberBillList> login) {
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
     * 会员资金待入明细列表查询
     */
    public void getMemberCapitalWaitLogList(BaseActivity context,
                                            String memberId,
                                            String isArrival,
                                            String beginTime,
                                            String endTime,
                                            int pageNo,
                                            int pageSize, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (memberId != null) {
                jsonObject.put("memberId", memberId);
            }
            if (isArrival != null) {
                jsonObject.put("isArrival", isArrival);
            }
            if (beginTime != null) {
                jsonObject.put("createTime_begin", beginTime);
            }
            if (endTime != null) {
                jsonObject.put("createTime_end", endTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getMemberCapitalWaitLogList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<MemberCapitalWaitLogList>>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(List<MemberCapitalWaitLogList> login) {
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
     * 会员批发额度明细列表查询
     */
    public void getMemberWholesaleCapitalList(BaseActivity context,
                                              String memberId,
                                              String isArrival,
                                              String beginTime,
                                              String endTime,
                                              int pageNo,
                                              int pageSize, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (memberId != null) {
                jsonObject.put("memberId", memberId);
            }
            if (isArrival != null) {
                jsonObject.put("isArrival", isArrival);
            }
            if (beginTime != null) {
                jsonObject.put("beginTime", beginTime);
            }
            if (endTime != null) {
                jsonObject.put("endTime", endTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getMemberWholesaleCapitalList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<MemberWholesaleCapitalList>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<MemberWholesaleCapitalList> login) {
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
     * 会员资金待入明细汇总
     */
    public void getMemberCapitalWaitLogSummary(BaseActivity context,
                                               String memberId,
                                               String isArrival, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (memberId != null) {
                jsonObject.put("memberId", memberId);
            }
            if (isArrival != null) {
                jsonObject.put("isArrival", isArrival);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getMemberCapitalWaitLogSummary(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<MemberCapitalWaitLogSummary>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(MemberCapitalWaitLogSummary login) {
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
     * 收益列表明细查询
     */
    public void getMemberSettlementLineList(BaseActivity context,
                                            String memberId,
                                            String settlementId,
                                            String type,
                                            int pageNo,
                                            int pageSize, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (memberId != null) {
                jsonObject.put("memberId", memberId);
            }
            if (settlementId != null) {
                jsonObject.put("settlementId", settlementId);
            }
            if (memberId != null) {
                jsonObject.put("type", type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getMemberSettlementLineList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<MemberSettlementLineList>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<MemberSettlementLineList> login) {
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
     * 代理商收益统计查询
     */
    public void getMemberSettlementLineStatistics(BaseActivity context,
                                                  final RequestImpl listener) {

        HttpClient.Builder.getWYYServer().getMemberSettlementLineStatistics("")
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<MemberSettlementLineStatistics>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(MemberSettlementLineStatistics login) {
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
     * 结算列表查询
     */
    public void getMemberSettlementLis(BaseActivity context,
                                       int pageNo,
                                       int pageSize, final RequestImpl listener) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            if (memberId != null) {
//                jsonObject.put("memberId", memberId);
//            }
//            if (memberId != null) {
//                jsonObject.put("type", type);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        HttpClient.Builder.getWYYServer().getMemberSettlementLis("", pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<MemberSettlementLis>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<MemberSettlementLis> login) {
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
     * 会员积分明细列表查询
     */
    public void getMemberPointsLogList(BaseActivity context,
                                  String memberId,
                                  int type, //0：购物积分，1：代金券，2：生态分
                                  int pageNo,
                                  int pageSize, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("memberId", memberId);
            jsonObject.put("type", type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getMemberPointsLogList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<GetMemberPointsLogList>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<GetMemberPointsLogList> login) {
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
