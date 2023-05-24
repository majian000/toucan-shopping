package com.toucan.shopping.modules.seller.util;

import com.toucan.shopping.modules.common.util.MD5Util;

public class FreightTemplateUtils {

    private static final String salt="0.99";

    public static String getDeleteSignHeader(String userMainId) throws Exception
    {
        return MD5Util.md5(userMainId+salt);
    }


}
