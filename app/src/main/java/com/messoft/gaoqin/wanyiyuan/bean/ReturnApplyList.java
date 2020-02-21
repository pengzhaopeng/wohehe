package com.messoft.gaoqin.wanyiyuan.bean;

import java.io.Serializable;
import java.util.List;

public class ReturnApplyList implements Serializable{

    /**
     * createTime : 2019-11-14 10:38:06
     * phone :
     * accountId : 4833
     * sort : 0
     * orderItemId : 480
     * orderCode : 1573698671212
     * reason : 币
     * auditName : 系统管理员
     * returnQty : 1
     * type : 0
     * createBy : -100
     * id : 11
     * amount : 500
     * applyImgs : [{"url":"http://mstest-www.mesandbox.com/upload/headline/20191114/20191114103806_345.png","dbFileName":"headline/20191114/20191114103806_345.png","fileName":"20191114103806_345.png"}]
     * isDel : 0
     * skuPrice : 500
     * logisticsId :
     * auditTime :
     * description : 是
     * skuId : 152
     * logisticsName :
     * productTime : 2019-11-14 09:09:05
     * orderId : 481
     * updateBy : -100
     * auditRemarks :
     * auditState : 1
     * waybill :
     * standard : 100
     * productCode : 1573693745043
     * status : 1
     * updateTime : 2019-11-14 10:38:29
     * auditId : 999999
     * code : 1573699086533
     * serviceType : 0
     * createName :
     * productId : 51
     * logisticsImgs :
     * updateName :
     * account : 18800000009
     * memberId : 76
     * receiptStatus : 1
     * companyId : 74
     * productName : 测试
     */

    private String createTime;
    private String phone;
    private String accountId;
    private String sort;
    private String orderItemId;
    private String orderCode;
    private String reason;
    private String auditName;
    private String returnQty;
    private String type;
    private String createBy;
    private String id;
    private String amount;
    private String isDel;
    private String subStatus;
    private String skuPrice;
    private String logisticsId;
    private String auditTime;
    private String description;
    private String skuId;
    private String logisticsName;
    private String productTime;
    private String orderId;
    private String updateBy;
    private String auditRemarks;
    private String auditState;
    private String waybill;
    private String standard;
    private String productCode;
    private String status;
    private String updateTime;
    private String auditId;
    private String code;
    private String serviceType;
    private String createName;
    private String productId;
    private String logisticsImgs;
    private String updateName;
    private String account;
    private String memberId;
    private String receiptStatus;
    private String companyId;
    private String productName;
    private List<ApplyImgsBean> applyImgs;

    public String getSubStatus() {
        return subStatus == null ? "" : subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus == null ? "" : subStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public String getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(String returnQty) {
        this.returnQty = returnQty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getSkuPrice() {
        return skuPrice;
    }

    public void setSkuPrice(String skuPrice) {
        this.skuPrice = skuPrice;
    }

    public String getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(String logisticsId) {
        this.logisticsId = logisticsId;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    public String getProductTime() {
        return productTime;
    }

    public void setProductTime(String productTime) {
        this.productTime = productTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getAuditRemarks() {
        return auditRemarks;
    }

    public void setAuditRemarks(String auditRemarks) {
        this.auditRemarks = auditRemarks;
    }

    public String getAuditState() {
        return auditState;
    }

    public void setAuditState(String auditState) {
        this.auditState = auditState;
    }

    public String getWaybill() {
        return waybill;
    }

    public void setWaybill(String waybill) {
        this.waybill = waybill;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLogisticsImgs() {
        return logisticsImgs;
    }

    public void setLogisticsImgs(String logisticsImgs) {
        this.logisticsImgs = logisticsImgs;
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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(String receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<ApplyImgsBean> getApplyImgs() {
        return applyImgs;
    }

    public void setApplyImgs(List<ApplyImgsBean> applyImgs) {
        this.applyImgs = applyImgs;
    }

    public static class ApplyImgsBean implements Serializable{
        /**
         * url : http://mstest-www.mesandbox.com/upload/headline/20191114/20191114103806_345.png
         * dbFileName : headline/20191114/20191114103806_345.png
         * fileName : 20191114103806_345.png
         */

        private String url;
        private String dbFileName;
        private String fileName;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDbFileName() {
            return dbFileName;
        }

        public void setDbFileName(String dbFileName) {
            this.dbFileName = dbFileName;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}
