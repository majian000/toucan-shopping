package com.toucan.shopping.category.service;




import com.toucan.shopping.category.entity.CategoryImg;

import java.util.List;

public interface CategoryImgService {

    List<CategoryImg> queryList(CategoryImg entity);

    /**
     * 保存实体
     * @param entity
     * @return
     */
    int save(CategoryImg entity);



    CategoryImg queryById(Long id);

    int deleteById(Long id);

    List<CategoryImg> queryListByCategoryId(Long categoryId);

}
