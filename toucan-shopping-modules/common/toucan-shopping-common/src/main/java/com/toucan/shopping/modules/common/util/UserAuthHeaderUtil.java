package com.toucan.shopping.modules.common.util;


import org.apache.commons.lang3.StringUtils;

public class UserAuthHeaderUtil {


    public static String getUserMainId(String appCode,String authHeader) throws Exception {
        if(StringUtils.isEmpty(authHeader))
        {
            throw new IllegalArgumentException("请求头参数无效");

        }

        String adminId = StringUtils.substringAfter(authHeader,appCode+"_uid=");
        if(adminId.indexOf(";")!=-1)
        {
            adminId=adminId.substring(0,adminId.indexOf(";"));
        }
        return adminId;
    }



    public static String getToken(String appCode,String authHeader) throws Exception {
        if(StringUtils.isEmpty(authHeader))
        {
            throw new IllegalArgumentException("请求头参数无效");

        }

        String token = StringUtils.substringAfter(authHeader,appCode+"_lt=");
        if(token.indexOf(";")!=-1)
        {
            token=token.substring(0,token.indexOf(";"));
        }
        return token;
    }

}
