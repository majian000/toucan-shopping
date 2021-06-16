package com.toucan.shopping.modules.category.service.impl;

import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.mapper.CategoryImgMapper;
import com.toucan.shopping.modules.category.mapper.CategoryMapper;
import com.toucan.shopping.modules.category.page.CategoryTreeInfo;
import com.toucan.shopping.modules.category.service.CategoryService;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    @Autowired
    private CategoryImgMapper categoryImgMapper;

    @Override
    public List<Category> queryList(CategoryVO category) {
        return categoryMapper.queryList(category);
    }

    @Override
    public List<Category> queryPcIndexList(CategoryVO category) {
        return categoryMapper.queryPcIndexList(category);
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





    public void setChildren(List<Category> categorys, CategoryTreeVO currentNode) throws InvocationTargetException, IllegalAccessException {
        for (Category category : categorys) {
            //为当前参数的子节点
            if(category.getParentId().longValue()==currentNode.getId().longValue())
            {
                CategoryTreeVO categoryTreeVO = new CategoryTreeVO();
                BeanUtils.copyProperties(categoryTreeVO, category);
                categoryTreeVO.setTitle(category.getName());
                categoryTreeVO.setText(category.getName());
                categoryTreeVO.setChildren(new ArrayList<CategoryVO>());

                currentNode.getChildren().add(categoryTreeVO);

                //查找当前节点的子节点
                setChildren(categorys,categoryTreeVO);
            }
        }
    }




    @Override
    public List<CategoryVO> findTreeTable(CategoryTreeInfo queryTreeInfo) {
        return categoryMapper.findTreeTableByPageInfo(queryTreeInfo);
    }


    @Override
    public void queryChildren(List<Category> children, Category query) {
        List<Category> categoryList = categoryMapper.queryByParentId(query.getId());
        children.addAll(categoryList);
        for(Category category:categoryList)
        {
            queryChildren(children,category);
        }
    }


}
