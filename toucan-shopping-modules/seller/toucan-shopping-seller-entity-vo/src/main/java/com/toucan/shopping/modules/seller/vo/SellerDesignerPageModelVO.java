package com.toucan.shopping.modules.seller.vo;

import com.toucan.shopping.modules.seller.entity.SellerDesignerPageModel;
import lombok.Data;

/**
 * 设计器页面
 * @author majian
 */
@Data
public class SellerDesignerPageModelVO extends SellerDesignerPageModel {

    private String createrName;  //创建人名称 店铺所属人ID或者管理员名称

    private String updaterName; //修改人名称 店铺所属人ID或者管理员名称

    private String shopName; //店铺名称

    private String pcIndexPage; //PC首页正式页

}
