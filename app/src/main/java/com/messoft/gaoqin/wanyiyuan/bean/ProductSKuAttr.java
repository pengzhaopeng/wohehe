package com.messoft.gaoqin.wanyiyuan.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductSKuAttr {

    /**
     * productAttrItemListMap : [{"key":"颜色","value":[{"attrValueId":11,"attrValueName":"紫色"},{"attrValueId":2,"attrValueName":"黄色"},{"attrValueId":13,"attrValueName":"黑色"},{"attrValueId":12,"attrValueName":"绿色"},{"attrValueId":1,"attrValueName":"红色"},{"attrValueId":14,"attrValueName":"白色"}]},{"key":"版本","value":[{"attrValueId":9,"attrValueName":"全网通128G"},{"attrValueId":10,"attrValueName":"全网通256G"},{"attrValueId":8,"attrValueName":"全网通64G"}]}]
     * productAttrSkuMap : {"42":"2,10","48":"14,10","47":"1,8","46":"1,9","49":"14,9","45":"1,10","44":"2,8","50":"14,8","43":"2,9","51":"13,10","41":"11,8","40":"11,9","52":"13,9","39":"11,10","38":"12,8","53":"13,8","37":"12,9","36":"12,10"}
     * productAttrItemMap : {"颜色":[{"attrValueId":11,"attrValueName":"紫色"},{"attrValueId":2,"attrValueName":"黄色"},{"attrValueId":13,"attrValueName":"黑色"},{"attrValueId":12,"attrValueName":"绿色"},{"attrValueId":1,"attrValueName":"红色"},{"attrValueId":14,"attrValueName":"白色"}],"版本":[{"attrValueId":9,"attrValueName":"全网通128G"},{"attrValueId":10,"attrValueName":"全网通256G"},{"attrValueId":8,"attrValueName":"全网通64G"}]}
     * productAttrSkuListMap : [{"key":"42","value":"2,10"},{"key":"48","value":"14,10"},{"key":"47","value":"1,8"},{"key":"46","value":"1,9"},{"key":"49","value":"14,9"},{"key":"45","value":"1,10"},{"key":"44","value":"2,8"},{"key":"50","value":"14,8"},{"key":"43","value":"2,9"},{"key":"51","value":"13,10"},{"key":"41","value":"11,8"},{"key":"40","value":"11,9"},{"key":"52","value":"13,9"},{"key":"39","value":"11,10"},{"key":"38","value":"12,8"},{"key":"53","value":"13,8"},{"key":"37","value":"12,9"},{"key":"36","value":"12,10"}]
     */

    private List<ProductAttrItemListMapBean> productAttrItemListMap;
    private List<ProductAttrSkuListMapBean> productAttrSkuListMap;


    public List<ProductAttrItemListMapBean> getProductAttrItemListMap() {
        return productAttrItemListMap;
    }

    public void setProductAttrItemListMap(List<ProductAttrItemListMapBean> productAttrItemListMap) {
        this.productAttrItemListMap = productAttrItemListMap;
    }

    public List<ProductAttrSkuListMapBean> getProductAttrSkuListMap() {
        return productAttrSkuListMap;
    }

    public void setProductAttrSkuListMap(List<ProductAttrSkuListMapBean> productAttrSkuListMap) {
        this.productAttrSkuListMap = productAttrSkuListMap;
    }


    public static class ProductAttrItemListMapBean {
        /**
         * key : 颜色
         * value : [{"attrValueId":11,"attrValueName":"紫色"},{"attrValueId":2,"attrValueName":"黄色"},{"attrValueId":13,"attrValueName":"黑色"},{"attrValueId":12,"attrValueName":"绿色"},{"attrValueId":1,"attrValueName":"红色"},{"attrValueId":14,"attrValueName":"白色"}]
         */

        private String key;
        private List<ValueBean> value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<ValueBean> getValue() {
            return value;
        }

        public void setValue(List<ValueBean> value) {
            this.value = value;
        }

        public static class ValueBean {
            /**
             * attrValueId : 11
             * attrValueName : 紫色
             */

            private int attrValueId;
            private String attrValueName;

            public int getAttrValueId() {
                return attrValueId;
            }

            public void setAttrValueId(int attrValueId) {
                this.attrValueId = attrValueId;
            }

            public String getAttrValueName() {
                return attrValueName;
            }

            public void setAttrValueName(String attrValueName) {
                this.attrValueName = attrValueName;
            }
        }
    }

    public static class ProductAttrSkuListMapBean {
        /**
         * key : 42
         * value : 2,10
         */

        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
