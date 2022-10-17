package com.toucan.shopping.modules.seller.redis;

/**
 * 商户店铺运费模板
 */
public class FreightTemplateKey {



    /**
     * 保存运费模板锁
     * @param userMainId
     * @return
     */
    public static String getSaveLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:SELLER:SHOP:FREIGHT:TEMPLATE:SAVE:"+userMainId+"_LOCK";
    }


}
