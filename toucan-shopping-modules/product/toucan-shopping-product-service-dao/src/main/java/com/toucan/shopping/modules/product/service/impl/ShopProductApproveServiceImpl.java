package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ShopProductApprove;
import com.toucan.shopping.modules.product.mapper.ShopProductApproveMapper;
import com.toucan.shopping.modules.product.page.ShopProductApprovePageInfo;
import com.toucan.shopping.modules.product.service.ShopProductApproveService;
import com.toucan.shopping.modules.product.vo.ShopProductApproveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopProductApproveServiceImpl implements ShopProductApproveService {

    @Autowired
    private ShopProductApproveMapper shopProductMapper;

    @Override
    public List<ShopProductApprove> queryAllList(ShopProductApprove queryModel) {
        return shopProductMapper.queryAllList(queryModel);
    }

    @Override
    public int save(ShopProductApprove entity) {
        return shopProductMapper.insert(entity);
    }

    @Override
    public int deleteById(Long id) {
        return shopProductMapper.deleteById(id);
    }

    @Override
    public ShopProductApproveVO findById(Long id) {
        return shopProductMapper.findById(id);
    }

    @Override
    public int updateApproveStatus(Long id, Integer approveStatus) {
        return shopProductMapper.updateApproveStatus(id,approveStatus);
    }

    @Override
    public int updateApproveStatusAndProductId(Long id, Integer approveStatus, Long productId, String productUuid) {
        return shopProductMapper.updateApproveStatusAndProductId(id,approveStatus,productId,productUuid);
    }

    @Override
    public PageInfo<ShopProductApproveVO> queryListPage(ShopProductApprovePageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<ShopProductApproveVO> pageInfo = new PageInfo();
        pageInfo.setList(shopProductMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(shopProductMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public List<ShopProductApproveVO> queryList(ShopProductApproveVO shopProductVO) {
        return shopProductMapper.queryList(shopProductVO);
    }
}
