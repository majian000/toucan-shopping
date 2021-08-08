package com.toucan.shopping.modules.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * HTTP COOKIE工具类
 * @author majian
 * @date 2021-8-8 10:56:30
 */
public class HttpCookieUtil {


    /**
     * 是否支持SimeSite
     * @return true支持 false不支持
     */
    public static boolean disallowsSameSiteNone(String userAgent)
    {
        if(StringUtils.isEmpty(userAgent))
        {
            return false;
        }

        //这些型号的不支持
        if (userAgent.indexOf("Chrome/1")!=-1 ||
                userAgent.indexOf("Chrome/2")!=-1||
                userAgent.indexOf("Chrome/3")!=-1||
                userAgent.indexOf("Chrome/4")!=-1||
                userAgent.indexOf("Chrome/5")!=-1)
        {
            return false;
        }

        return true;
    }
}
