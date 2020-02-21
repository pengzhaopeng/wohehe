package com.messoft.gaoqin.wanyiyuan.bean;

import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;

import java.util.List;

public class GetRushGoodsTypeList {


    /**
     * createTime : 2019-12-20 08:35:38
     * isSale : 1
     * currentPeople : 10
     * sort : 0
     * payCode :
     * urlImgs :
     * priceSection : gg
     * type : 1
     * memberAccount :
     * createBy : -100
     * cardNo :
     * endPanicTime : 2019-12-28 17:00:00
     * cardName :
     * isDel : 0
     * replacementFee : 1
     * order : 1
     * imgsList :
     * Content : gfdgd
     * updateBy : -100
     * cardId :
     * endSwapTime : 21:00:00
     * updateTime : 2019-12-20 10:17:23
     * channelId : 2
     * createName : 梵蒂冈
     * beginSwapTime : 15:00:00
     * orderList : [{"totalMedal":0,"goodsId":1,"goodsIntegral":2,"remark":"ert","shippingNumber":"ertert","street":"dg","type":0,"payTime":"","shippingCode":"erter","city":"dg","streetText":"dg","cardName":"","isVipShop":0,"memberRole":1,"goodsImg":"","isRevenue":0,"shopId":0,"userId":4,"province":"dfg","goodsPrice":"111","partner":0,"shippingName":"ert","settlementId":"1","updateBy":-100,"updateTime":"2019-12-25 10:01:32","status":9,"shippingRemark":"tertet","channelId":1,"totalIntegral":1,"number":2,"statusRatio":0,"isApplyLevel":0,"createName":"","isVipMember":0,"deliveryTime":"","goodsTypeId":1,"payType":"","memberId":4,"medal":1,"goodsName":"十多个","deleteTime":"","mobile":"666666666","tradeType":"2","createTime":"2019-12-23 18:33:09","transactionId":"34","sort":1,"payCode":"","provinceText":"fdg","memberAccount":"666666666","createBy":-100,"cardNo":"","isDel":0,"parentOrderId":"","name":"","parentGoodsId":1,"standardValue":100,"cityText":"g","district":"h","orderId":1,"totalPrice":"111","cardId":"","marketPrice":"23","percentage":"0","districtText":"h","shippingCheck":"","isEnvelopes":0,"orderNo":"s1111111","updateName":"","address":"dgf","distributionStatus":0,"completeTime":"","envelopesValue":0}]
     * typeName : 9少年
     * updateName :
     * StringervalDays : 1
     * goodsTypeId : 1
     * payType :
     * registerPeople : 10
     * beginPanicTime : 2019-12-28 11:00:00
     * ecoFee : 10
     */

    private String createTime;
    private String isSale;
    private String currentPeople;
    private String sort;
    private String payCode;
    private String urlImgs;
    private String priceSection;
    private String type;
    private String memberAccount;
    private String createBy;
    private String cardNo;
    private String endPanicTime;
    private String cardName;
    private String isDel;
    private String replacementFee;
    private String order;
    private List<String> imgsList;
    private String content;
    private String updateBy;
    private String cardId;
    private String endSwapTime;
    private String updateTime;
    private String channelId;
    private String createName;
    private String beginSwapTime;
    private String typeName;
    private String updateName;
    private String StringervalDays;
    private String goodsTypeId;
    private String payType;
    private String registerPeople;
    private String beginPanicTime;
    private String ecoFee;
    private List<OrderListBean> orderList;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIsSale() {
        return isSale;
    }

    public void setIsSale(String isSale) {
        this.isSale = isSale;
    }

    public String getCurrentPeople() {
        return currentPeople;
    }

    public void setCurrentPeople(String currentPeople) {
        this.currentPeople = currentPeople;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getUrlImgs() {
        return urlImgs;
    }

    public void setUrlImgs(String urlImgs) {
        this.urlImgs = urlImgs;
    }

    public String getPriceSection() {
        return priceSection;
    }

    public void setPriceSection(String priceSection) {
        this.priceSection = priceSection;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMemberAccount() {
        return memberAccount;
    }

    public void setMemberAccount(String memberAccount) {
        this.memberAccount = memberAccount;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getEndPanicTime() {
        return endPanicTime == null ? "" : endPanicTime;
    }

    public void setEndPanicTime(String endPanicTime) {
        this.endPanicTime = endPanicTime == null ? "" : endPanicTime;
    }

    public String getStringervalDays() {
        return StringervalDays == null ? "" : StringervalDays;
    }

    public void setStringervalDays(String stringervalDays) {
        StringervalDays = stringervalDays == null ? "" : stringervalDays;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getReplacementFee() {
        return replacementFee;
    }

    public void setReplacementFee(String replacementFee) {
        this.replacementFee = replacementFee;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public List<String> getImgsList() {
        return imgsList;
    }

    public void setImgsList(List<String> imgsList) {
        this.imgsList = imgsList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String Content) {
        this.content = Content;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getEndSwapTime() {
        return endSwapTime;
    }

    public void setEndSwapTime(String endSwapTime) {
        this.endSwapTime = endSwapTime;
    }

    public String getUpdateTime() {
        if (!StringUtils.isNoEmpty(updateTime)) {
            return getCreateTime();
        }
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getBeginSwapTime() {
        return beginSwapTime;
    }

    public void setBeginSwapTime(String beginSwapTime) {
        this.beginSwapTime = beginSwapTime;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getIntervalDays() {
        return StringervalDays;
    }

    public void setIntervalDays(String StringervalDays) {
        this.StringervalDays = StringervalDays;
    }

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getRegisterPeople() {
        return registerPeople;
    }

    public void setRegisterPeople(String registerPeople) {
        this.registerPeople = registerPeople;
    }

    public String getBeginPanicTime() {
        return beginPanicTime == null ? "" : beginPanicTime;
    }

    public void setBeginPanicTime(String beginPanicTime) {
        this.beginPanicTime = beginPanicTime == null ? "" : beginPanicTime;
    }

    public String getEcoFee() {
        return ecoFee;
    }

    public void setEcoFee(String ecoFee) {
        this.ecoFee = ecoFee;
    }

    public List<OrderListBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderListBean> orderList) {
        this.orderList = orderList;
    }

    public static class OrderListBean {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
