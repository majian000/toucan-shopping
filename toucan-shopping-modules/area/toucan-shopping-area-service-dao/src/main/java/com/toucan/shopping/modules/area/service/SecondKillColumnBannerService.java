package com.toucan.shopping.modules.area.service;



import com.toucan.shopping.modules.area.entity.SecondKillColumnArea;
import com.toucan.shopping.modules.area.entity.SecondKillColumnBanner;
import com.toucan.shopping.modules.area.vo.SecondKillColumnAreaVO;
import com.toucan.shopping.modules.area.vo.SecondKillColumnBannerVO;

import java.util.List;

public interface SecondKillColumnBannerService {

    /**
     * 查询栏目列表
     * @param entity
     * @return
     */
    List<SecondKillColumnBanner> queryList(SecondKillColumnBannerVO entity);


    /**
     * 保存实体
     * @param entity
     * @return
     */
    int save(SecondKillColumnBanner entity);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

}
