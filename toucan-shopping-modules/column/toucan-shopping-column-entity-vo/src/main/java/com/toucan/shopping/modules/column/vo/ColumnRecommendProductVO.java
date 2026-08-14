package com.toucan.shopping.modules.column.vo;

import com.toucan.shopping.modules.column.entity.ColumnRecommendProduct;
import lombok.Data;

/**
 * 栏目推荐商品
 */
@Data
public class ColumnRecommendProductVO extends ColumnRecommendProduct {

    private String shopProductName; //店铺商品名称


    private String httpImgPath; //外网访问地址

    /**
     * base64图片数据（仅前端传输用，不持久化）
     */
    private String imgBase64;

}
