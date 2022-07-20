package com.toucan.shopping.modules.content.service;



import com.toucan.shopping.modules.content.entity.Banner;
import com.toucan.shopping.modules.content.page.BannerPageInfo;
import com.toucan.shopping.modules.content.vo.BannerVO;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface BannerService {

    List<BannerVO> queryList(BannerVO banner);

    List<BannerVO> queryIndexList(BannerVO banner);


    /**
     * 保存实体
     * @param banner
     * @return
     */
    int save(Banner banner);

    int deleteById(Long id);

    int update(Banner banner);

    Banner findById(Long id);

    /**
     * 查询列表页
     * @param appPageInfo
     * @return
     */
    PageInfo<BannerVO> queryListPage(BannerPageInfo appPageInfo);



}
