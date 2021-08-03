package com.toucan.shopping.cloud.apps.web.vo.index;

import com.toucan.shopping.modules.product.entity.ProductBuy;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 热门商品
 *
 * @author majian
 */
@Data
public class HotProductVo extends ProductSkuVO {

    /**
     * 介绍
     */
    private String desc;

}
