package com.messoft.gaoqin.wanyiyuan.model;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.EvaluationList;
import com.messoft.gaoqin.wanyiyuan.bean.ProductDetailById;
import com.messoft.gaoqin.wanyiyuan.bean.ProductInfo;
import com.messoft.gaoqin.wanyiyuan.bean.ProductSKuAttr;
import com.messoft.gaoqin.wanyiyuan.bean.ProductType;
import com.messoft.gaoqin.wanyiyuan.bean.SkuList;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;

import org.json.JSONObject;

import java.util.List;

public class ProductModel {
    /**
     * 2.1.1 商品分类列表查询
     */
    public void getAreaList(BaseActivity context, String memberId, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("memberId", memberId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getProductType(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<ProductType>>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(List<ProductType> login) {
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
     * 2.1.1 商品分类列表查询(生活必备)
     */
    public void getAreaList1(BaseActivity context,
                             String memberId,
                             String code,
                             final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("memberId", memberId);
            jsonObject.put("code", code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getProductType(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<ProductType>>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(List<ProductType> login) {
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
     * 2.2.4 商品列表查询
     */
    public void getProductList(BaseActivity context,
                               String memberId,
                               String classId,
                               String isSale,
                               String isDel,
                               String auditState,
                               int pageNo, int pageSize, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (memberId != null) {
                jsonObject.put("memberId", memberId);
            }
            if (classId != null) {
                jsonObject.put("classId", classId);
            }
            if (isSale != null) {
                jsonObject.put("isSale", isSale);
            }
            if (isDel != null) {
                jsonObject.put("isDel", isDel);
            }
            if (auditState != null) {
                jsonObject.put("auditState", auditState);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getProductList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<ProductInfo>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<ProductInfo> login) {
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
     * 商品标识查询
     */
    public void getProductById(BaseActivity context, String getProductById, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", getProductById);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getProductById(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<ProductInfo>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(ProductInfo login) {
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
     * 2.3.1 商品详细信息标识查询
     */
    public void getProductDetailById(BaseActivity context, String productDetailId, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", productDetailId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getProductDetailById(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<ProductDetailById>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(ProductDetailById login) {
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
     * 商品详细信息标识查询
     */
    public void getProductSKuAttr(BaseActivity context, String productId, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("productId", productId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getProductSKuAttr(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<ProductSKuAttr>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(ProductSKuAttr login) {
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
     * 商品评价列表查询
     */
    public void getEvaluationList(BaseActivity context,
                                  String type,
                                  String productId,
                                  int pageNo,
                                  int pageSize, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", type); //评价类型(1:商品评价类别,2:店铺评价类别)
            if (productId != null) {
                jsonObject.put("productId", productId); //商品标识
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getEvaluationList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), pageNo, pageSize)
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<EvaluationList>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<EvaluationList> login) {
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
     * 商品评价列表查询
     */
    public void getSkuList(BaseActivity context,
                           String productId,
                           final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("productId", productId); //商品标识
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getSkuList(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<SkuList>>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(List<SkuList> login) {
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
     * 商品删除
     */
    @SuppressWarnings("unchecked")
    public void delProductForm(BaseActivity context, String id, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().delProductForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
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
     * 2.2.2 商品修改
     */
    @SuppressWarnings("unchecked")
    public void updateProductForm(BaseActivity context,
                                  String id,
                                  String memberId,
                                  String name,
                                  String shortDesc,
                                  String price,//商城价
                                  String settlementPrice,//结算价
                                  String applyImgs,
                                  String payType,
                                  String isTransfer,
                                  String isCashBack,
                                  String auditState,
                                  final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("memberId", memberId);
            if (name != null) {
                jsonObject.put("name", name);
            }
            if (shortDesc != null) {
                jsonObject.put("shortDesc", shortDesc);
            }

            if (price != null) {
                jsonObject.put("price", price);
            }
            if (settlementPrice != null) {
                jsonObject.put("settlementPrice", settlementPrice);
            }
            if (applyImgs != null) {
                jsonObject.put("applyImgs", applyImgs);
            }
            if (payType != null) {
                jsonObject.put("payType", payType);
            }
            if (isTransfer != null) {
                jsonObject.put("isTransfer", isTransfer);
            }
            if (isCashBack != null) {
                jsonObject.put("isCashBack", isCashBack);
            }
            if (auditState != null) {
                jsonObject.put("auditState", auditState);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().updateProductForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
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
     * 商品申请
     */
    @SuppressWarnings("unchecked")
    public void addProductForm(BaseActivity context,
                               String memberId,
                               String name,
                               String shortDesc,
                               String price,//商城价
                               String settlementPrice,//结算价
                               String applyImgs,
                               String payType,
                               String isTransfer,
                               String isCashBack,
                               String auditState,
                               final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("memberId", memberId);
            jsonObject.put("name", name);
            if (shortDesc != null) {
                jsonObject.put("shortDesc", shortDesc);
            }
            if (price != null) {
                jsonObject.put("price", price);
            }
            if (settlementPrice != null) {
                jsonObject.put("settlementPrice", settlementPrice);
            }
            if (applyImgs != null) {
                jsonObject.put("applyImgs", applyImgs);
            }
            if (payType != null) {
                jsonObject.put("payType", payType);
            }
            if (isTransfer != null) {
                jsonObject.put("isTransfer", isTransfer);
            }
            if (isCashBack != null) {
                jsonObject.put("isCashBack", isCashBack);
            }
            if (auditState != null) {
                jsonObject.put("auditState", auditState);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().addProductForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
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
