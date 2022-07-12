package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.mapper.ProductSkuMapper;
import com.toucan.shopping.modules.product.page.ProductSkuPageInfo;
import com.toucan.shopping.modules.product.service.ProductSkuService;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
public class ProductSkuServiceImpl implements ProductSkuService {

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Override
    public List<ProductSkuVO> queryList(ProductSkuVO productSkuVO) {
        return productSkuMapper.queryList(productSkuVO);
    }


    @Override
    public ProductSkuVO queryVOByIdAndShelves(Long id) {
        return productSkuMapper.queryVOByIdAndShelves(id);
    }

    @Override
    public PageInfo<ProductSkuVO> queryListPage(ProductSkuPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<ProductSkuVO> pageInfo = new PageInfo();
        pageInfo.setList(productSkuMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(productSkuMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public List<ProductSkuVO> queryShelvesVOListByShopProductId(Long shopProductId) {
        return productSkuMapper.queryShelvesVOListByShopProductId(shopProductId);
    }

    @Override
    public ProductSkuVO queryFirstOneByShopProductId(Long shopProductId) {
        return productSkuMapper.queryFirstOneByShopProductId(shopProductId);
    }

    @Transactional
    @Override
    public int save(ProductSku productSku) {
        return productSkuMapper.insert(productSku);
    }

    @Override
    public int saves(List<ProductSku> productSkus) {
        if(CollectionUtils.isEmpty(productSkus))
            throw new IllegalArgumentException("实体列表参数不能为空");
        return productSkuMapper.inserts(productSkus);
    }

    @Override
    public ProductSku queryById(Long id) {
        return productSkuMapper.queryById(id);
    }

    @Override
    public ProductSku queryByUuid(String uuid) {
        return productSkuMapper.queryByUuid(uuid);
    }

    @Override
    public int deleteByShopProductId(Long shopProductId) {
        return productSkuMapper.deleteByShopProductId(shopProductId);
    }
}
