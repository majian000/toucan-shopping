package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ShopProductApproveSku;
import com.toucan.shopping.modules.product.mapper.ShopProductApproveSkuMapper;
import com.toucan.shopping.modules.product.page.ShopProductApproveSkuPageInfo;
import com.toucan.shopping.modules.product.service.ShopProductApproveSkuService;
import com.toucan.shopping.modules.product.vo.ShopProductApproveSkuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ShopProductApproveSkuServiceImpl implements ShopProductApproveSkuService {

    @Autowired
    private ShopProductApproveSkuMapper shopProductApproveSkuMapper;

    @Override
    public List<ShopProductApproveSkuVO> queryList(ShopProductApproveSkuVO productSkuVO) {
        return shopProductApproveSkuMapper.queryList(productSkuVO);
    }


    @Override
    public PageInfo<ShopProductApproveSkuVO> queryListPage(ShopProductApproveSkuPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<ShopProductApproveSkuVO> pageInfo = new PageInfo();
        pageInfo.setList(shopProductApproveSkuMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(shopProductApproveSkuMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public int updateProductIdAndProductUuidByApproveId(Long approveId, Long productId, String productUuid) {
        return shopProductApproveSkuMapper.updateProductIdAndProductUuidByApproveId(approveId,productId,productUuid);
    }

    @Transactional
    @Override
    public int save(ShopProductApproveSku productSku) {
        return shopProductApproveSkuMapper.insert(productSku);
    }

    @Override
    public int saves(List<ShopProductApproveSku> productSkus) {
        if(CollectionUtils.isEmpty(productSkus))
            throw new IllegalArgumentException("实体列表参数不能为空");
        return shopProductApproveSkuMapper.inserts(productSkus);
    }

    @Override
    public ShopProductApproveSku queryById(Long id) {
        return shopProductApproveSkuMapper.queryById(id);
    }

    @Override
    public ShopProductApproveSkuVO queryVOById(Long id) {
        return shopProductApproveSkuMapper.queryVOById(id);
    }

    @Override
    public ShopProductApproveSku queryByUuid(String uuid) {
        return shopProductApproveSkuMapper.queryByUuid(uuid);
    }

    @Override
    public int deleteByShopProductApproveId(Long productApproveId) {
        return shopProductApproveSkuMapper.deleteByShopProductApproveId(productApproveId);
    }

    @Override
    public int deleteByIdList(List<Long> idList) {
        return shopProductApproveSkuMapper.deleteByIdList(idList);
    }

    @Override
    public int updateResumeByShopProductApproveId(Long productApproveId) {
        return shopProductApproveSkuMapper.updateResumeByShopProductApproveId(productApproveId);
    }

    @Override
    public List<ShopProductApproveSkuVO> queryListByProductApproveId(Long productApproveId) {
        return shopProductApproveSkuMapper.queryListByProductApproveId(productApproveId);
    }
}
