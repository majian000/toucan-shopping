package com.toucan.shopping.modules.area.enums;

/**
 * 国家编码
 * @author majian
 * @date 2022-9-8 15:10:22
 */
public enum CountryCodeEnum {
    COUNTRY_CHN("CHN", "中国")
            ;
    private String code;
    private String name;

    CountryCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static  CountryCodeEnum getKey(String code){
        for (CountryCodeEnum changeActivityEnumType : CountryCodeEnum.values()) {
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
