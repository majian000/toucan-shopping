package com.toucan.shopping.modules.category.service;



import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.entity.CategoryHot;
import com.toucan.shopping.modules.category.page.CategoryHotTreeInfo;
import com.toucan.shopping.modules.category.page.CategoryTreeInfo;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryHotVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 热门商品分类
 */
public interface CategoryHotService {

    List<CategoryHot> queryList(CategoryHotVO category);


    /**
     * 查询树表格
     * @param queryTreeInfo
     * @return
     */
    List<CategoryHotVO> findTreeTable(CategoryHotTreeInfo queryTreeInfo);


    Long queryCount(CategoryHot category);

}
