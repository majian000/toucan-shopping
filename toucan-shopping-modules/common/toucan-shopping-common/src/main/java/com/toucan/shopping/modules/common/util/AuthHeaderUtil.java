package com.toucan.shopping.modules.common.util;


import com.toucan.shopping.modules.common.exception.InvalidAdminHeaderException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AuthHeaderUtil {

    private static final String adminPrefix="ADMIN_";

    public static String getAdminId(String appCode,String authHeader) throws Exception {
        if(StringUtils.isEmpty(authHeader))
        {
            throw new InvalidAdminHeaderException("请求头参数无效");

        }

        String adminId = StringUtils.substringAfter(authHeader,appCode+"_aid=");
        if(adminId.indexOf(";")!=-1)
        {
            adminId=adminId.substring(0,adminId.indexOf(";"));
        }
        return adminId;
    }


    public static String getAdminPrefix(){
        return adminPrefix;
    }

    public static String getAdminIdAndPrefix(String appCode,String authHeader) throws Exception {
        if(StringUtils.isEmpty(authHeader))
        {
            throw new InvalidAdminHeaderException("请求头参数无效");

        }

        String adminId = StringUtils.substringAfter(authHeader,appCode+"_aid=");
        if(adminId.indexOf(";")!=-1)
        {
            adminId=adminId.substring(0,adminId.indexOf(";"));
        }
        return adminPrefix+adminId;
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
            throw new InvalidAdminHeaderException("请求头参数无效");

        }

        String token = StringUtils.substringAfter(authHeader,appCode+"_lt=");
        if(token.indexOf(";")!=-1)
        {
            token=token.substring(0,token.indexOf(";"));
        }
        return token;
    }

}
