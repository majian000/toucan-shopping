package com.toucan.shopping.modules.product.enums;

/**
 * 品牌商标注册地区
 */
public enum BrandTrademarkAreaTypeEnum {
    TRADEMARK_AREA_TYPE_1("1", "中国大陆地区"),
    TRADEMARK_AREA_TYPE_2("2", "香港、澳门特别行政区，台湾省和境外国家")
            ;
    private String code;
    private String msg;
    BrandTrademarkAreaTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public static   BrandTrademarkAreaTypeEnum getKey(String code){
        for (BrandTrademarkAreaTypeEnum changeActivityEnumType : BrandTrademarkAreaTypeEnum.values()) {
            if (changeActivityEnumType.getCode().equals(code)){
                return  changeActivityEnumType;
            }
        }
        return  null;
    }
    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
