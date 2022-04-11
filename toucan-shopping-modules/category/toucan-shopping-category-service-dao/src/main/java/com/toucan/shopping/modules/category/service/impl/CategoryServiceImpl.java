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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

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


    @Override
    public int save(Category category) {
        return categoryMapper.insert(category);
    }


    @Override
    public int saves(Category[] categorys) {
        return categoryMapper.inserts(categorys);
    }

    @Override
    public int update(Category category) {
        return categoryMapper.update(category);
    }

    @Override
    public Category queryById(Long id) {
        return categoryMapper.queryById(id);
    }

    @Override
    public List<Category> queryListByIdList(List<Long> ids) {
        return categoryMapper.queryListByIdList(ids);
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

    @Override
    public Long findCountByParentId(Long parentId) {
        return categoryMapper.findCountByParentId(parentId);
    }

    @Override
    public Long queryCount(Category category) {
        return categoryMapper.queryCount(category);
    }


    public void setChildrenByParentId(CategoryVO categoryVO,List<Category> categoryList) throws InvocationTargetException, IllegalAccessException {
        if(CollectionUtils.isNotEmpty(categoryList))
        {
            List<CategoryTreeVO> childrenCategoryVoList = new ArrayList<CategoryTreeVO>();
            for(Category category:categoryList)
            {
                CategoryTreeVO childCategoryVo = new CategoryTreeVO();
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
                categoryTreeVO.setPid(category.getParentId());
                categoryTreeVO.setChildren(new ArrayList<CategoryTreeVO>());
                if(currentNode.getPath()!=null) {
                    categoryTreeVO.setPath(currentNode.getPath() +"》"+category.getName());
                }

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



    @Override
    public List<CategoryTreeVO> queryTree() {
        List<CategoryTreeVO> categoryTreeVOS = new ArrayList<CategoryTreeVO>();
        try {
            CategoryVO query = new CategoryVO();
            List<Category> categoryVOS = categoryMapper.queryList(query);
            for (Category category : categoryVOS) {
                if (category.getParentId().longValue() == -1L) {
                    CategoryTreeVO categoryTreeVO = new CategoryTreeVO();
                    categoryTreeVO.setTitle(category.getName());
                    categoryTreeVO.setText(category.getName());
                    BeanUtils.copyProperties(categoryTreeVO, category);
                    categoryTreeVO.setChildren(new ArrayList<CategoryTreeVO>());
                    categoryTreeVOS.add(categoryTreeVO);

                    //递归查找子节点
                    setChildren(categoryVOS,categoryTreeVO);
                }
            }
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return categoryTreeVOS;
    }

    @Override
    public void setNamePath(List<CategoryVO> categoryVOS,List<Long> parentIdList) {
        if(CollectionUtils.isNotEmpty(parentIdList))
        {
            //查询出所有上级节点
            List<Category> parentCategorys = categoryMapper.queryListByIdList(parentIdList);
            parentIdList.clear();
            if(CollectionUtils.isNotEmpty(parentCategorys))
            {
                for(Category parentCategory:parentCategorys)
                {
                    for(CategoryVO categoryVO:categoryVOS)
                    {
                        if(parentCategory.getId().longValue()==categoryVO.getParentIdPoint().longValue())
                        {
                            categoryVO.setNamePath(parentCategory.getName()+"》"+categoryVO.getNamePath());
                            //移动上级ID指针
                            categoryVO.setParentIdPoint(parentCategory.getParentId());
                        }
                    }
                    if(parentCategory.getParentId()!=null&&parentCategory.getParentId().longValue()!=-1L)
                    {
                        parentIdList.add(parentCategory.getParentId());
                    }
                }
            }
            if(CollectionUtils.isNotEmpty(parentIdList))
            {
                setNamePath(categoryVOS,parentIdList);
            }
        }
    }

    @Override
    public void setPath(CategoryTreeVO categoryTreeVO) {
        if(categoryTreeVO.getParentId()!=null&&categoryTreeVO.getParentId().longValue()!=-1)
        {
            Category parentCategory = this.queryById(categoryTreeVO.getParentId());
            if(parentCategory!=null) {
                categoryTreeVO.setPath(parentCategory.getName()+"》"+categoryTreeVO.getName());
                categoryTreeVO.setParentId(parentCategory.getParentId());
                this.setPath(categoryTreeVO);
            }
        }
    }


    @Override
    public void setIdPath(CategoryTreeVO categoryTreeVO) {
        if(categoryTreeVO.getParentId()!=null&&categoryTreeVO.getParentId().longValue()!=-1)
        {
            Category parentCategory = this.queryById(categoryTreeVO.getParentId());
            if(parentCategory!=null) {
                categoryTreeVO.getIdPath().add(parentCategory.getId());
                categoryTreeVO.setParentId(parentCategory.getParentId());
                this.setIdPath(categoryTreeVO);
            }
        }
    }
}
