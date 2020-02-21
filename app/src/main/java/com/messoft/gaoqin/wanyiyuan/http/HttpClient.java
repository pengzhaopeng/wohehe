package com.messoft.gaoqin.wanyiyuan.http;

import com.messoft.gaoqin.wanyiyuan.bean.AddressBean;
import com.messoft.gaoqin.wanyiyuan.bean.AgentMemberIndexInfo;
import com.messoft.gaoqin.wanyiyuan.bean.AgentMemberInfoDetail;
import com.messoft.gaoqin.wanyiyuan.bean.AvailableLimitAmount;
import com.messoft.gaoqin.wanyiyuan.bean.BaseBean;
import com.messoft.gaoqin.wanyiyuan.bean.BeanOrderInfoById;
import com.messoft.gaoqin.wanyiyuan.bean.BeanOrderInfoList;
import com.messoft.gaoqin.wanyiyuan.bean.BlankCard;
import com.messoft.gaoqin.wanyiyuan.bean.ConfigValue;
import com.messoft.gaoqin.wanyiyuan.bean.DictionaryList;
import com.messoft.gaoqin.wanyiyuan.bean.EvaluationList;
import com.messoft.gaoqin.wanyiyuan.bean.EvaluationTagList;
import com.messoft.gaoqin.wanyiyuan.bean.ExistPayPassword;
import com.messoft.gaoqin.wanyiyuan.bean.GetAreaList;
import com.messoft.gaoqin.wanyiyuan.bean.GetBeanOrderAppealList;
import com.messoft.gaoqin.wanyiyuan.bean.GetCartList;
import com.messoft.gaoqin.wanyiyuan.bean.GetMemberNotifyList;
import com.messoft.gaoqin.wanyiyuan.bean.GetMemberPointsLogList;
import com.messoft.gaoqin.wanyiyuan.bean.GetMemberWholesaleRushListByCache;
import com.messoft.gaoqin.wanyiyuan.bean.GetNewsList;
import com.messoft.gaoqin.wanyiyuan.bean.GetRushGoodsList;
import com.messoft.gaoqin.wanyiyuan.bean.GetRushGoodsTypeList;
import com.messoft.gaoqin.wanyiyuan.bean.GetRushOrderList;
import com.messoft.gaoqin.wanyiyuan.bean.HomeBanner;
import com.messoft.gaoqin.wanyiyuan.bean.LoginBean;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.bean.MemberAvailableAmount;
import com.messoft.gaoqin.wanyiyuan.bean.MemberBillList;
import com.messoft.gaoqin.wanyiyuan.bean.MemberBillStatistics;
import com.messoft.gaoqin.wanyiyuan.bean.MemberCapitalLogList;
import com.messoft.gaoqin.wanyiyuan.bean.MemberCapitalWaitLogList;
import com.messoft.gaoqin.wanyiyuan.bean.MemberCapitalWaitLogSummary;
import com.messoft.gaoqin.wanyiyuan.bean.MemberInfoList;
import com.messoft.gaoqin.wanyiyuan.bean.MemberSettlementLineList;
import com.messoft.gaoqin.wanyiyuan.bean.MemberSettlementLineStatistics;
import com.messoft.gaoqin.wanyiyuan.bean.MemberSettlementLis;
import com.messoft.gaoqin.wanyiyuan.bean.MemberUplevelAmount;
import com.messoft.gaoqin.wanyiyuan.bean.MemberWholesaleCapitalList;
import com.messoft.gaoqin.wanyiyuan.bean.OrderInfo;
import com.messoft.gaoqin.wanyiyuan.bean.OrderItemList;
import com.messoft.gaoqin.wanyiyuan.bean.OrderStatusCount;
import com.messoft.gaoqin.wanyiyuan.bean.PayOrderByWx;
import com.messoft.gaoqin.wanyiyuan.bean.PayOrderByZfb;
import com.messoft.gaoqin.wanyiyuan.bean.PaymentMethodList;
import com.messoft.gaoqin.wanyiyuan.bean.ProductDetailById;
import com.messoft.gaoqin.wanyiyuan.bean.ProductInfo;
import com.messoft.gaoqin.wanyiyuan.bean.ProductSKuAttr;
import com.messoft.gaoqin.wanyiyuan.bean.ProductType;
import com.messoft.gaoqin.wanyiyuan.bean.ReturnApplyList;
import com.messoft.gaoqin.wanyiyuan.bean.SendSms;
import com.messoft.gaoqin.wanyiyuan.bean.SkuList;
import com.messoft.gaoqin.wanyiyuan.bean.TransferMemberInfo;
import com.messoft.gaoqin.wanyiyuan.bean.UploadFile;
import com.messoft.gaoqin.wanyiyuan.bean.WholesaleRush;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public interface HttpClient {

    class Builder {

        public static HttpClient getWYYServer() {
            return HttpUtils.getInstance().getJavaWYYServer(HttpClient.class);
        }

        public static HttpClient getMemberServer() {
            return HttpUtils.getInstance().getJavaMemberServer(HttpClient.class);
        }

        public static HttpClient getJavaCommonServer() {
            return HttpUtils.getInstance().getJavaCommonServer(HttpClient.class);
        }

        public static HttpClient getJavaQgServer() {
            return HttpUtils.getInstance().getJavaQgServer(HttpClient.class);
        }
    }

    /**
     * 3.1.1 手机号码注册
     */
    @FormUrlEncoded
    @POST("/account/account.do?action=reg")
    Observable<BaseBean<LoginBean>> register(@Field("data") String data);

    /**
     * 3.1.4 会员登录
     */
    @FormUrlEncoded
    @POST("/account/account.do?action=login")
    Observable<BaseBean<LoginBean>> signIn(@Field("data") String data);

    /**
     * 注销
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=cancelAccount")
    Observable<BaseBean> cancelAccount(@Field("data") String data);

    /**
     * 3.2.1 发送短信验证码
     */
    @FormUrlEncoded
    @POST("/verificationCode/verificationCode.do?action=getVerificationCode")
    Observable<BaseBean<SendSms>> sendSms(@Field("data") String data);

    /**
     * 2.1.8 查询登录人信息
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=getLoginMemberInfo")
    Observable<BaseBean<LoginMemberInfo>> getLoginMemberInfo(@Field("data") String data);

    /**
     * 2.1.10 修改登录人信息
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=updateLoginMemberInfo")
    Observable<BaseBean> updateLoginMemberInfo(@Field("data") String data);

    /**
     * 2.1.11 是否存在支付密码
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=existPayPassword")
    Observable<BaseBean<ExistPayPassword>> existPayPassword(@Field("data") String data);

    /**
     * 2.1.12 登录密码验证
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=validatePassword")
    Observable<BaseBean> validatePassword(@Field("data") String data);

    /**
     * 2.1.12 支付密码验证
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=validatePayPassword")
    Observable<BaseBean> validatePayPassword(@Field("data") String data);

    /**
     * 2.1.14 支付密码设置/修改
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=modifyPayPassword")
    Observable<BaseBean> modifyPayPassword(@Field("data") String data);

    /**
     * 2.1.15 支付密码重置
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=resetPayPassword")
    Observable<BaseBean> resetPayPassword(@Field("data") String data);

    /**
     * 2.1.16 账号密码修改
     */
    @FormUrlEncoded
    @POST("/account/account.do?action=updatePassword")
    Observable<BaseBean> updatePassword(@Field("data") String data);

    /**
     * 2.1.16 账号密码修改
     */
    @FormUrlEncoded
    @POST("/account/account.do?action=resetPassword")
    Observable<BaseBean> resetPassword(@Field("data") String data);


    /**
     * 数据字典
     */
    @FormUrlEncoded
    @POST("/dataDictionary/dataDictionary.do?action=getDataDictionaryList")
    Observable<BaseBean<List<DictionaryList>>> getDataDictionaryList(@Field("data") String data);

    /**
     * 文件上传
     */
    @FormUrlEncoded
    @POST("/upload/upload.do?action=uploadFile")
    Observable<BaseBean<UploadFile>> uploadFile(@Field("moduleName") String moduleName,
                                                @Field("extName") String extName,
                                                @Field("fileStr") String fileStr);


    /**
     * java
     * 2.3.3 区域列表查询
     */
    @FormUrlEncoded
    @POST("/area/area.do?action=getAreaList")
    Observable<BaseBean<List<GetAreaList>>> getAreaList(@Field("data") String data);

    /**
     * 收货地址列表
     */
    @FormUrlEncoded
    @POST("/memberAddress/memberAddress.do?action=getMemberAddressList")
    Observable<BaseBean<List<AddressBean>>> getAddress(@Field("data") String data);

    /**
     * 3.1.11 添加收货地址
     */
    @FormUrlEncoded
    @POST("/memberAddress/memberAddress.do?action=addMemberAddressForm")
    Observable<BaseBean> addAddress(@Field("data") String data);

    /**
     * 3.1.14 删除收货地址
     */
    @FormUrlEncoded
    @POST("/memberAddress/memberAddress.do?action=delMemberAddressForm")
    Observable<BaseBean> deleteAddress(@Field("data") String data);

    /**
     * 更新收货地址
     */
    @FormUrlEncoded
    @POST("/memberAddress/memberAddress.do?action=updateMemberAddressForm")
    Observable<BaseBean> updateMemberAddressForm(@Field("data") String data);

    /**
     * 银行卡列表
     */
    @FormUrlEncoded
    @POST("/memberBank/memberBank.do?action=getMemberBankList")
    Observable<BaseBean<List<BlankCard>>> getMemberBankList(@Field("data") String data);

    /**
     * 银行卡新增
     */
    @FormUrlEncoded
    @POST("/memberBank/memberBank.do?action=addMemberBankForm")
    Observable<BaseBean> addMemberBankForm(@Field("data") String data);

    /**
     * 银行卡修改
     */
    @FormUrlEncoded
    @POST("/memberBank/memberBank.do?action=updateMemberBankForm")
    Observable<BaseBean> updateMemberBankForm(@Field("data") String data);

    /**
     * 银行卡删除
     */
    @FormUrlEncoded
    @POST("/memberBank/memberBank.do?action=delMemberBankForm")
    Observable<BaseBean> delMemberBankForm(@Field("data") String data);

    /**
     * 首页轮播图
     */
    @FormUrlEncoded
    @POST("/image/image.do?action=getImageList")
    Observable<BaseBean<List<HomeBanner>>> getImageList(@Field("data") String data);

    /**
     * 2.1.1 商品分类列表查询
     */
    @FormUrlEncoded
    @POST("/class/class.do?action=getClassList")
    Observable<BaseBean<List<ProductType>>> getProductType(@Field("data") String data);

    /**
     * 2.2.4 商品列表查询
     */
    @FormUrlEncoded
    @POST("/product/product.do?action=getProductList")
    Observable<BaseBean<List<ProductInfo>>> getProductList(@Field("data") String data,
                                                           @Field("pageNo") int pageNo,
                                                           @Field("pageSize") int pageSize);

    /**
     * 2.2.3 商品标识查询
     */
    @FormUrlEncoded
    @POST("/product/product.do?action=getProductById")
    Observable<BaseBean<ProductInfo>> getProductById(@Field("data") String data);

    /**
     * 2.3.1 商品详细信息标识查询
     */
    @FormUrlEncoded
    @POST("/product/product.do?action=getProductDetailById")
    Observable<BaseBean<ProductDetailById>> getProductDetailById(@Field("data") String data);

    /**
     * 2.3.1 商品详细信息标识查询
     */
    @FormUrlEncoded
    @POST("/product/product.do?action=getProductSKuAttr")
    Observable<BaseBean<ProductSKuAttr>> getProductSKuAttr(@Field("data") String data);

    /**
     * 2.2.5 商品删除
     */
    @FormUrlEncoded
    @POST("/product/product.do?action=delProductForm")
    Observable<BaseBean> delProductForm(@Field("data") String data);

    /**
     * 2.2.2 商品修改
     */
    @FormUrlEncoded
    @POST("/product/product.do?action=updateProductForm")
    Observable<BaseBean> updateProductForm(@Field("data") String data);

    /**
     * 2.2.1 商品申请
     */
    @FormUrlEncoded
    @POST("/product/product.do?action=addProductForm")
    Observable<BaseBean> addProductForm(@Field("data") String data);

    /**
     * 2.6.2 评价列表查询
     */
    @FormUrlEncoded
    @POST("/evaluation/evaluation.do?action=getEvaluationList")
    Observable<BaseBean<List<EvaluationList>>> getEvaluationList(@Field("data") String data,
                                                                 @Field("pageNo") int pageNo,
                                                                 @Field("pageSize") int pageSize);

    /**
     * 2.5.2 SKU列表查询
     */
    @FormUrlEncoded
    @POST("/sku/sku.do?action=getSkuList")
    Observable<BaseBean<List<SkuList>>> getSkuList(@Field("data") String data);

    /**
     * 2.8.1 订单新增
     */
    @FormUrlEncoded
    @POST("/order/order.do?action=addOrderForm")
    Observable<BaseBean<List<OrderInfo>>> addOrderForm(@Field("data") String data);

    /**
     * 2.8.3 订单列表查询
     */
    @FormUrlEncoded
    @POST("/order/order.do?action=getOrderList")
    Observable<BaseBean<List<OrderInfo>>> getOrderList(@Field("data") String data,
                                                       @Field("pageNo") int pageNo,
                                                       @Field("pageSize") int pageSize);


    /**
     * 2.8.2 订单标识查询
     */
    @FormUrlEncoded
    @POST("order/order.do?action=getOrderById")
    Observable<BaseBean<OrderInfo>> getOrderById(@Field("data") String data);

    /**
     * 更改订单状态
     */
    @FormUrlEncoded
    @POST("/order/order.do?action=updateOrderStatus")
    Observable<BaseBean> updateOrderStatus(@Field("data") String data);

    /**
     * 2.8.1 订单项提货券转让
     */
    @FormUrlEncoded
    @POST("/order/order.do?action=updateOrderItemForm")
    Observable<BaseBean> updateOrderItemForm(@Field("data") String data);


    /**
     * 2.6.1 商品评价新增
     */
    @FormUrlEncoded
    @POST("/evaluation/evaluation.do?action=addEvaluationForm")
    Observable<BaseBean> addEvaluationForm(@Field("data") String data);

    /**
     * 2.6.3 评价标签列表查询
     */
    @FormUrlEncoded
    @POST("/evaluation/evaluation.do?action=getEvaluationTagList")
    Observable<BaseBean<List<EvaluationTagList>>> getEvaluationTagList(@Field("data") String data);



    /**
     * 2.8.4 会员余额明细列表查询
     */
    @FormUrlEncoded
    @POST("/memberCapitalLog/memberCapitalLog.do?action=getMemberCapitalLogList")
    Observable<BaseBean<List<MemberCapitalLogList>>> getMemberCapitalLogList(@Field("data") String data,
                                                                             @Field("pageNo") int pageNo,
                                                                             @Field("pageSize") int pageSize);


    /**
     * 2.5.2 提现手续费率查询
     */
    @FormUrlEncoded
    @POST("/config/config.do?action=getConfigValue")
    Observable<BaseBean<ConfigValue>> getConfigValue(@Field("data") String data);

    /**
     * 2.1.1 会员可用金额查询
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=getMemberAvailableAmount")
    Observable<BaseBean<MemberAvailableAmount>> getMemberAvailableAmount(@Field("data") String data);



    /**
     * 2.5.1 会员提现新增
     */
    @FormUrlEncoded
    @POST("/memberWithdraw/memberWithdraw.do?action=addMemberWithdrawForm")
    Observable<BaseBean> addMemberWithdrawForm(@Field("data") String data);

    /**
     * 2.6.1 会员转账新增
     */
    @FormUrlEncoded
    @POST("/memberTransfer/memberTransfer.do?action=addMemberTransferForm")
    Observable<BaseBean> addMemberTransferForm(@Field("data") String data);

    /**
     * 2.6.3 转账账号信息查询
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=getTransferMemberInfo")
    Observable<BaseBean<TransferMemberInfo>> getTransferMemberInfo(@Field("data") String data);

    /**
     * 2.11.2 会员账单列表查询
     */
    @FormUrlEncoded
    @POST("/memberBill/memberBill.do?action=getMemberBillList")
    Observable<BaseBean<List<MemberBillList>>> getMemberBillList(@Field("data") String data,
                                                                 @Field("pageNo") int pageNo,
                                                                 @Field("pageSize") int pageSize);

    /**
     * 2.10.1 会员资金待入明细列表查询
     */
    @FormUrlEncoded
    @POST("/memberCapitalWaitLog/memberCapitalWaitLog.do?action=getMemberCapitalWaitLogList")
    Observable<BaseBean<List<MemberCapitalWaitLogList>>> getMemberCapitalWaitLogList(@Field("data") String data,
                                                                                     @Field("pageNo") int pageNo,
                                                                                     @Field("pageSize") int pageSize);

    /**
     * 2.10.4 会员批发额度明细列表查询
     */
    @FormUrlEncoded
    @POST("/memberWholesaleCapital/memberWholesaleCapital.do?action=getMemberWholesaleCapitalList")
    Observable<BaseBean<List<MemberWholesaleCapitalList>>> getMemberWholesaleCapitalList(@Field("data") String data,
                                                                                         @Field("pageNo") int pageNo,
                                                                                         @Field("pageSize") int pageSize);

    /**
     * 2.10.2 会员资金待入明细汇总
     */
    @FormUrlEncoded
    @POST("/memberCapitalWaitLog/memberCapitalWaitLog.do?action=getMemberCapitalWaitLogSummary")
    Observable<BaseBean<MemberCapitalWaitLogSummary>> getMemberCapitalWaitLogSummary(@Field("data") String data);

    /**
     * 2.13.3 代理商收益统计查询
     */
    @FormUrlEncoded
    @POST("/memberSettlementLine/memberSettlementLine.do?action=getMemberSettlementLineStatistics")
    Observable<BaseBean<MemberSettlementLineStatistics>> getMemberSettlementLineStatistics(@Field("data") String data);

    /**
     * 2.12.2 团队列表查询
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=getMemberInfoList")
    Observable<BaseBean<List<MemberInfoList>>> getMemberInfoList(@Field("data") String data,
                                                                 @Field("pageNo") int pageNo,
                                                                 @Field("pageSize") int pageSize);

    /**
     *2.12.3 代理商详情查询
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=getAgentMemberInfoDetail")
    Observable<BaseBean<AgentMemberInfoDetail>> getAgentMemberInfoDetail(@Field("data") String data);

    /**
     * 2.8.1 订单项列表查询
     */
    @FormUrlEncoded
    @POST("/order/order.do?action=getOrderItemList")
    Observable<BaseBean<List<OrderItemList>>> getOrderItemList(@Field("data") String data,
                                                               @Field("pageNo") int pageNo,
                                                               @Field("pageSize") int pageSize);

    /**
     * 2.9.4 退换货申请列表查询
     */
    @FormUrlEncoded
    @POST("/returnApply/returnApply.do?action=getReturnApplyList")
    Observable<BaseBean<List<ReturnApplyList>>> getReturnApplyList(@Field("data") String data,
                                                                   @Field("pageNo") int pageNo,
                                                                   @Field("pageSize") int pageSize);

    /**
     * 2.9.1 退换货申请新增
     */
    @FormUrlEncoded
    @POST("/returnApply/returnApply.do?action=addReturnApplyForm")
    Observable<BaseBean> addReturnApplyForm(@Field("data") String data);

    /**
     * 2.13.2 结算/收益列表明细查询
     */
    @FormUrlEncoded
    @POST("/memberSettlementLine/memberSettlementLine.do?action=getMemberSettlementLineList")
    Observable<BaseBean<List<MemberSettlementLineList>>> getMemberSettlementLineList(@Field("data") String data,
                                                                                     @Field("pageNo") int pageNo,
                                                                                     @Field("pageSize") int pageSize);

    /**
     * 2.2.1 支付方式列表查询
     */
    @FormUrlEncoded
    @POST("paymentMethod/paymentMethod.do?action=getPaymentMethodList")
    Observable<BaseBean<List<PaymentMethodList>>> getPaymentMethodList(@Field("data") String data);

    /**
     * 钱包订单支付
     */
    @FormUrlEncoded
    @POST("/payTransaction/payTransaction.do?action=payOrder")
    Observable<BaseBean> payOrderByQb(@Field("data") String data);

    /**
     * 支付宝订单支付
     */
    @FormUrlEncoded
    @POST("/payTransaction/payTransaction.do?action=payOrder")
    Observable<BaseBean<PayOrderByZfb>> payOrderByZfb(@Field("data") String data);

    /**
     * 支付宝会员升级支付
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=updateMemberLevel")
    Observable<BaseBean<PayOrderByZfb>> updateMemberLevelByZfb(@Field("data") String data);

    /**
     * 支付宝置换手续费支付
     */
    @FormUrlEncoded
    @POST("/payTransaction/payTransaction.do?action=payOrderFee")
    Observable<BaseBean<PayOrderByZfb>> payOrderFeeByZfb(@Field("data") String data);

    /**
     * 支付宝订单充值
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=memberRecharge")
    Observable<BaseBean<PayOrderByZfb>> payRechargeByZfb(@Field("data") String data);

    /**
     * 支付宝生态分充值
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=membeRecologyPointsRecharge")
    Observable<BaseBean<PayOrderByZfb>> membeRecologyPointsRechargeByZfb(@Field("data") String data);

    /**
     * 2.1.19 会员升级金额查询
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=getMemberUplevelAmount")
    Observable<BaseBean<MemberUplevelAmount>> getMemberUplevelAmount(@Field("data") String data);

    /**
     * 微信订单支付
     */
    @FormUrlEncoded
    @POST("/payTransaction/payTransaction.do?action=payOrder")
    Observable<BaseBean<PayOrderByWx>> payOrderByWx(@Field("data") String data);

    /**
     * 微信2.1.20 会员升级支付
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=updateMemberLevel")
    Observable<BaseBean<PayOrderByWx>> updateMemberLevelByWx(@Field("data") String data);

    /**
     * 微信置换手续费支付
     */
    @FormUrlEncoded
    @POST("/payTransaction/payTransaction.do?action=payOrderFee")
    Observable<BaseBean<PayOrderByWx>> payOrderFeeByWx(@Field("data") String data);

    /**
     * 微信订单充值
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=memberRecharge")
    Observable<BaseBean<PayOrderByWx>> payRechargeByWx(@Field("data") String data);

    /**
     * 微信生态分充值
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=membeRecologyPointsRecharge")
    Observable<BaseBean<PayOrderByWx>> membeRecologyPointsRechargeByWx(@Field("data") String data);

    /**
     * 2.1.7 金豆挂售列表查询
     */
    @FormUrlEncoded
    @POST("/beanOrder/beanOrder.do?action=getBeanOrderInfoList")
    Observable<BaseBean<List<BeanOrderInfoList>>> getBeanOrderInfoList(@Field("data") String data,
                                                                       @Field("pageNo") int pageNo,
                                                                       @Field("pageSize") int pageSize);

    /**
     * 2.1.8 我挂售的金豆列表查询
     */
    @FormUrlEncoded
    @POST("/beanOrder/beanOrder.do?action=getMySaleBeanOrderList")
    Observable<BaseBean<List<BeanOrderInfoList>>> getMySaleBeanOrderList(@Field("data") String data,
                                                                       @Field("pageNo") int pageNo,
                                                                       @Field("pageSize") int pageSize);

    /**
     * 2.1.9 我采购的金豆列表查询
     */
    @FormUrlEncoded
    @POST("/beanOrder/beanOrder.do?action=getMyPurchaserBeanOrderList")
    Observable<BaseBean<List<BeanOrderInfoList>>> getMyPurchaserBeanOrderList(@Field("data") String data,
                                                                         @Field("pageNo") int pageNo,
                                                                         @Field("pageSize") int pageSize);

    /**
     *2.1.2 金豆详情查询
     */
    @FormUrlEncoded
    @POST("/beanOrder/beanOrder.do?action=getBeanOrderInfoById")
    Observable<BaseBean<BeanOrderInfoById>> getBeanOrderInfoById(@Field("data") String data);

    /**
     * 2.1.3 金豆充值
     */
    @FormUrlEncoded
    @POST("/beanOrder/beanOrder.do?action=rechargeBeanOrderForm")
    Observable<BaseBean> rechargeBeanOrderForm(@Field("data") String data);

    /**
     * 2.1.1 金豆挂售新增
     */
    @FormUrlEncoded
    @POST("beanOrder/beanOrder.do?action=addBeanOrderForm")
    Observable<BaseBean> addBeanOrderForm(@Field("data") String data);

    /**
     * 2.1.6 取消金豆订单
     */
    @FormUrlEncoded
    @POST("/beanOrder/beanOrder.do?action=cancelBeanOrderForm")
    Observable<BaseBean> cancelBeanOrderForm(@Field("data") String data);

    /**
     *2.1.5 金豆发货
     */
    @FormUrlEncoded
    @POST("/beanOrder/beanOrder.do?action=deliverBeanOrderForm")
    Observable<BaseBean> deliverBeanOrderForm(@Field("data") String data);

    /**
     * 2.1.4 金豆支付
     */
    @FormUrlEncoded
    @POST("/beanOrder/beanOrder.do?action=payBeanOrderForm")
    Observable<BaseBean> payBeanOrderForm(@Field("data") String data);

    /**
     * 2.2.1 金豆订单投诉新增
     */
    @FormUrlEncoded
    @POST("/beanOrderAppeal/beanOrderAppeal.do?action=addBeanOrderAppealForm")
    Observable<BaseBean> addBeanOrderAppealForm(@Field("data") String data);

    /**
     *2.11.1 会员账单统计查询
     */
    @FormUrlEncoded
    @POST("/memberBill/memberBill.do?action=getMemberBillStatistics")
    Observable<BaseBean<MemberBillStatistics>> getMemberBillStatistics(@Field("data") String data);

    /**
     *2.12.1 代理商首页信息查询
     */
    @FormUrlEncoded
    @POST("/member/member.do?action=getAgentMemberIndexInfo")
    Observable<BaseBean<AgentMemberIndexInfo>> getAgentMemberIndexInfo(@Field("data") String data);

    /**
     * 2.13.1 结算列表查询
     */
    @FormUrlEncoded
    @POST("/memberSettlement/memberSettlement.do?action=getMemberSettlementList")
    Observable<BaseBean<List<MemberSettlementLis>>> getMemberSettlementLis(@Field("data") String data,
                                                                           @Field("pageNo") int pageNo,
                                                                           @Field("pageSize") int pageSize);

    /**
     * 2.5.5 通知列表查询
     */
    @FormUrlEncoded
    @POST("/notify/notify.do?action=getMemberNotifyList")
    Observable<BaseBean<List<GetMemberNotifyList>>> getMemberNotifyList(@Field("data") String data,
                                                                        @Field("pageNo") int pageNo,
                                                                        @Field("pageSize") int pageSize);

    /**
     * 2.5.4 拉取信息
     */
    @FormUrlEncoded
    @POST("/notify/notify.do?action=pull")
    Observable<BaseBean> pullNews(@Field("data") String data);

    /**
     * 标记已读
     */
    @FormUrlEncoded
    @POST("/notify/notify.do?action=read")
    Observable<BaseBean> isReadNewsList(@Field("data") String data);

    /**
     * 下单校验
     * http://wyy.ms.com/classConfig/classConfig.do?action=getAvailableLimitAmount&data={memberId:76,skuId:157,classId:1}
     */
    @FormUrlEncoded
    @POST("/classConfig/classConfig.do?action=getAvailableLimitAmount")
    Observable<BaseBean<AvailableLimitAmount>> getAvailableLimitAmount(@Field("data") String data);

    /**
     * 2.8.1 订单状态统计查询
     */
    @FormUrlEncoded
    @POST("/order/order.do?action=getOrderStatusCount")
    Observable<BaseBean<OrderStatusCount>> getOrderStatusCount(@Field("data") String data);

    /**
     * 2.1.4 查询资讯列表
     */
    @FormUrlEncoded
    @POST("/news/news.do?action=getNewsList")
    Observable<BaseBean<List<GetNewsList>>> getNewsList(@Field("data") String data);

    /**
     * 2.7.4 购物车列表查询
     */
    @FormUrlEncoded
    @POST("/cart/cart.do?action=getCartList")
    Observable<BaseBean<List<GetCartList>>> getCartList(@Field("data") String data);

    /**
     * 2.7.4 购物车新增
     */
    @FormUrlEncoded
    @POST("/cart/cart.do?action=addCartForm")
    Observable<BaseBean> addCartForm(@Field("data") String data);

    /**
     * 2.7.4 购物车修改
     */
    @FormUrlEncoded
    @POST("/cart/cart.do?action=updateCartForm")
    Observable<BaseBean> updateCartForm(@Field("data") String data);

    /**
     * 2.7.5 购物车删除
     */
    @FormUrlEncoded
    @POST("/cart/cart.do?action=delCartForm")
    Observable<BaseBean> delCartForm(@Field("data") String data);

    /**
     * 2.14.2 会员积分明细列表查询
     */
    @FormUrlEncoded
    @POST("/memberPointsLog/memberPointsLog.do?action=getMemberPointsLogList")
    Observable<BaseBean<List<GetMemberPointsLogList>>> getMemberPointsLogList(@Field("data") String data,
                                                                              @Field("pageNo") int pageNo,
                                                                              @Field("pageSize") int pageSize);

    /**
     * 抢购列表
     */
    @FormUrlEncoded
    @POST("/rush/rushGoodsType.do?action=getRushGoodsTypeList")
    Observable<BaseBean<List<GetRushGoodsTypeList>>> getRushGoodsTypeList(@Field("data") String data,
                                                                          @Field("pageNo") int pageNo,
                                                                          @Field("pageSize") int pageSize);

    /**
     * 抢购列表
     */
    @FormUrlEncoded
    @POST("/rush/rushGoodsType.do?action=getRushGoodsTypeById")
    Observable<BaseBean<GetRushGoodsTypeList>> getRushGoodsTypeById(@Field("data") String data);

    /**
     * 我的抢购订单列表
     */
    @FormUrlEncoded
    @POST("/rush/rushOrder.do?action=getRushOrderList")
    Observable<BaseBean<List<GetRushOrderList>>> getRushOrderList(@Field("data") String data,
                                                                  @Field("pageNo") int pageNo,
                                                                  @Field("pageSize") int pageSize);

    /**
     * 我的抢购订单详情
     */
    @FormUrlEncoded
    @POST("/rush/rushOrder.do?action=getRushOrderById")
    Observable<BaseBean<GetRushOrderList>> getRushOrderById(@Field("data") String data);

    /**
     * 我的待寄售订单列表
     */
    @FormUrlEncoded
    @POST("/rush/rushGoods.do?action=getRushGoodsList")
    Observable<BaseBean<List<GetRushGoodsList>>> getRushGoodsList(@Field("data") String data,
                                                                  @Field("pageNo") int pageNo,
                                                                  @Field("pageSize") int pageSize);

    /**
     * 我的待寄售详情
     */
    @FormUrlEncoded
    @POST("/rush/rushGoods.do?action=getRushGoodsById")
    Observable<BaseBean<GetRushGoodsList>> getRushGoodsById(@Field("data") String data);


    /**
     * 立即抢购
     */
    @FormUrlEncoded
    @POST("/rush/rushOrder.do?action=AutoAllotGoodsOrder")
    Observable<BaseBean> autoAllotGoodsOrder(@Field("data") String data);

    /**
     * 新增申请预约抢购
     */
    @FormUrlEncoded
    @POST("/rush/rushOrder.do?action=addRushOrderForm")
    Observable<BaseBean> addRushOrderForm(@Field("data") String data);

    /**
     * 1.16 抢购订单编辑（更新地址）+ （新增我要寄售）
     */
    @FormUrlEncoded
    @POST("/rush/rushOrder.do?action=updateRushOrderForm")
    Observable<BaseBean> updateRushOrderForm(@Field("data") String data);

    /**
     * 1.18 寄售 (抢购订单申请置换)+(申请置换)
     */
    @FormUrlEncoded
    @POST("/rush/rushOrder.do?action=AutoAllotOrder2Goods")
    Observable<BaseBean> autoAllotOrder2Goods(@Field("data") String data);

    /**
     * 卖家申请寄售
     */
    @FormUrlEncoded
    @POST("/rush/rushGoods.do?action=updateRushGoodsForm")
    Observable<BaseBean> updateRushGoodsForm(@Field("data") String data);

    /**
     * 1.27 金豆投诉单列表查询
     */
    @FormUrlEncoded
    @POST("/beanOrderAppeal/beanOrderAppeal.do?action=getBeanOrderAppealList")
    Observable<BaseBean<List<GetBeanOrderAppealList>>> getBeanOrderAppealList(@Field("data") String data,
                                                                              @Field("pageNo") int pageNo,
                                                                              @Field("pageSize") int pageSize);
    /**
     * 1.1 投诉单更新
     */
    @FormUrlEncoded
    @POST("/beanOrderAppeal/beanOrderAppeal.do?action=updateAuditBeanOrderAppealForm")
    Observable<BaseBean> updateAuditBeanOrderAppealForm(@Field("data") String data);

    /**
     * 转让订单
     */
    @FormUrlEncoded
    @POST("/rush/rushOrder.do?action=updateRushOrderCall")
    Observable<BaseBean> updateRushOrderCall(@Field("data") String data);

    /**
     * 2.15 批发额度抢购列表
     */
    @FormUrlEncoded
    @POST("/memberWholesaleRush/memberWholesaleRush.do?action=getMemberWholesaleRushListByCache")
    Observable<BaseBean<List<GetMemberWholesaleRushListByCache>>> getMemberWholesaleRushListByCache(@Field("data") String data);

    /**
     * 2.15.2 批发额度抢购
     */
    @FormUrlEncoded
    @POST("/memberWholesaleRush/memberWholesaleRush.do?action=wholesaleRush")
    Observable<BaseBean<WholesaleRush>> wholesaleRush(@Field("data") String data);
}
