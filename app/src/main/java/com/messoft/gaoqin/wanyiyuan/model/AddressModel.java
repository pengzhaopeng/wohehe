package com.messoft.gaoqin.wanyiyuan.model;


import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.AddressBean;
import com.messoft.gaoqin.wanyiyuan.bean.BaseBean;
import com.messoft.gaoqin.wanyiyuan.bean.GetAreaList;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;

import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

public class AddressModel {

    /**
     * 2.3.3 区域列表查询
     */
    public void getAreaList(BaseActivity context, String parentId, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("parentId", parentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getJavaCommonServer().getAreaList(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.<BaseBean<List<GetAreaList>>>compose())
                .compose(context.<BaseBean<List<GetAreaList>>>bindToLifecycle())
                .subscribe(new BaseObserver<List<GetAreaList>>(context, false, "加载中...") {
                    @Override
                    protected void onSuccess(List<GetAreaList> login) {
                        if (login != null) {
                            Collections.reverse(login);
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
     * 3.1.10 获取收货地址列表
     */
    public void getAddress(BaseActivity context,  final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sortField", "isDefault");
            jsonObject.put("sortOrder", "desc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getAddress(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(context.bindToLifecycle())
                .subscribe(new BaseObserver<List<AddressBean>>(context, true, "加载中...") {
                    @Override
                    protected void onSuccess(List<AddressBean> login) {
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
     * 3.1.11 添加收货地址
     */
    @SuppressWarnings("unchecked")
    public void addAddress(BaseActivity context,
                           String contactName,
                           String contactMobile,
                           String isDefault,
                           String province,
                           String provinceText,
                           String city,
                           String cityText,
                           String district,
                           String districtText,
                           String address,
                           final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (contactName != null) {
                jsonObject.put("contactName", contactName);
            }
            if (contactMobile != null) {
                jsonObject.put("contactMobile", contactMobile);
            }
            if (isDefault != null) {
                jsonObject.put("isDefault", isDefault);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().addAddress(jsonObject.toString())
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
     * 3.1.14 删除收货地址
     */
    @SuppressWarnings("unchecked")
    public void deleteAddress(BaseActivity context, String aid, final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", aid);
//            jsonObject.put("bind_account_id", BusinessUtils.getBindAccount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().deleteAddress(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.<BaseBean>compose())
                .compose(context.<BaseBean>bindToLifecycle())
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
     * 更新认收货地址
     */
    @SuppressWarnings("unchecked")
    public void updateMemberAddressForm(BaseActivity context,
                                  String id,
                                  String contactName,
                                  String contactMobile,
                                  String isDefault,
                                  String province,
                                  String provinceText,
                                  String city,
                                  String cityText,
                                  String district,
                                  String districtText,
                                  String address,
                                  final RequestImpl listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            if (contactName != null) {
                jsonObject.put("contactName", contactName);
            }
            if (contactMobile != null) {
                jsonObject.put("contactMobile", contactMobile);
            }
            if (isDefault != null) {
                jsonObject.put("isDefault", isDefault);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().updateMemberAddressForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
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
