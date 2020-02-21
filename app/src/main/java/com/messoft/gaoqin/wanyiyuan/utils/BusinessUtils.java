package com.messoft.gaoqin.wanyiyuan.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.widget.TextView;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.ActivityManage;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.ui.MainActivity;


public class BusinessUtils {

    /**
     * 保存用户信息
     *
     * @param login MobileSignUp
     */
    public static void saveUserData(LoginMemberInfo login) {
        if (login == null) {
            return;
        }
        Constants.bindAccountId = login.getId();
        SPUtils.putBoolean("isLogin", true);
        //绑定的会员id
        SPUtils.putString("bind_account_id", login.getId());
        SPUtils.putObject("LoginMemberInfo", login);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static LoginMemberInfo getUserData() {
        return (LoginMemberInfo) SPUtils.getObject("LoginMemberInfo");
    }

    /**
     * 存token
     *
     * @param assessToken
     */
    public static void setToken(String assessToken) {
        SPUtils.putString("accessToken", assessToken);
    }

    /**
     * 获取token
     *
     * @return
     */
    public static String getToken() {
        return SPUtils.getString("accessToken", "");
    }

    /**
     * 存secret
     *
     * @param assessToken
     */
    public static void setSecret(String assessToken) {
        SPUtils.putString("secret", assessToken);
    }

    /**
     * 获取secret
     *
     * @return
     */
    public static String getSecret() {
        return SPUtils.getString("secret", "");
    }

    /**
     * 获取会员账号id
     */
    public static String getBindAccountId() {
        if (StringUtils.isNoEmpty(Constants.bindAccountId)) {
            return Constants.bindAccountId;
        }
        return SPUtils.getString("bind_account_id", "");
    }

    /**
     * 存UserAgree
     *
     * @param isAgree
     */
    public static void setUserAgree(boolean isAgree) {
        SPUtils.putBoolean("_isAgree", isAgree);
    }

    /**
     * 获取UserAgree
     *
     * @return
     */
    public static boolean getUserAgree() {
        return SPUtils.getBoolean("_isAgree", false);
    }

    /**
     * 获取手机号
     *
     * @return
     */
    public static String getMobile() {
        LoginMemberInfo userData = getUserData();
        if (userData != null && userData.getMobile() != null) {
            return userData.getMobile();
        }
        return "";
    }

    /**
     * 获取账号
     *
     * @return
     */
    public static String getAccount() {
        LoginMemberInfo userData = getUserData();
        if (userData != null && userData.getAccount() != null) {
            return userData.getAccount();
        }
        return "";
    }

    /***
     * 退出登录跳转到登录页
     */
    public static <T> void loginOut(Activity fromActivity, Class<T> cla) {
        if (fromActivity == null || cla == null) {
            return;
        }
        SPUtils.putBoolean("isLogin", false);
        BusinessUtils.setToken(null);
        BusinessUtils.setSecret(null);
        BusinessUtils.setUserAgree(false);

        //断开webSocket
//        WebSocketService.sendSocketSignal(fromActivity, 0);
        ActivityManage.finishActivity(MainActivity.class);
        Intent in = new Intent(fromActivity, cla);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        fromActivity.startActivity(in);
        fromActivity.finish();
    }

    /**
     * 获得银行卡背景和logo
     * bank_type_zsyh：招商银行
     * bank_type_gsyh：工商银行
     * bank_type_jsyh：建设银行
     * bank_type_nyyh：农业银行
     * bank_type_zgyh：中国银行
     * bank_type_yzyh：邮政储蓄银行
     * bank_type_msyh：民生银行
     * bank_type_jtyh：交通银行
     * bank_type_payn：平安银行
     * bank_type_pfyn：浦发银行
     * bank_type_zxyh：中信银行
     *
     * @return
     */
    public static int[] getBlankBgAndLog(String typeCode) {
        int[] imgs = new int[3];
        switch (typeCode) {
            case "bank_type_zsyh": //交通银行
                imgs[0] = R.drawable.zhaoshang_bg;
                imgs[1] = R.drawable.zhaoshang_logo;
                break;
            case "bank_type_gsyh": //工商银行
                imgs[0] = R.drawable.gongshang_bg;
                imgs[1] = R.drawable.gongshang_logo;
                break;
            case "bank_type_jtyh": //交通银行
                imgs[0] = R.drawable.jiaotong_bg;
                imgs[1] = R.drawable.jiaotong_logo;
                break;
            case "bank_type_jsyh": //交通银行
                imgs[0] = R.drawable.jianshe_bg;
                imgs[1] = R.drawable.jianshe_logo;
                break;
            case "bank_type_nyyh": //交通银行
                imgs[0] = R.drawable.nonghang_bg;
                imgs[1] = R.drawable.nonghang_logo;
                break;
            case "bank_type_zgyh": //交通银行
                imgs[0] = R.drawable.zhongguo_bg;
                imgs[1] = R.drawable.zhongguo_logo;
                break;
            case "bank_type_yzyh": //交通银行
                imgs[0] = R.drawable.youzheng_bg;
                imgs[1] = R.drawable.youzheng_logo;
                break;
            case "bank_type_msyh": //交通银行
                imgs[0] = R.drawable.mingsheng_bg;
                imgs[1] = R.drawable.mingsheng_logo;
                break;
            case "bank_type_payn": //交通银行
                imgs[0] = R.drawable.pingan_bg;
                imgs[1] = R.drawable.pingan_logo;
                break;
            case "bank_type_pfyn": //交通银行
                imgs[0] = R.drawable.pufa_bg;
                imgs[1] = R.drawable.pufa_logo;
                break;
            case "bank_type_zxyh": //交通银行
                imgs[0] = R.drawable.zhongxin_bg;
                imgs[1] = R.drawable.zhongxin_logo;
                break;
        }
        return imgs;
    }


    /**
     * 设置页面右上角按钮
     *
     * @param tvRight
     */
    public static void setRightTv(TextView tvRight, String str) {
        tvRight.setBackgroundResource(R.drawable.shape_sure_selecte);
        tvRight.setText(str);
        tvRight.setHeight(UIUtils.dip2Px(30));
        tvRight.setPadding(UIUtils.dip2Px(12), UIUtils.dip2Px(4), UIUtils.dip2Px(12), UIUtils.dip2Px(4));
        tvRight.setTextColor(UIUtils.getColor(R.color.white));
    }

    /**
     * 金额明细类型
     *
     * @param type
     * @return
     */
    public static String capitalType(String type) {
        if (type == null) {
            return "";
        }
        switch (type) {
            case "0":
                return "购买商品";
            case "1":
                return "零售返现";
            case "2":
                return "分销奖励";
            case "3":
                return "提现";
            case "4":
                return "提现失败";
            case "5":
                return "提现手续费";
            case "6":
                return "转账";
            case "7":
                return "转账手续费";
            case "8":
                return "充值";
            case "9":
                return "线下充值";
            case "10":
                return "vip升级";
            case "11":
                return "商品转让冻结金";
            case "12":
                return "取消商品转让";
            case "13":
                return "商品转让手续费";
            case "14":
                return "商品转让成功";
            case "15":
                return "商品转让手续费退回";
            case "16":
                return "提现手续费退回";
            case "17":
                return "转账手续费退回";
            case "18":
                return "提货权转让";
            case "21":
                return "订单退款";
            case "23":
                return "权益值转入";
            case "99":
                return "系统调整";

        }
        return "";
    }

    /**
     * 送货类型
     *
     * @param type
     * @return
     */
    public static String deliveryType(String type) {
        if (type == null) {
            return "任意时间";
        }
        switch (type) {
            case "1":
                return "只工作日送货(双休日、假日不用送)";
            case "2":
                return "只双休日、假日送货(工作日不用送)";
            case "3":
                return "工作日、双休日与假日均可送货";
        }
        return "任意时间";
    }

    /**
     * 角色（0.普通会员,1.付费会员,2.区代,3.市代,4.金牌市代,5.省代）
     */
    public static String memberLevel(String type) {
        switch (type) {
            case "0":
                return "普通会员";
            case "1":
                return "付费会员";
            case "2":
                return "区代";
            case "3":
                return "市代";
            case "4":
                return "金牌市代";
            case "5":
                return "省代";

        }
        return "";
    }

    /**
     * 子类型
     * (0.购买商品，1.置换返送 ，31.购买代金券，32.购买商品使用，61.充值，62.预约套餐，63.直接购买套餐，63.抢购失败退还 )
     * 0.购买商品，1.置换返送 ，2.订单取消退还 ，3.订单退款，31.购买代金券，（代金券相关）32.购买商品使用，33.订单取消退还，
     * 34.订单退款（生态分相关）61.充值，62.预约套餐，63.直接购买套餐，64.抢购失败退还 )
     */
    public static String subType(String type) {
        switch (type) {
            case "0":
                return "购买商品";
            case "1":
                return "置换返送";
            case "2":
                return "订单取消退还";
            case "3":
                return "订单退款";
            case "31":
                return "购买代金券（代金券相关）";
            case "32":
                return "购买商品使用";
            case "33":
                return "订单取消退还";
            case "34":
                return "订单退款（生态分相关）";
            case "61":
                return "充值";
            case "62":
                return "预约套餐";
            case "63":
                return "直接购买套餐";
            case "64":
                return "抢购失败退还";
            case "65":
                return "互销返送";
            case "66":
                return "转入余额";

        }
        return "";
    }

    /**
     * 红色价格
     * 设置 textView 显示价格和积分
     * 1.有积分无价格  100积分
     * 2.有价格无积分  ￥100
     * 3.有积分有价格  100积分 + ￥100
     */
    public static void setPriceAndPoints(TextView tvPrice, int textSize, long points, double price) {
        String price1 = NumberUtils.formatNumber(price);
        if (points != 0 && price != 0) { //3
            String str = String.format(
                    "<font style='font-size:" + textSize + "px'>" +
                            "<font  size=\"7\" color=\"#FF3B30\">%s</font>" +
                            "<font size=\"10\" color=\"#FF3B30\"><small>%s</small></font>" +
                            "<font size=\"10\">%s</font>" +
                            "<font size=\"10\" color=\"#2A2A2A\"><small>%s</small></font>" +
                            "<font size=\"10\">%s</font>" +
                            "<font size=\"10\" color=\"#FF3B30\"><small>%s</small></font>" +
                            "<font size=\"14\" color=\"#FF3B30\">%s</font>" +
                            "</font>",
                    points, "积分", "", "+", "", "¥", price1);
            tvPrice.setText(Html.fromHtml(str));
        } else if (points != 0) { //1
            String str = String.format(
                    "<font style='font-size:\"+textSize+\"px'>" +
                            "<font size=\"14\" color=\"#FF3B30\">%s</font>" +
                            "<font size=\"10\" color=\"#FF3B30\"><small>%s</small></font>" +
                            "</font>",
                    points, "积分");
            tvPrice.setText(Html.fromHtml(str));
        } else if (price != 0) { //2
            String str = String.format(
                    "<font style='font-size:\"+textSize+\"px'>" +
                            "<font size=\"10\" color=\"#FF3B30\"><small>%s</small></font>" +
                            "<font size=\"14\" color=\"#FF3B30\">%s</font>" +
                            "</font>",
                    "¥", price1);
            tvPrice.setText(Html.fromHtml(str));
        } else {
            String str = String.format(
                    "<font style='font-size:\"+textSize+\"px'>" +
                            "<font size=\"10\" color=\"#FF3B30\"><small>%s</small></font>" +
                            "<font size=\"14\" color=\"#FF3B30\">%s</font>" +
                            "</font>",
                    "¥", 0.00);
            tvPrice.setText(Html.fromHtml(str));
        }
    }

    /**
     * 黑色价格
     * 设置 textView 显示价格和积分
     * 1.有积分无价格  100积分
     * 2.有价格无积分  ￥100
     * 3.有积分有价格  100积分 + ￥100
     */
    public static void setPriceAndPoints1(TextView tvPrice, long points, double price) {
        String price1 = NumberUtils.formatNumber(price);
        if (points != 0 && price != 0) { //3
            String str = String.format(
                    "<font style='font-size:11px'>" +
                            "<font size=\"7\" color=\"#333333 \">%s</font>" +
                            "<font size=\"10\" color=\"#151515\"><small>%s</small></font>" +
                            "<font size=\"10\">%s</font>" +
                            "<font size=\"10\" color=\"#151515\"><small>%s</small></font>" +
                            "<font size=\"10\">%s</font>" +
                            "<font size=\"10\" color=\"#151515\"><small>%s</small></font>" +
                            "<font size=\"14\" color=\"#333333\">%s</font>" +
                            "</font>",
                    points, "积分", " ", "+", " ", "￥", price1);
            tvPrice.setText(Html.fromHtml(str));
        } else if (points != 0) { //1
            String str = String.format(
                    "<font style='font-size:11px'>" +
                            "<font size=\"14\" color=\"#333333\">%s</font>" +
                            "<font size=\"10\" color=\"#151515\">%s</font>" +
                            "</font>",
                    points, "积分");
            tvPrice.setText(Html.fromHtml(str));
        } else if (price != 0) { //2
            String str = String.format(
                    "<font style='font-size:11px'>" +
                            "<font size=\"10\" color=\"#151515\">%s</font>" +
                            "<font size=\"14\" color=\"#333333\">%s</font>" +
                            "</font>",
                    "￥", price1);
            tvPrice.setText(Html.fromHtml(str));
        }
    }
}
