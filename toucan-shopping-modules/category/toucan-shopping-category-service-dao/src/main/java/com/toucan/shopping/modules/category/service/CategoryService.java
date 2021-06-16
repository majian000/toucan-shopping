package com.toucan.shopping.modules.category.service;



import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.page.CategoryTreeInfo;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface CategoryService {

    List<Category> queryList(CategoryVO category);

    List<Category> queryPcIndexList(CategoryVO category);

    /**
     * 保存实体
     * @param category
     * @return
     */
    int save(Category category);


    int update(Category category);

    Category queryById(Long id);

    int deleteById(Long id);

    List<Category> findByParentId(Long parentId);

    /**
     * 删除所有子节点
     * @param id
     */
    void deleteChildrenByParentId(Long id);



    /**
     * 从一个集合中找到所有子节点并设置上
     * @param categoryVOS
     * @param currentNode
     */
    void setChildren(List<Category> categoryVOS, CategoryTreeVO currentNode) throws InvocationTargetException, IllegalAccessException ;


    /**
     * 查询树表格
     * @param queryTreeInfo
     * @return
     */
    List<CategoryVO> findTreeTable(CategoryTreeInfo queryTreeInfo);



    /**
     * 查询所有子节点
     * @param children
     * @param query
     */
    void queryChildren(List<Category> children,Category query);


}
