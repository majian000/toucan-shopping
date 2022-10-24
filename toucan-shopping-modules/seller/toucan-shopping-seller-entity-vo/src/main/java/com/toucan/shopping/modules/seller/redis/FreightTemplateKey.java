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



    /**
     * 修改运费模板锁
     * @param userMainId
     * @return
     */
    public static String getUpdateLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:SELLER:SHOP:FREIGHT:TEMPLATE:UPDATE:"+userMainId+"_LOCK";
    }





    /**
     * 删除运费模板锁
     * @param userMainId
     * @return
     */
    public static String getDeleteLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:SELLER:SHOP:FREIGHT:TEMPLATE:UPDATE:"+userMainId+"_LOCK";
    }
}
