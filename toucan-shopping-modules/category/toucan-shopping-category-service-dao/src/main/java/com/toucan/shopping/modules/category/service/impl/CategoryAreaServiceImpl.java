package com.toucan.shopping.modules.category.service.impl;

import com.toucan.shopping.modules.category.entity.CategoryArea;
import com.toucan.shopping.modules.category.mapper.CategoryAreaMapper;
import com.toucan.shopping.modules.category.service.CategoryAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryAreaServiceImpl implements CategoryAreaService {

    @Autowired
    private CategoryAreaMapper categoryAreaMapper;


    @Override
    public List<CategoryArea> queryList(CategoryArea entity) {
        return categoryAreaMapper.queryList(entity);
    }

    @Override
    public int save(CategoryArea entity) {
        return categoryAreaMapper.insert(entity);
    }

    @Override
    public CategoryArea queryById(Long id) {
        return categoryAreaMapper.queryById(id);
    }

    @Override
    public int deleteById(Long id) {
        return categoryAreaMapper.deleteById(id);
    }

}
