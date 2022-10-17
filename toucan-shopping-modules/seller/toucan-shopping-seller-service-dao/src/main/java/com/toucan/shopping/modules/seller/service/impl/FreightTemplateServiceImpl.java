package com.toucan.shopping.modules.seller.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.FreightTemplate;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.mapper.FreightTemplateMapper;
import com.toucan.shopping.modules.seller.mapper.SellerShopMapper;
import com.toucan.shopping.modules.seller.page.FreightTemplatePageInfo;
import com.toucan.shopping.modules.seller.page.SellerShopPageInfo;
import com.toucan.shopping.modules.seller.service.FreightTemplateService;
import com.toucan.shopping.modules.seller.service.SellerShopService;
import com.toucan.shopping.modules.seller.vo.FreightTemplateVO;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 运费模板
 * @author majian
 * @date 2022-9-21 11:26:55
 */
@Service
public class FreightTemplateServiceImpl implements FreightTemplateService {


    @Autowired
    private FreightTemplateMapper freightTemplateMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public PageInfo<FreightTemplate> queryListPage(FreightTemplatePageInfo queryPageInfo) {
        PageInfo<FreightTemplate> pageInfo = new PageInfo();
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        pageInfo.setList(freightTemplateMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(freightTemplateMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public Long queryCount(FreightTemplateVO freightTemplateVO) {
        return freightTemplateMapper.queryCount(freightTemplateVO);
    }

    @Override
    public int save(FreightTemplate entity) {
        return freightTemplateMapper.insert(entity);
    }

}
