package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.mapper.ProductSkuStatisticMapper;
import com.toucan.shopping.modules.product.service.ProductSkuStatisticService;
import com.toucan.shopping.modules.product.vo.ProductSkuStatisticVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductSkuStatisticServiceImpl implements ProductSkuStatisticService {


    @Autowired
    private ProductSkuStatisticMapper productSkuStatisticMapper;


    @Override
    public ProductSkuStatisticVO queryTotalAndTodayAndCurrentMonthAndCurrentYear() {
        ProductSkuStatisticVO productSkuStatisticVO=new ProductSkuStatisticVO();
        productSkuStatisticVO.setTotal(productSkuStatisticMapper.queryTotal()); //总数
        productSkuStatisticVO.setTodayCount(productSkuStatisticMapper.queryTodayTotal()); //今日新增
        productSkuStatisticVO.setCurMonthCount(productSkuStatisticMapper.queryCurMonthTotal()); //本月新增
        productSkuStatisticVO.setCurYearCount(productSkuStatisticMapper.queryCurYearTotal()); //本年新增
        return productSkuStatisticVO;
    }

    @Override
    public List<ProductSkuStatisticVO> queryCategoryStatistic(ProductSkuStatisticVO query) {
        return productSkuStatisticMapper.queryCategoryStatistic(query);
    }

}
