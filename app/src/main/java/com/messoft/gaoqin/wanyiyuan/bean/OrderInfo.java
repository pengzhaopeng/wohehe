package com.messoft.gaoqin.wanyiyuan.bean;

import java.io.Serializable;
import java.util.List;

public class OrderInfo implements Serializable{

    private String remark;
    private String orderCode;
    private String endTime;
    private String payTime;
    private String paymentTypeName;
    private String receiveDistrictText;
    private String receiveId;
    private double orderAmount;
    private String paymentMethodId;
    private String receiveCityText;
    private String orderType;
    private String updateTime;
    private String sellerPhone;
    private String sellerName;
    private String orderPayment;
    private String deliveryTime;
    private String receiveTime;
    private String onlinePaymentAmount;
    private String receiveAddress;
    private String memberId;
    private String receiveStreetId;
    private String receiveCityId;
    private String createTime;
    private String accountId;
    private String paymentTypeId;
    private String receiveMobile;
    private String receiveProvinceId;
    private String paymentMethodName;
    private String createBy;
    private String discountAmount;
    private String usePoString;
    private String handleStatus;
    private String id;
    private String postIp;
    private String isDel;
    private String receivePostcode;
    private String actualPaymentAmount;
    private String receiveName;
    private String receiveNote;
    private String receiveProvinceText;
    private String postTime;
    private String receiveStreetText;
    private String postFee;
    private String waybill;
    private String invoiceName;
    private String standard;
    private String orderSource;
    private String fundsPaymentAmount;
    private String deliveryType;
    private String receivePhone;
    private String receiveDistrictId;
    private String updateName;
    private String account;
    private String companyId;
    private double productAmount;
    private String sellerMobile;
    private String isComment;
    private String useVoucher;
    private String logisticsId;
    private String logisticsName;
    private long usePoint;
    private String isPickUp; //是否提货 0.不提货 1.提货
    private List<OrderItemDataBean> orderItemData;

    public long getUsePoint() {
        return usePoint;
    }

    public void setUsePoint(long usePoint) {
        this.usePoint = usePoint;
    }

    public String getUseVoucher() {
        return useVoucher == null ? "" : useVoucher;
    }

    public void setUseVoucher(String useVoucher) {
        this.useVoucher = useVoucher == null ? "" : useVoucher;
    }

    public String getIsPickUp() {
        return isPickUp == null ? "" : isPickUp;
    }

    public void setIsPickUp(String isPickUp) {
        this.isPickUp = isPickUp == null ? "" : isPickUp;
    }

    public String getLogisticsId() {
        return logisticsId == null ? "" : logisticsId;
    }

    public void setLogisticsId(String logisticsId) {
        this.logisticsId = logisticsId == null ? "" : logisticsId;
    }

    public String getLogisticsName() {
        return logisticsName == null ? "" : logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName == null ? "" : logisticsName;
    }

    public String getIsComment() {
        return isComment == null ? "" : isComment;
    }

    public void setIsComment(String isComment) {
        this.isComment = isComment == null ? "" : isComment;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }

    public String getReceiveDistrictText() {
        return receiveDistrictText;
    }

