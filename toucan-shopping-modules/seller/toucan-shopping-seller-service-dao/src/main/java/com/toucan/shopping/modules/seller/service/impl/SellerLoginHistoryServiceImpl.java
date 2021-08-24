package com.toucan.shopping.modules.seller.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.SellerLoginHistory;
import com.toucan.shopping.modules.seller.mapper.SellerLoginHistoryMapper;
import com.toucan.shopping.modules.seller.page.SellerLoginHistoryPageInfo;
import com.toucan.shopping.modules.seller.service.SellerLoginHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 卖家店铺服务
 * @author majian
 * @date 2021-8-4 17:44:57
 */
@Service
public class SellerLoginHistoryServiceImpl implements SellerLoginHistoryService {


    @Autowired
    private SellerLoginHistoryMapper sellerLoginHistoryMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public int save(SellerLoginHistory entity) {
        return sellerLoginHistoryMapper.insert(entity);
    }

    @Override
    public PageInfo<SellerLoginHistory> queryListPage(SellerLoginHistoryPageInfo queryPageInfo) {
        PageInfo<SellerLoginHistory> pageInfo = new PageInfo();
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        pageInfo.setList(sellerLoginHistoryMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(sellerLoginHistoryMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }
}
