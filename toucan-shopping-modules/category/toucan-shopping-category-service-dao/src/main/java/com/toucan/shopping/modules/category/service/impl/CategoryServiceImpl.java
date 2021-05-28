package com.toucan.shopping.modules.category.service.impl;

import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.entity.CategoryArea;
import com.toucan.shopping.modules.category.mapper.CategoryAreaMapper;
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
    private CategoryAreaMapper categoryAreaMapper;

    @Autowired
    private CategoryImgMapper categoryImgMapper;

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
            categoryVO.setShowStatus(1);
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
                        //设置类别广告图片
                        rootCategoryVO.setCategoryImgs(categoryImgMapper.queryListByCategoryId(rootCategoryVO.getId()));
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
        List<CategoryVO> retNodes = new ArrayList<CategoryVO>();
        List<CategoryVO> nodes = categoryMapper.findTreeTableByPageInfo(queryTreeInfo);
        if(!CollectionUtils.isEmpty(nodes))
        {
            retNodes.addAll(nodes);
        }
        return retNodes;
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
