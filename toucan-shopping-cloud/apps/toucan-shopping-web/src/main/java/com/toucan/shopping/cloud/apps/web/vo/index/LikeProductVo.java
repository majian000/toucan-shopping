package com.toucan.shopping.cloud.apps.web.vo.index;

import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import lombok.Data;

/**
 * 猜你喜欢
 *
 * @author majian
 * @date 2021-8-3 09:33:19
 */
@Data
public class LikeProductVo extends ProductSkuVO {


    /**
     * 介绍
     */
    private String desc;

}
