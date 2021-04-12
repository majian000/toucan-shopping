package com.toucan.shopping.modules.category.service.impl;

import com.toucan.shopping.modules.category.entity.CategoryImg;
import com.toucan.shopping.modules.category.mapper.CategoryImgMapper;
import com.toucan.shopping.modules.category.service.CategoryImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryImgServiceImpl implements CategoryImgService {

    @Autowired
    private CategoryImgMapper categoryImgMapper;


    @Override
    public List<CategoryImg> queryList(CategoryImg entity) {
        return categoryImgMapper.queryList(entity);
    }

    @Override
    public int save(CategoryImg entity) {
        return categoryImgMapper.insert(entity);
    }

    @Override
    public CategoryImg queryById(Long id) {
        return categoryImgMapper.queryById(id);
    }

    @Override
    public int deleteById(Long id) {
        return categoryImgMapper.deleteById(id);
    }

    @Override
    public List<CategoryImg> queryListByCategoryId(Long categoryId) {
        return categoryImgMapper.queryListByCategoryId(categoryId);
    }

}
