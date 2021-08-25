package com.toucan.shopping.modules.seller.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.SellerLoginHistory;
import com.toucan.shopping.modules.seller.page.SellerLoginHistoryPageInfo;
import com.toucan.shopping.modules.seller.vo.SellerLoginHistoryVO;

import java.util.List;

/**
 * 卖家店铺登录历史服务
 * @author majian
 * @date 2021-8-24 16:11:11
 */
public interface SellerLoginHistoryService {


    int save(SellerLoginHistory entity);


    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<SellerLoginHistory> queryListPage(SellerLoginHistoryPageInfo queryPageInfo);

    /**
     * 查询某个人最新登录的几条数据
     * @param query
     * @return
     */
    List<SellerLoginHistory> queryListByCreateDateDesc(SellerLoginHistoryVO query);

}
