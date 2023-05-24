package com.toucan.shopping.modules.product.constant;

/**
 * 商品字典
 */
public class ProductConstant {

    public static final Integer PROCESSING=1; //审核中
    public static final Integer PASS=2; //审核通过
    public static final Integer REJECT=3; //审核驳回

    public static final Integer SHELVES_UP = 1; //上架
    public static final Integer SHELVES_DOWN = 0; //下架


    public static final int MESSAGE_CONTENT_TYPE_1=1; //纯文本
    public static final String PRODUCT_APPROVE_MESSAGE_CODE="80010"; //商品审核编码

    private static String SELLER_APP_CODE="10001004"; //卖家端应用编码

    public static final int MAX_SKU_NAME_SIZE=400; //SKU名称最大长度

    public static final int MAX_SHOP_PRODUCT_NAME_SIZE=300; //商品名称最大长度

    public static final int PRODUCT_APPROVE_SKU_REDIS_MAX_AGE=1800; //商品审核预览缓存 半小时过期


    public static final int NO_STOCK = 30001; //没有库存
    public static final int SOLD_OUT = 30002; //下架了

    public static final int DELETE_REDIS_SLEEP = 1000; //延迟双删,线程休眠时间


}
