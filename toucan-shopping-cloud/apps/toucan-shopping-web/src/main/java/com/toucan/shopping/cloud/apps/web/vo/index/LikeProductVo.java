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
     * 单位
     */
    private String unit;

    /**
     *  数量
     */
    private Integer num;

}
