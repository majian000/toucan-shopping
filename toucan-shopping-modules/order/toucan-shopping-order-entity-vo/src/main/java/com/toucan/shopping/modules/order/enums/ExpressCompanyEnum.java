package com.toucan.shopping.modules.order.enums;

import java.util.List;

/**
 * 快递公司
 */
public enum ExpressCompanyEnum {
    SHENTONG("SHENTONG", "申通快递"),
    YUANTONG("YUANTONG", "圆通速递"),
    ZHONGTONG("ZHONGTONG", "中通快递"),
    BAISHI("BAISHI", "百世快递"),
    YUNDA("YUNDA", "韵达快递")
            ;
    private String code;
    private String name;
    ExpressCompanyEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ExpressCompanyEnum getKey(String code){
        for (ExpressCompanyEnum changeActivityEnumType : ExpressCompanyEnum.values()) {
            if (changeActivityEnumType.getCode().equals(code)){
                return  changeActivityEnumType;
            }
        }
        return  null;
    }



    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
