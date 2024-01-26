package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.page.ProductSkuPageInfo;
import com.toucan.shopping.modules.product.vo.ProductSkuStatisticVO;
import com.toucan.shopping.modules.product.vo.ProductSkuStatusVO;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;


/**
 * 商品SKU统计
 * @author majian
 */
@Mapper
public interface ProductSkuStatisticMapper {


    /**
     * 查询总数量
     * @return
     */
    Long queryTotal();

    /**
     * 查询今日数量
     * @return
     */
    Long queryTodayTotal();


    /**
     * 查询本月数量
     * @return
     */
    Long queryCurMonthTotal();

    /**
     * 查询本年数量
     * @return
     */
    Long queryCurYearTotal();

    /**
     * 查询分类统计
     * @param query
     * @return
     */
    List<ProductSkuStatisticVO> queryCategoryStatistic(ProductSkuStatisticVO query);

}
