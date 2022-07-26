package com.toucan.shopping.cloud.apps.seller.web.util;


import org.apache.commons.lang3.StringUtils;

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
