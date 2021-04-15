package com.toucan.shopping.modules.category.service;



import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.vo.CategoryVO;

import java.util.List;

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
