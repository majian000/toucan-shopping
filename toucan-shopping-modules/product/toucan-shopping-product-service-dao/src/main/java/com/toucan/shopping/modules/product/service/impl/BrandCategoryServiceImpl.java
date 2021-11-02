package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.entity.BrandCategory;
import com.toucan.shopping.modules.product.mapper.BrandCategoryMapper;
import com.toucan.shopping.modules.product.mapper.BrandMapper;
import com.toucan.shopping.modules.product.page.BrandCategoryPageInfo;
import com.toucan.shopping.modules.product.service.BrandCategoryService;
import com.toucan.shopping.modules.product.service.BrandService;
import com.toucan.shopping.modules.product.vo.BrandCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class BrandCategoryServiceImpl implements BrandCategoryService {

    @Autowired
    private BrandCategoryMapper brandCategoryMapper;

    @Override
    public List<BrandCategory> queryAllList(BrandCategory queryModel) {
        return brandCategoryMapper.queryAllList(queryModel);
    }

    @Override
    public List<BrandCategory> queryList(BrandCategory queryModel) {
        return brandCategoryMapper.queryList(queryModel);
    }

    @Override
    public int save(BrandCategory entity) {
        return brandCategoryMapper.insert(entity);
    }

    @Override
    public List<BrandCategoryVO> queryListByBrandIds(List<Long> brandIds) {
        if(CollectionUtils.isEmpty(brandIds)) {
            return null;
        }
        return brandCategoryMapper.queryListByBrandIds(brandIds);
    }

    @Override
    public PageInfo<BrandCategoryVO> queryListPage(BrandCategoryPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<BrandCategoryVO> pageInfo = new PageInfo();
        pageInfo.setList(brandCategoryMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(brandCategoryMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public List<Long> queryBrandIdListByCategoryId(List<Long> categoryIds) {
        return brandCategoryMapper.queryBrandIdListByCategoryId(categoryIds);
    }
}
