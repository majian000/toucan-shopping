package com.toucan.shopping.modules.order.mapper;

import com.toucan.shopping.modules.product.vo.ProductSkuStatisticVO;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;


/**
 * 订单统计
 * @author majian
 */
@Mapper
public interface OrderStatisticMapper {

    /**
     * 查询总金额
     * @return
     */
    BigDecimal queryTotalMoney();

}
