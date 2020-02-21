package com.messoft.gaoqin.wanyiyuan.model;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.GetCartList;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;

import org.json.JSONObject;

import java.util.List;

public class ShopCarModel {

    /**
     * 购物车列表查询
     */
    public void getCartList(BaseActivity context, String memberId, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("memberId", memberId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getCartList(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<GetCartList>>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(List<GetCartList> login) {
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
     * 购物车新增
     */
    @SuppressWarnings("unchecked")
    public void addCartForm(BaseActivity context,
                             String memberId,
                             String companyId,
                             String classId,
                             String productId,
                             String skuId,
                             String skuPrice,
                             int skuQty,
                             final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("memberId", memberId);
            jsonObject.put("companyId", companyId);
            jsonObject.put("classId", classId);
            jsonObject.put("productId", productId);
            jsonObject.put("skuId", skuId);
            jsonObject.put("skuPrice", skuPrice);
            jsonObject.put("skuQty", skuQty);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().addCartForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, false, "加载中...") {
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
     * 购物车修改
     */
    @SuppressWarnings("unchecked")
    public void updateCartForm(BaseActivity context,
                             String id,
                             int skuQty,
                             final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("skuQty", skuQty);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().updateCartForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, false, "加载中...") {
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
     * 购物车删除
     */
    @SuppressWarnings("unchecked")
    public void delCartForm(BaseActivity context,
                                String id,
                                final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().delCartForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver(context, false, "加载中...") {
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
