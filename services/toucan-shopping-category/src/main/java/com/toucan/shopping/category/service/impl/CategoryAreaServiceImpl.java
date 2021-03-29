package com.toucan.shopping.category.service.impl;

import com.toucan.shopping.category.entity.Category;
import com.toucan.shopping.category.entity.CategoryArea;
import com.toucan.shopping.category.mapper.CategoryAreaMapper;
import com.toucan.shopping.category.mapper.CategoryMapper;
import com.toucan.shopping.category.service.CategoryAreaService;
import com.toucan.shopping.category.service.CategoryService;
import com.toucan.shopping.category.vo.CategoryVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
