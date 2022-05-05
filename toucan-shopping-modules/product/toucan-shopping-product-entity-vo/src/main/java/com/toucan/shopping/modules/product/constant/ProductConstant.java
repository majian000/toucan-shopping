package com.toucan.shopping.modules.product.constant;

/**
 * 商品字典
 */
public class ProductConstant {

    public static final Integer PROCESSING=1; //审核通过
    public static final Integer PASS=2; //审核通过
    public static final Integer REJECT=3; //审核驳回

    public static final Integer SHELVES_UP = 1; //上架
    public static final Integer SHELVES_DOWN = 0; //下架


    public static final int MESSAGE_CONTENT_TYPE_1=1; //纯文本
    public static final String PRODUCT_APPROVE_MESSAGE_CODE="80010"; //商品审核编码

    private static String SELLER_APP_CODE="10001004"; //卖家端应用编码

    public static final int MAX_SKU_NAME_SIZE=400; //SKU名称最大长度

    public static final int MAX_SHOP_PRODUCT_NAME_SIZE=300; //商品名称最大长度




}
