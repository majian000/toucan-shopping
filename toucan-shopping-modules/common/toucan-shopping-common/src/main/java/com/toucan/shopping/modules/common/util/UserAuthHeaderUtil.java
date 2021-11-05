package com.toucan.shopping.modules.common.util;


import org.apache.commons.lang3.StringUtils;

public class UserAuthHeaderUtil {


    public static String getUserMainId(String authHeader) throws Exception {
        if(StringUtils.isEmpty(authHeader))
        {
            throw new IllegalArgumentException("请求头参数无效");
        }

        String userId = StringUtils.substringAfter(authHeader,"tss_uid=");
        if(userId.indexOf(";")!=-1)
        {
            userId=userId.substring(0,userId.indexOf(";"));
        }
        return userId;
    }



    public static String getToken(String authHeader) throws Exception {
        if(StringUtils.isEmpty(authHeader))
        {
            throw new IllegalArgumentException("请求头参数无效");

        }

        String token = StringUtils.substringAfter(authHeader,"tss_lt=");
        if(token.indexOf(";")!=-1)
        {
            token=token.substring(0,token.indexOf(";"));
        }
        return token;
    }

}
