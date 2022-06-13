package com.toucan.shopping.modules.common.util;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AuthHeaderUtil {


    public static String getAdminId(String appCode,String authHeader) throws Exception {
        if(StringUtils.isEmpty(authHeader))
        {
            throw new IllegalArgumentException("请求头参数无效");

        }

        String adminId = StringUtils.substringAfter(authHeader,appCode+"_aid=");
        if(adminId.indexOf(";")!=-1)
        {
            adminId=adminId.substring(0,adminId.indexOf(";"));
        }
        return adminId;
    }


    /**
     * 拿到管理员ID
     * @param appCode
     * @param authHeader
     * @return 如果没有找到就返回null
     * @throws Exception
     */
    public static String getAdminIdDefaultNull(String appCode,String authHeader) throws Exception {
        if (StringUtils.isEmpty(authHeader)) {
            return null;
        }

        if(authHeader.indexOf(appCode + "_aid=")!=-1)
        {
            String adminId = StringUtils.substringAfter(authHeader, appCode + "_aid=");
            if (adminId.indexOf(";") != -1) {
                adminId = adminId.substring(0, adminId.indexOf(";"));
            }

            return adminId;
        }
        return null;
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
