package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ShopProductApprove;
import com.toucan.shopping.modules.product.mapper.ShopProductApproveMapper;
import com.toucan.shopping.modules.product.page.ShopProductApprovePageInfo;
import com.toucan.shopping.modules.product.service.ShopProductApproveService;
import com.toucan.shopping.modules.product.vo.ShopProductApproveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ShopProductApproveServiceImpl implements ShopProductApproveService {

    @Autowired
    private ShopProductApproveMapper shopProductApproveMapper;

    @Override
    public List<ShopProductApprove> queryAllList(ShopProductApprove queryModel) {
        return shopProductApproveMapper.queryAllList(queryModel);
    }

    @Override
    public int save(ShopProductApprove entity) {
        return shopProductApproveMapper.insert(entity);
    }

    @Override
    public int updateForRepublish(ShopProductApprove entity) {
        return shopProductApproveMapper.updateForRepublish(entity);
    }

    @Override
    public int deleteById(Long id) {
        return shopProductApproveMapper.deleteById(id);
    }

    @Override
    public ShopProductApproveVO findById(Long id) {
        return shopProductApproveMapper.findById(id);
    }

    @Override
    public int updateApproveStatus(Long id, Integer approveStatus) {
        return shopProductApproveMapper.updateApproveStatus(id,approveStatus);
    }

    @Override
    public int updateApproveStatusAndRejectText(Long id, Integer approveStatus, String rejectedText) {
        return shopProductApproveMapper.updateApproveStatusAndRejectText(id,approveStatus,rejectedText);
    }

    @Override
    public int updateApproveStatusAndRejectTextAndUpdateDate(Long id, Integer approveStatus, String rejectedText, Date updateDate) {
        return shopProductApproveMapper.updateApproveStatusAndRejectTextAndUpdateDate(id,approveStatus,rejectedText,updateDate);
    }

    @Override
    public int updateApproveStatusAndProductId(Long id, Integer approveStatus, Long productId, String productUuid) {
        return shopProductApproveMapper.updateApproveStatusAndProductId(id,approveStatus,productId,productUuid);
    }

    @Override
    public int updateApproveStatusAndProductIdAndRejectText(Long id, Integer approveStatus, Long productId, String productUuid, String rejectedText) {
        return shopProductApproveMapper.updateApproveStatusAndProductIdAndRejectText(id,approveStatus,productId,productUuid,rejectedText);
    }

    @Override
    public PageInfo<ShopProductApproveVO> queryListPage(ShopProductApprovePageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<ShopProductApproveVO> pageInfo = new PageInfo();
        pageInfo.setList(shopProductApproveMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(shopProductApproveMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public List<ShopProductApproveVO> queryList(ShopProductApproveVO shopProductVO) {
        return shopProductApproveMapper.queryList(shopProductVO);
    }

    @Override
    public ShopProductApproveVO queryOne(ShopProductApproveVO shopProductVO) {
        return shopProductApproveMapper.queryOne(shopProductVO);
    }

    @Override
    public int updateShopProductId(Long id, Long shopProductId) {
        return shopProductApproveMapper.updateShopProductId(id, shopProductId);
    }
}
