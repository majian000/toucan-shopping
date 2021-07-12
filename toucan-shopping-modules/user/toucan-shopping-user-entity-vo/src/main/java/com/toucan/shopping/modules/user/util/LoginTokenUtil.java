package com.toucan.shopping.modules.user.util;

import com.toucan.shopping.modules.common.util.MD5Util;


public class LoginTokenUtil {

    public static String salt="0.4947";


    /**
     * 生成登录token
     * @param userMainId
     * @return
     */
    public static String generatorToken(Long userMainId) throws Exception
    {
        try {
            return MD5Util.md5(String.valueOf(userMainId) + "|" + salt);
        }catch(Exception e)
        {
            throw e;
        }
    }


}