    public void setReceiveDistrictText(String receiveDistrictText) {
        this.receiveDistrictText = receiveDistrictText;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getReceiveCityText() {
        return receiveCityText;
    }

    public void setReceiveCityText(String receiveCityText) {
        this.receiveCityText = receiveCityText;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(String orderPayment) {
        this.orderPayment = orderPayment;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getOnlinePaymentAmount() {
        return onlinePaymentAmount;
    }

    public void setOnlinePaymentAmount(String onlinePaymentAmount) {
        this.onlinePaymentAmount = onlinePaymentAmount;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getReceiveStreetId() {
        return receiveStreetId;
    }

    public void setReceiveStreetId(String receiveStreetId) {
        this.receiveStreetId = receiveStreetId;
    }

    public String getReceiveCityId() {
        return receiveCityId;
    }

    public void setReceiveCityId(String receiveCityId) {
        this.receiveCityId = receiveCityId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(String paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getReceiveMobile() {
        return receiveMobile;
    }

    public void setReceiveMobile(String receiveMobile) {
        this.receiveMobile = receiveMobile;
    }

    public String getReceiveProvinceId() {
        return receiveProvinceId;
    }

    public void setReceiveProvinceId(String receiveProvinceId) {
        this.receiveProvinceId = receiveProvinceId;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getUsePoString() {
        return usePoString;
    }

    public void setUsePoString(String usePoString) {
        this.usePoString = usePoString;
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostIp() {
        return postIp;
    }

    public void setPostIp(String postIp) {
        this.postIp = postIp;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getReceivePostcode() {
        return receivePostcode;
    }

    public void setReceivePostcode(String receivePostcode) {
        this.receivePostcode = receivePostcode;
    }

    public String getActualPaymentAmount() {
        return actualPaymentAmount;
    }

    public void setActualPaymentAmount(String actualPaymentAmount) {
        this.actualPaymentAmount = actualPaymentAmount;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceiveNote() {
        return receiveNote;
    }

    public void setReceiveNote(String receiveNote) {
        this.receiveNote = receiveNote;
    }

    public String getReceiveProvinceText() {
        return receiveProvinceText;
    }

    public void setReceiveProvinceText(String receiveProvinceText) {
        this.receiveProvinceText = receiveProvinceText;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getReceiveStreetText() {
        return receiveStreetText;
    }

    public void setReceiveStreetText(String receiveStreetText) {
        this.receiveStreetText = receiveStreetText;
    }

    public String getPostFee() {
        return postFee;
    }

    public void setPostFee(String postFee) {
        this.postFee = postFee;
    }

    public String getWaybill() {
        return waybill;
    }

    public void setWaybill(String waybill) {
        this.waybill = waybill;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getFundsPaymentAmount() {
        return fundsPaymentAmount;
    }

    public void setFundsPaymentAmount(String fundsPaymentAmount) {
        this.fundsPaymentAmount = fundsPaymentAmount;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getReceiveDistrictId() {
        return receiveDistrictId;
    }

    public void setReceiveDistrictId(String receiveDistrictId) {
        this.receiveDistrictId = receiveDistrictId;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public double getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(double productAmount) {
        this.productAmount = productAmount;
    }

    public String getSellerMobile() {
        return sellerMobile;
    }

    public void setSellerMobile(String sellerMobile) {
        this.sellerMobile = sellerMobile;
    }

    public List<OrderItemDataBean> getOrderItemData() {
        return orderItemData;
    }

    public void setOrderItemData(List<OrderItemDataBean> orderItemData) {
        this.orderItemData = orderItemData;
    }

    public static class OrderItemDataBean implements Serializable{
        private String createTime;
        private double skuAmount;
        private String sort;
        private String orderCode;
        private String skuSettlementAmount;
        private String brandId;
        private String createBy;
        private String id;
        private String isDel;
        private double skuPrice;
        private String skuSettlementPrice;
        private String skuId;
        private String orderId;
        private String postTime;
        private String updateBy;
        private String standard;
        private String updateTime;
        private String productCode;
        private String classId;
        private String classCode;
        private String className;
        private String property;
        private String createName;
        private String skuQty;
        private String productId;
        private String updateName;
        private String companyId;
        private String transferState;
        private String productName;
        private String subStatus;
        private long skuPoints;

        public long getSkuPoints() {
            return skuPoints;
        }

        public void setSkuPoints(long skuPoints) {
            this.skuPoints = skuPoints;
        }

        public String getClassName() {
            return className == null ? "" : className;
        }

        public void setClassName(String className) {
            this.className = className == null ? "" : className;
        }

        public String getSubStatus() {
            return subStatus == null ? "" : subStatus;
        }

        public void setSubStatus(String subStatus) {
            this.subStatus = subStatus == null ? "" : subStatus;
        }

        public String getClassCode() {
            return classCode == null ? "" : classCode;
        }

        public void setClassCode(String classCode) {
            this.classCode = classCode == null ? "" : classCode;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public double getSkuAmount() {
            return skuAmount;
        }

        public void setSkuAmount(double skuAmount) {
            this.skuAmount = skuAmount;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getSkuSettlementAmount() {
            return skuSettlementAmount;
        }

        public void setSkuSettlementAmount(String skuSettlementAmount) {
            this.skuSettlementAmount = skuSettlementAmount;
        }

        public String getBrandId() {
            return brandId;
        }

        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsDel() {
            return isDel;
        }

        public void setIsDel(String isDel) {
            this.isDel = isDel;
        }

        public double getSkuPrice() {
            return skuPrice;
        }

        public void setSkuPrice(double skuPrice) {
            this.skuPrice = skuPrice;
        }

        public String getSkuSettlementPrice() {
            return skuSettlementPrice;
        }

        public void setSkuSettlementPrice(String skuSettlementPrice) {
            this.skuSettlementPrice = skuSettlementPrice;
        }

        public String getSkuId() {
            return skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getPostTime() {
            return postTime;
        }

        public void setPostTime(String postTime) {
            this.postTime = postTime;
        }

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }

        public String getStandard() {
            return standard;
        }

        public void setStandard(String standard) {
            this.standard = standard;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getCreateName() {
            return createName;
        }

        public void setCreateName(String createName) {
            this.createName = createName;
        }

        public String getSkuQty() {
            return skuQty;
        }

        public void setSkuQty(String skuQty) {
            this.skuQty = skuQty;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getUpdateName() {
            return updateName;
        }

        public void setUpdateName(String updateName) {
            this.updateName = updateName;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getTransferState() {
            return transferState;
        }

        public void setTransferState(String transferState) {
            this.transferState = transferState;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }
    }
}
