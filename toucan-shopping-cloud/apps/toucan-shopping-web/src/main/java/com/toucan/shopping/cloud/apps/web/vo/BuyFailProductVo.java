package com.toucan.shopping.cloud.apps.web.vo;

import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购买失败项对象
 * @author majian
 */
@Data
public class BuyFailProductVo {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ProductSkuVO productSkuVO; //无法购买的商品

    private String failMsg; //失败原因

    private int failCode; //失败状态码

}
