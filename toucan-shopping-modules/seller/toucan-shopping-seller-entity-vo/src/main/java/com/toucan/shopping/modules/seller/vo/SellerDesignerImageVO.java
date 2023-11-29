package com.toucan.shopping.modules.seller.vo;

import com.toucan.shopping.modules.seller.entity.SellerDesignerImage;
import lombok.Data;

/**
 * 店铺图片
 *
 * @author majian
 */
@Data
public class SellerDesignerImageVO extends SellerDesignerImage {

    private String createrName;  //创建人名称 店铺所属人ID或者管理员名称

    private String updaterName; //修改人名称 店铺所属人ID或者管理员名称

}
