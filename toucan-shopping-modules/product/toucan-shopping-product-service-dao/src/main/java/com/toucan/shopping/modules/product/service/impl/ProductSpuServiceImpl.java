package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ProductSpu;
import com.toucan.shopping.modules.product.mapper.ProductSpuMapper;
import com.toucan.shopping.modules.product.page.ProductSpuPageInfo;
import com.toucan.shopping.modules.product.service.ProductSpuService;
import com.toucan.shopping.modules.product.vo.ProductSpuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSpuServiceImpl implements ProductSpuService {

    @Autowired
    private ProductSpuMapper productSpuMapper;

    @Override
    public List<ProductSpu> queryAllList(ProductSpu queryModel) {
        return productSpuMapper.queryAllList(queryModel);
    }

    @Override
    public int save(ProductSpu entity) {
        return productSpuMapper.insert(entity);
    }

    @Override
    public int update(ProductSpuVO entity) {
        return productSpuMapper.update(entity);
    }

    @Override
    public List<ProductSpuVO> queryList(ProductSpuVO query) {
        return productSpuMapper.queryList(query);
    }

    @Override
    public int deleteById(Long id) {
        return productSpuMapper.deleteById(id);
    }

    @Override
    public PageInfo<ProductSpuVO> queryListPage(ProductSpuPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<ProductSpuVO> pageInfo = new PageInfo();
        pageInfo.setList(productSpuMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(productSpuMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public ProductSpu queryByIdIgnoreDelete(Long id) {
        return productSpuMapper.queryByIdIgnoreDelete(id);
    }
}
