package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ShopProduct;
import com.toucan.shopping.modules.product.mapper.ShopProductMapper;
import com.toucan.shopping.modules.product.page.ShopProductPageInfo;
import com.toucan.shopping.modules.product.service.ShopProductService;
import com.toucan.shopping.modules.product.vo.ShopProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ShopProductServiceImpl implements ShopProductService {

    @Autowired
    private ShopProductMapper shopProductMapper;

    @Override
    public List<ShopProduct> queryAllList(ShopProduct queryModel) {
        return shopProductMapper.queryAllList(queryModel);
    }

    @Override
    public int save(ShopProduct entity) {
        return shopProductMapper.insert(entity);
    }

    @Override
    public int deleteById(Long id) {
        return shopProductMapper.deleteById(id);
    }

    @Override
    public int deleteByIdAndInsertDeleteDate(Long id, Date deleteDate) {
        return shopProductMapper.deleteByIdAndInsertDeleteDate(id,deleteDate);
    }

    @Override
    public ShopProductVO findById(Long id) {
        return shopProductMapper.findById(id);
    }

    @Override
    public ShopProductVO findByIdAndStatus(Long id,int status) {
        return shopProductMapper.findByIdAndStatus(id,status);
    }
    @Override
    public int updateStatus(Long id,Long shopId, Integer status) {
        return shopProductMapper.updateStatus(id,shopId,status);
    }


    @Override
    public PageInfo<ShopProductVO> queryListPage(ShopProductPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<ShopProductVO> pageInfo = new PageInfo();
        pageInfo.setList(shopProductMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(shopProductMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public List<ShopProductVO> queryList(ShopProductVO shopProductVO) {
        return shopProductMapper.queryList(shopProductVO);
    }

    @Override
    public ShopProductVO queryOne(ShopProductVO shopProductVO) {
        return shopProductMapper.queryOne(shopProductVO);
    }
}
