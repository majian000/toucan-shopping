package com.toucan.shopping.modules.column.vo;

import com.toucan.shopping.modules.column.entity.ColumnRecommendProduct;
import com.toucan.shopping.modules.column.entity.HotProduct;
import lombok.Data;

/**
 * 热门商品
 * @author majian
 */
@Data
public class HotProductVO extends HotProduct {


    private String httpImgPath; //外网访问地址


    private String createAdminName; //创建人姓名
    private String updateAdminName; //修改人姓名

}
