package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.mapper.BrandMapper;
import com.toucan.shopping.modules.product.page.BrandPageInfo;
import com.toucan.shopping.modules.product.service.BrandService;
import com.toucan.shopping.modules.product.service.BrandService;
import com.toucan.shopping.modules.product.vo.BrandVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageInfo<BrandVO> queryListPage(BrandPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<BrandVO> pageInfo = new PageInfo();
        pageInfo.setList(brandMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(brandMapper.queryListPageCount(queryPageInfo).getDataCount());
        return pageInfo;
    }

    @Override
    public List<Brand> queryByIdList(List<Long> idList) {
        if(CollectionUtils.isEmpty(idList))
        {
            return null;
        }
        return brandMapper.queryByIdList(idList);
    }


    @Override
    public List<Brand> queryAllList(Brand queryModel) {
        return brandMapper.queryAllList(queryModel);
    }

    @Override
    public List<Brand> queryList(BrandVO queryModel) {
        return brandMapper.queryList(queryModel);
    }


    @Override
    public int save(Brand entity) {
        return brandMapper.insert(entity);
    }

    @Override
    public int update(Brand entity) {
        return brandMapper.update(entity);
    }

    @Override
    public int deleteById(Long id) {
        return brandMapper.deleteById(id);
    }


}
