package com.toucan.shopping.modules.seller.vo;

import com.toucan.shopping.modules.seller.entity.SellerShop;
import lombok.Data;

import java.util.List;

/**
 * 商户店铺信息
 */
@Data
public class SellerShopVO extends SellerShop {

    /**
     * 验证码
     */
    private String vcode;

    /**
     * 外网访问的logo地址
     */
    private String httpLogo;

    /**
     * 剩余修改名称次数
     */
    private Integer surplusChangeNameCount;


    /**
     * 用户登录历史
     */
    private List<SellerLoginHistoryVO> loginHistoryList;

    /**
     * 上一次登录时间
     */
    private String lastLoginTime;

    /**
     * 默认图标路径
     */
    private String defaultLogo;


    /**
     * 默认图标路径
     */
    private String httpDefaultLogo;


    private String createAdminName; //创建人姓名
    private String updateAdminName; //修改人姓名

}
