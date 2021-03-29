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
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryAreaMapper categoryAreaMapper;

    @Override
    public List<Category> queryList(CategoryVO category) {
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
    public List<CategoryVO> queryTree(String areaCode) throws Exception {
        List<CategoryVO> categoryVoList=new ArrayList<CategoryVO>();
        //拿到所有关联关系
        List<CategoryArea> categoryAreas = categoryAreaMapper.queryListByAreaCode(areaCode);
        if(CollectionUtils.isNotEmpty(categoryAreas))
        {
            Long[] categoryIdArray = new Long[categoryAreas.size()];
            for(int i=0;i<categoryAreas.size();i++)
            {
                categoryIdArray[i]=categoryAreas.get(i).getCategoryId();
            }
            CategoryVO categoryVO = new CategoryVO();
            categoryVO.setIdArray(categoryIdArray);
            //拿到地区下关联所有类别
            List<Category> categoryList =this.queryList(categoryVO);
            if(CollectionUtils.isNotEmpty(categoryList))
            {
                //先查出顶级节点
                for(Category category:categoryList)
                {
                    if(category.getParentId()==-1L)
                    {
                        CategoryVO rootCategoryVO = new CategoryVO();
                        BeanUtils.copyProperties(rootCategoryVO,category);
                        categoryVoList.add(rootCategoryVO);
                        //开始填充下级节点
                        setChildrenByParentId(rootCategoryVO,categoryList);
                    }
                }
            }
        }
        return categoryVoList;
    }


    public void setChildrenByParentId(CategoryVO categoryVO,List<Category> categoryList) throws InvocationTargetException, IllegalAccessException {
        if(CollectionUtils.isNotEmpty(categoryList))
        {
            List<CategoryVO> childrenCategoryVoList = new ArrayList<CategoryVO>();
            for(Category category:categoryList)
            {
                CategoryVO childCategoryVo = new CategoryVO();
                BeanUtils.copyProperties(childCategoryVo,category);
                if(category!=null&&categoryVO.getId().longValue() == category.getParentId().longValue()) {
                    childrenCategoryVoList.add(childCategoryVo);
                    setChildrenByParentId(childCategoryVo,categoryList);
                }
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
