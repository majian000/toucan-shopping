package com.toucan.shopping.cloud.user.web.util;

import org.apache.commons.lang.StringUtils;

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

}
