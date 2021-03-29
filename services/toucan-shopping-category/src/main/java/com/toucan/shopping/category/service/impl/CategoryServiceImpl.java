package com.toucan.shopping.category.service.impl;

import com.toucan.shopping.category.entity.Category;
import com.toucan.shopping.category.mapper.CategoryMapper;
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
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<Category> queryList(Category category) {
        return categoryMapper.queryList(category);
    }



    @Transactional
    @Override
    public int save(Category category) {
        return categoryMapper.insert(category);
    }

    @Override
    public int update(Category category) {
        return categoryMapper.update(category);
    }

    @Override
    public Category queryById(Long id) {
        return categoryMapper.queryById(id);
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        return categoryMapper.deleteById(id);
    }

    @Override
    public List<Category> findByParentId(Long parentId) {
        return categoryMapper.queryByParentId(parentId);
    }

    /**
     * 拿到指定应用的类别树
     */
    public List<CategoryVO> queryTree() throws Exception {
        List<Category> categoryList =this.findByParentId(-1L);
        List<CategoryVO> categoryVoList=new ArrayList<CategoryVO>();
        if(CollectionUtils.isNotEmpty(categoryList))
        {
            for(Category category:categoryList)
            {
                CategoryVO categoryVO = new CategoryVO();
                BeanUtils.copyProperties(categoryVO,category);
                categoryVoList.add(categoryVO);
                setChildrenByParentId(categoryVO);
            }
        }
        return categoryVoList;
    }


    @Override
    public void setChildrenByParentId(CategoryVO categoryVO) throws InvocationTargetException, IllegalAccessException {
        List<Category> childrenCategoryList = this.findByParentId(categoryVO.getId());
        if(CollectionUtils.isNotEmpty(childrenCategoryList))
        {
            List<CategoryVO> childrenCategoryVoList = new ArrayList<CategoryVO>();
            for(Category category:childrenCategoryList)
            {
                CategoryVO childCategoryVo = new CategoryVO();
                BeanUtils.copyProperties(childCategoryVo,category);
                if(category!=null&&category.getId()!=null) {
                    setChildrenByParentId(childCategoryVo);
                }
                childrenCategoryVoList.add(childCategoryVo);
            }
            categoryVO.setChildren(childrenCategoryVoList);
        }
    }

    @Override
    public void deleteChildrenByParentId(Long id) {
        List<Category> categoryList = this.findByParentId(id);
        if(CollectionUtils.isNotEmpty(categoryList))
        {
            for(Category category:categoryList)
            {
                if(category!=null&&category.getId()!=null) {
                    deleteChildrenByParentId(category.getId());
                    this.deleteById(category.getId());
                }
            }
        }
    }

}
