package com.toucan.shopping.modules.seller.vo;

import com.toucan.shopping.modules.seller.entity.ShopBanner;
import lombok.Data;

import java.util.List;

/**
 * 店铺轮播图
 *
 * @author majian
 */
@Data
public class ShopBannerVO extends ShopBanner {

    private String createrName;  //创建人名称 店铺所属人ID或者管理员名称

    private String updaterName; //修改人名称 店铺所属人ID或者管理员名称

}
