package com.toucan.shopping.modules.seller.redis;

/**
 * 卖家设计器页面
 */
public class SellerDesignerPageKey {



    /**
     * 保存修改锁
     * @param userMainId
     * @return
     */
    public static String getSaveUpdateLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:SELLER:DESIGNER:PAGE:SAVEUPDATE:"+userMainId+"_LOCK";
    }

    /**
     * 删除锁
     * @param id
     * @return
     */
    public static String getDeleteLockKey(String id)
    {
        return "TOUCAN_SHOPPING_WEB:SELLER:DESIGNER:PAGE:DELETE:"+id+"_LOCK";
    }
}
