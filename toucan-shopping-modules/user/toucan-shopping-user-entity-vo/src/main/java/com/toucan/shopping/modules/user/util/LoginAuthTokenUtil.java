package com.toucan.shopping.modules.user.util;

import com.toucan.shopping.modules.common.util.MD5Util;


public class LoginAuthTokenUtil {

    public static String salt="0.9474";  //这个必须要修改成你自己的,要不然会有很严重的安全性问题


    /**
     * 生成权限token(用户忽略权限校验,优化流量开销)
     * @param token
     * @return
     */
    public static String generatorAuthToken(String token) throws Exception
    {
        try {
            return MD5Util.md5(token + "|" + salt);
        }catch(Exception e)
        {
            throw e;
        }
    }


}