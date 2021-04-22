package com.toucan.shopping.modules.area.service;



import com.toucan.shopping.modules.area.entity.Banner;
import com.toucan.shopping.modules.area.entity.SecondKillColumn;
import com.toucan.shopping.modules.area.vo.BannerVO;
import com.toucan.shopping.modules.area.vo.SecondKillColumnVO;

import java.util.List;

public interface SecondKillColumnService {

    /**
     * 查询栏目列表
     * @param secondKillColumnVO
     * @return
     */
    List<SecondKillColumn> queryList(SecondKillColumnVO secondKillColumnVO);


    /**
     * 保存实体
     * @param entity
     * @return
     */
    int save(SecondKillColumn entity);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

}
