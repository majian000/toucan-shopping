package com.toucan.shopping.cloud.apps.web.util;


import com.toucan.shopping.cloud.apps.web.redis.VerifyCodeRedisKey;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class VCodeUtil {


    public static String getClientVCodeId(String cookie) throws Exception {
        if(StringUtils.isEmpty(cookie))
        {
            throw new IllegalArgumentException("请求头参数无效");

        }

        String vcode = StringUtils.substringAfter(cookie,"clientVCodeId=");
        if(vcode.indexOf(";")!=-1)
        {
            vcode=vcode.substring(0,vcode.indexOf(";"));
        }
        return vcode;
    }

    public static String getVerifyCodeKey(String appCode,HttpServletRequest request) throws Exception
    {
        return VerifyCodeRedisKey.getVerifyCodeKey(appCode,getClientVCodeId(request.getHeader("Cookie")));
    }

}
