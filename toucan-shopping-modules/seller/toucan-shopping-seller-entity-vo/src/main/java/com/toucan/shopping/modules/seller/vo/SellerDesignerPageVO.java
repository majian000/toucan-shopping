package com.toucan.shopping.modules.seller.vo;

import com.toucan.shopping.modules.seller.entity.SellerDesignerPage;
import com.toucan.shopping.modules.seller.entity.ShopBanner;
import lombok.Data;

/**
 * 设计器页面
 * @author majian
 */
@Data
public class SellerDesignerPageVO extends SellerDesignerPage {

    private String createrName;  //创建人名称 店铺所属人ID或者管理员名称

    private String updaterName; //修改人名称 店铺所属人ID或者管理员名称

}
