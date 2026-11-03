package com.toucan.shopping.modules.common.util;


import com.toucan.shopping.modules.exception.user.NotFoundUserTokenException;
import com.toucan.shopping.modules.exception.user.NotFountUserMainIdException;
import com.toucan.shopping.modules.exception.user.UserHeaderException;
import org.apache.commons.lang3.StringUtils;

public class UserAuthHeaderUtil {

    /**
     * 会话 cookie 名前缀（默认 tss_，与 PC/卖家/消息共用；H5 端在启动时置为 h5_ 实现独立会话）
     */
    public static String cookiePrefix = "tss_";


    public static String getUserMainId(String authHeader) throws Exception {
        if(StringUtils.isEmpty(authHeader))
        {
            throw new UserHeaderException("请求头参数无效");
        }

        String userId = StringUtils.substringAfter(authHeader, cookiePrefix + "uid=");
        if(userId.indexOf(";")!=-1)
        {
            userId=userId.substring(0,userId.indexOf(";"));
        }
        if(StringUtils.isEmpty(userId))
        {
            throw new NotFountUserMainIdException("从请求头中没有找到用户ID 请求头值为:"+authHeader);
        }
        return userId;
    }



    public static String getToken(String authHeader) throws Exception {
        if(StringUtils.isEmpty(authHeader))
        {
            throw new UserHeaderException("请求头参数无效");
        }

        String token = StringUtils.substringAfter(authHeader, cookiePrefix + "lt=");
        if(token.indexOf(";")!=-1)
        {
            token=token.substring(0,token.indexOf(";"));
        }
        if(StringUtils.isEmpty(token))
        {
            throw new NotFoundUserTokenException("从请求头中没有找到用户token 请求头值为:"+authHeader);
        }
        return token;
    }

}
