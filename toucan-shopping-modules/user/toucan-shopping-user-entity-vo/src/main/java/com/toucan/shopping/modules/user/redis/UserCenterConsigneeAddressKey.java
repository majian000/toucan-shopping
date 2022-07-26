package com.toucan.shopping.modules.user.redis;

/**
 * 收货地址
 */
public class UserCenterConsigneeAddressKey {


    /**
     * 保存收货人信息
     * @param userMainId
     * @return
     */
    public static String getSaveLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:INDEX:CONSIGNEE_ADDRESS:SAVE:"+userMainId+"_LOCK";
    }



    /**
     * 修改收货人信息
     * @param userMainId
     * @return
     */
    public static String getUpdateLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:INDEX:CONSIGNEE_ADDRESS:UPDATE:"+userMainId+"_LOCK";
    }

}
