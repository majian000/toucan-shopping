package com.toucan.shopping.modules.area.enums;

/**
 * 大区编码
 * @author majian
 * @date 2022-9-8 15:10:22
 */
public enum BigAreaCodeEnum {
    BIG_AREA_CODE_1("CHN","1", "华东"),
    BIG_AREA_CODE_2("CHN","2", "华北"),
    BIG_AREA_CODE_3("CHN","3", "华中"),
    BIG_AREA_CODE_4("CHN","4", "华南"),
    BIG_AREA_CODE_5("CHN","5", "东北"),
    BIG_AREA_CODE_6("CHN","6", "西北"),
    BIG_AREA_CODE_7("CHN","7", "西南"),
    BIG_AREA_CODE_8("CHN","8", "港澳台")
            ;

    private String countryCode;
    private String code;
    private String name;

    BigAreaCodeEnum(String countryCode, String code, String name) {
        this.countryCode = countryCode;
        this.code = code;
        this.name = name;
    }

    public static BigAreaCodeEnum getKey(String countryCode,String code){
        for (BigAreaCodeEnum changeActivityEnumType : BigAreaCodeEnum.values()) {
            if (changeActivityEnumType.getCountryCode().equals(countryCode)
                &&changeActivityEnumType.getCode().equals(code)){
                return  changeActivityEnumType;
            }
        }
        return  null;
    }

    public String getCountryCode() {
        return countryCode;
    }


    public String getCode() {
        return code;
    }


    public String getName() {
        return name;
    }


}
