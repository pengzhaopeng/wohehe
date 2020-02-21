package com.messoft.gaoqin.wanyiyuan.app;

public class Constants {

    //bindAccountId
    public static String bindAccountId = "";

    //push 推送通知栏点击 当app杀死后传递参数
    public static final String EXTRA_BUNDLE = "JPUSH_EXTRA_BUNDLE";

    //预览图片
    public static int REQUEST_CODE_PHOTO_PREVIEW = 2;
    public static int REQUEST_CODE_CHOOSE_PHOTO = 1;

    //域名
//    public static String URL_WYY = "http://mstest-wyy.mesandbox.com/";
    public static String URL_WYY = "http://ms-wyy.bybtb.com/";
//    public static String URL_MEMBER = "http://mstest-member.mesandbox.com/";
    public static String URL_MEMBER = "http://ms-member.messandbox.com/";
//    public static String URL_COMMON = "http://mstest-common.mesandbox.com/";
    public static String URL_COMMON = "http://ms-common.messandbox.com/";
    //抢购
//    public static String URL_QG = "http://t-wyyqg.mesandbox.com/";
    public static String URL_QG = "http://wyyqg.messandbox.com/";

    //文件主域
//    public static final String MASTER_URL = "http://mstest-image.mesandbox.com/";
    public static final String MASTER_URL = "http://ms-image.messandbox.com/";

    // 1-订单支付完成（零售区）  2-订单支付完成（批发区） 3-充值完成
    public static int ZFB_PAY_CODE;
    public static int ZFB_PAY_ORDER_LSQ = 1;
    public static int ZFB_PAY_ORDER_PFQ = 2;
    public static int ZFB_PAY_RECHARGE = 3; //包含会员升级

    //微信AppID
    public static final String WEIXIN_APP_ID = "wx8b1caefe11f1462f";
    //微信AppSecret
    public static final String WEIXIN_APP_SECRET = "6f84fd6a9d129da5a10b6f02d2b1f173";
    //支付方式编号: 00001:金豆支付 00002:APP微信支付 00003:APP支付宝支付
    public static String PAY_TYPE_QB = "00001";
    public static String PAY_TYPE_ZFB = "00003";
    public static String PAY_TYPE_WX = "00002";
    //标志当前微信支付的from 1-订单支付完成（零售区）  2-订单支付完成（批发区） 3-充值完成
    public static int WX_PAY_CODE;
    public static int WX_PAY_ORDER_LSQ = 1;
    public static int WX_PAY_ORDER_PFQ = 2;
    public static int WX_PAY_RECHARGE = 3; //包含会员升级
}
