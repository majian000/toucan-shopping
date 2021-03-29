package com.toucan.shopping.category.service;




import com.toucan.shopping.category.entity.CategoryArea;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface CategoryAreaService {

    List<CategoryArea> queryList(CategoryArea entity);

    /**
     * 保存实体
     * @param entity
     * @return
     */
    int save(CategoryArea entity);



    CategoryArea queryById(Long id);

    int deleteById(Long id);

}
