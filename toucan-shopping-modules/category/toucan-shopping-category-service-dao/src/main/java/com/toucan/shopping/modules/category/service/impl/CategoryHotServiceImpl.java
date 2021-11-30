package com.toucan.shopping.modules.category.service.impl;

import com.toucan.shopping.modules.category.entity.CategoryHot;
import com.toucan.shopping.modules.category.mapper.CategoryHotMapper;
import com.toucan.shopping.modules.category.page.CategoryHotTreeInfo;
import com.toucan.shopping.modules.category.service.CategoryHotService;
import com.toucan.shopping.modules.category.vo.CategoryHotTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryHotVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryHotServiceImpl implements CategoryHotService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CategoryHotMapper categoryHotMapper;



    @Override
    public int save(CategoryHot category) {
        return categoryHotMapper.insert(category);
    }

    @Override
    public List<CategoryHot> queryList(CategoryHotVO category) {
        return categoryHotMapper.queryList(category);
    }

    @Override
    public Long queryCount(CategoryHot category) {
        return categoryHotMapper.queryCount(category);
    }


    @Override
    public List<CategoryHotVO> findTreeTable(CategoryHotTreeInfo queryTreeInfo) {
        return categoryHotMapper.findTreeTableByPageInfo(queryTreeInfo);
    }

}
