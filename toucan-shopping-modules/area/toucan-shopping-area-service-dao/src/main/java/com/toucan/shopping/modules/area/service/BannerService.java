package com.toucan.shopping.modules.area.service;



import com.toucan.shopping.modules.area.entity.Area;
import com.toucan.shopping.modules.area.entity.Banner;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.area.vo.BannerVO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface BannerService {

    List<Banner> queryList(BannerVO banner);


    /**
     * 保存实体
     * @param banner
     * @return
     */
    int save(Banner banner);

    int deleteById(Long id);

}
