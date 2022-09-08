package com.toucan.shopping.modules.area.enums;

/**
 * 大区编码
 * @author majian
 * @date 2022-9-8 15:10:22
 */
public enum BigAreaCodeEnum {
    BIG_AREA_CODE_1("CHN","HUADONG", "华东"),
    BIG_AREA_CODE_2("CHN","HUABEI", "华北"),
    BIG_AREA_CODE_3("CHN","HUAZHONG", "华中"),
    BIG_AREA_CODE_4("CHN","HUANAN", "华南"),
    BIG_AREA_CODE_5("CHN","DONGBEI", "东北"),
    BIG_AREA_CODE_6("CHN","XIBEI", "西北"),
    BIG_AREA_CODE_7("CHN","XINAN", "西南"),
    BIG_AREA_CODE_8("CHN","GANGAOTAI", "港澳台")
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
