package com.toucan.shopping.modules.seller.util;

import com.toucan.shopping.modules.common.util.MD5Util;


public class ShopUtils {

    public static final String magic="CN1949"; //魔法值



    /**
     * 加密店铺ID
     * @param shopId
     * @return
     */
    public static String encShopId(String shopId) throws Exception
    {
        try {
            return MD5Util.md5(shopId + "|" + magic);
        }catch(Exception e)
        {
            throw e;
        }
    }

}
