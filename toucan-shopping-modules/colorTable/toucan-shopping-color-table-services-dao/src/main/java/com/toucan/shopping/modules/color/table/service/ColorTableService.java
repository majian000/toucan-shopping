package com.toucan.shopping.modules.color.table.service;


import com.toucan.shopping.modules.color.table.entity.ColorTable;
import com.toucan.shopping.modules.color.table.page.ColorTablePageInfo;
import com.toucan.shopping.modules.color.table.vo.ColorTableVO;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.util.List;

public interface ColorTableService {

    List<ColorTableVO> queryList(ColorTableVO entity);



    /**
     * 保存实体
     * @param entity
     * @return
     */
    int save(ColorTable entity);

    int deleteById(Long id);

    int update(ColorTable entity);


    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    PageInfo<ColorTableVO> queryListPage(ColorTablePageInfo pageInfo);
    
    
}
