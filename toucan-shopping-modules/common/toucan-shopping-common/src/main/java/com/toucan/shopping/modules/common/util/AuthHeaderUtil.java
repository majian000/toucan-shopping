package com.toucan.shopping.modules.common.util;


import org.apache.commons.lang3.StringUtils;

public class AuthHeaderUtil {


    public static String getAdminId(String authHeader) throws Exception {
        if(StringUtils.isEmpty(authHeader))
        {
            throw new IllegalArgumentException("请求头参数无效");

        }

        String adminId = StringUtils.substringAfter(authHeader,"aid=");
        if(adminId.indexOf(";")!=-1)
        {
            adminId=adminId.substring(0,adminId.indexOf(";"));
        }
        return adminId;
    }

}
