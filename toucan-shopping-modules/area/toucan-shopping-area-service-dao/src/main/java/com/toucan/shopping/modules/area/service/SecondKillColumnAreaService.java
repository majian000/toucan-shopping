package com.toucan.shopping.modules.area.service;



import com.toucan.shopping.modules.area.entity.SecondKillColumn;
import com.toucan.shopping.modules.area.entity.SecondKillColumnArea;
import com.toucan.shopping.modules.area.vo.SecondKillColumnAreaVO;
import com.toucan.shopping.modules.area.vo.SecondKillColumnVO;

import java.util.List;

public interface SecondKillColumnAreaService {

    /**
     * 查询栏目列表
     * @param entity
     * @return
     */
    List<SecondKillColumnArea> queryList(SecondKillColumnAreaVO entity);


    /**
     * 保存实体
     * @param entity
     * @return
     */
    int save(SecondKillColumnArea entity);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

}
