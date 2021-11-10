package com.toucan.shopping.cloud.apps.seller.web.service;

import com.toucan.shopping.modules.category.vo.CategoryVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 店铺页服务类
 */
public interface ShopPageService {


    String shopInfo(HttpServletRequest httpServletRequest);

}
