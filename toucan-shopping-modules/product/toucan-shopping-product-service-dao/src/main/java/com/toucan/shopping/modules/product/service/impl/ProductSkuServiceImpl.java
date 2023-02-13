package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.mapper.ProductSkuMapper;
import com.toucan.shopping.modules.product.page.ProductSkuPageInfo;
import com.toucan.shopping.modules.product.service.ProductSkuService;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
    public ProductSkuVO queryVOById(Long id) {
        return productSkuMapper.queryVOById(id);
    }
    @Override
    public ProductSkuVO queryVOByIdAndStatus(Long id,int status) {
        return productSkuMapper.queryVOByIdAndStatus(id,status);
    }
    @Override
    public int updateStatusByShopProductId(Long shopProductId, Long shopId, Integer status) {
        return productSkuMapper.updateStatusByShopProductId(shopProductId,shopId,status);
    }


    @Override
    public int updateStatusById(Long id,Long shopId,Integer status) {
        return productSkuMapper.updateStatusById(id,shopId,status);
    }

    @Override
    public int updateStock(Long id, Integer stockNum) {
        return productSkuMapper.updateStock(id,stockNum);
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
    public List<ProductSkuVO> queryVOListByShopProductIdAndStatus(Long shopProductId,int status) {
        return productSkuMapper.queryVOListByShopProductIdAndStatus(shopProductId,status);
    }

    @Override
    public List<ProductSkuVO> queryProductSkuListByShopProductUuid(String shopProductUuid) {
        return productSkuMapper.queryProductSkuListByShopProductUuid(shopProductUuid);
    }

    @Override
    public List<ProductSkuVO> queryProductSkuListByShopProductId(Long shopProductUuid) {
        return productSkuMapper.queryProductSkuListByShopProductId(shopProductUuid);
    }

    @Override
    public ProductSkuVO queryFirstOneByShopProductId(Long shopProductId) {
        return productSkuMapper.queryFirstOneByShopProductId(shopProductId);
    }

    @Override
    public ProductSkuVO queryFirstOneByShopProductIdAndStatus(Long shopProductId,int status){
        return productSkuMapper.queryFirstOneByShopProductIdAndStatus(shopProductId,status);
    }
    @Override
    public Long queryShelvesCountByShopProductId(Long shopProductId) {
        return productSkuMapper.queryShelvesCountByShopProductId(shopProductId);
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

    @Transactional
    @Override
    public int inventoryReduction(Long skuId,Integer stockNum) {
        //锁住这条记录
        ProductSku productSku = productSkuMapper.queryBySkuIdForUpdate(skuId);
        if(productSku.getStockNum()>0) {
            return productSkuMapper.inventoryReduction(skuId,stockNum);
        }
        return 0;
    }


    @Transactional
    @Override
    public int restoreStock(Long skuId,Integer stockNum) {
        //锁住这条记录
        ProductSku productSku = productSkuMapper.queryBySkuIdForUpdate(skuId);
        return productSkuMapper.restoreStock(skuId,stockNum);
    }

    @Override
    public List<ProductSku> queryShelvesListByIdList(List<Long> idList) {
        if(CollectionUtils.isEmpty(idList))
        {
            return new ArrayList();
        }
        return productSkuMapper.queryShelvesListByIdList(idList);
    }
}
