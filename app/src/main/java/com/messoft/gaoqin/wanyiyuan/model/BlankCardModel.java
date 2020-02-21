package com.messoft.gaoqin.wanyiyuan.model;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.BaseBean;
import com.messoft.gaoqin.wanyiyuan.bean.BlankCard;
import com.messoft.gaoqin.wanyiyuan.bean.DictionaryList;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;

import org.json.JSONObject;

import java.util.List;

public class BlankCardModel {
    /**
     * 所有银行列表查询（数据字典）
     */
    @SuppressWarnings("unchecked")
    public void getDataDictionaryList(BaseActivity context,
                                  String parentCode,
                                  final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("parentCode", parentCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getJavaCommonServer().getDataDictionaryList(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<DictionaryList>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<DictionaryList> login) {
                        listener.loadSuccess(login);
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        listener.loadFailed(errorCode, msg);
                    }
                });
    }


    /**
     * 我的银行列表
     */
    public void getMemberBankList(BaseActivity context, final RequestImpl listener) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("bind_account_id", bind_account_id);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        HttpClient.Builder.getWYYServer().getMemberBankList("")
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<BlankCard>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<BlankCard> login) {
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
     * 银行卡新增
     */
    @SuppressWarnings("unchecked")
    public void addMemberBankForm(BaseActivity context,
                                     String typeCode,
                                     String typeName,
                                     String branchName,
                                     String name,
                                     String cardNo,
                                     String isDefault,
                                     final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (typeCode != null) {
                jsonObject.put("typeCode", typeCode);
            }
            if (typeName != null) {
                jsonObject.put("typeName", typeName);
            }
            if (branchName != null) {
                jsonObject.put("branchName", branchName);
            }
            if (name != null) {
                jsonObject.put("name", name);
            }
            if (cardNo != null) {
                jsonObject.put("cardNo", cardNo);
            }
            if (isDefault != null) {
                jsonObject.put("isDefault", isDefault);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().addMemberBankForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.<BaseBean>compose())
                .compose(context.<BaseBean>bindToLifecycle())
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
     * 银行卡修改
     */
    @SuppressWarnings("unchecked")
    public void updateMemberBankForm(BaseActivity context,
                              String id,
                              String typeCode,
                              String typeName,
                              String branchName,
                              String name,
                              String cardNo,
                              String isDefault,
                              final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            if (typeCode != null) {
                jsonObject.put("typeCode", typeCode);
            }
            if (typeName != null) {
                jsonObject.put("typeName", typeName);
            }
            if (branchName != null) {
                jsonObject.put("branchName", branchName);
            }
            if (name != null) {
                jsonObject.put("name", name);
            }
            if (cardNo != null) {
                jsonObject.put("cardNo", cardNo);
            }
            if (isDefault != null) {
                jsonObject.put("isDefault", isDefault);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().updateMemberBankForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.<BaseBean>compose())
                .compose(context.<BaseBean>bindToLifecycle())
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
     * 银行卡删除
     */
    @SuppressWarnings("unchecked")
    public void delMemberBankForm(BaseActivity context,
                                     String id,
                                     final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().delMemberBankForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.<BaseBean>compose())
                .compose(context.<BaseBean>bindToLifecycle())
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
