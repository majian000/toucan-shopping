package com.toucan.shopping.modules.designer.seller.constant;

public class SellerDesignerRedisKey {

    /**
     * 返回PC首页预览key
     * @return
     */
    public static String getPcIndexPreviewKey(String shopId)
    {
        return "TOUCAN_SHOPPING:SELLER:WEB:DESIGNER:PC:INDEX:PREVIEW:"+shopId;
    }
}
