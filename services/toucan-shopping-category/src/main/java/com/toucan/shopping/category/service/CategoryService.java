package com.toucan.shopping.category.service;



import com.toucan.shopping.category.entity.Category;
import com.toucan.shopping.category.vo.CategoryVO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface CategoryService {

    List<Category> queryList(CategoryVO category);

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

    void deleteChildrenByParentId(Long id);

    /**
     * 填充类别树
     * @return
     */
    List<CategoryVO> queryTree(String areaCode)  throws Exception;

}
