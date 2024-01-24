package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.mapper.ProductSkuMapper;
import com.toucan.shopping.modules.product.mapper.ProductSkuStatisticMapper;
import com.toucan.shopping.modules.product.page.ProductSkuPageInfo;
import com.toucan.shopping.modules.product.service.ProductSkuService;
import com.toucan.shopping.modules.product.service.ProductSkuStatisticService;
import com.toucan.shopping.modules.product.vo.ProductSkuStatisticVO;
import com.toucan.shopping.modules.product.vo.ProductSkuStatusVO;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
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
}
