package com.toucan.shopping.modules.seller.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.SellerDesignerPageModel;
import com.toucan.shopping.modules.seller.mapper.SellerDesignerPageModelMapper;
import com.toucan.shopping.modules.seller.page.SellerDesignerPageModelPageInfo;
import com.toucan.shopping.modules.seller.service.SellerDesignerPageModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 卖家设计器页面
 * @author majian
 * @date 2023-10-25 16:35:56
 */
@Service
public class SellerDesignerPageModelServiceImpl implements SellerDesignerPageModelService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SellerDesignerPageModelMapper sellerDesignerPageModelMapper;

    @Override
    public int save(SellerDesignerPageModel sellerDesignerPageModel) {
        return sellerDesignerPageModelMapper.insert(sellerDesignerPageModel);
    }
    @Override
    public int update(SellerDesignerPageModel sellerDesignerPageModel) {
        return sellerDesignerPageModelMapper.update(sellerDesignerPageModel);
    }


    @Override
    public SellerDesignerPageModel queryLastOne(SellerDesignerPageModel sellerDesignerPageModel) {
        return sellerDesignerPageModelMapper.queryLastOne(sellerDesignerPageModel);
    }

    @Override
    public PageInfo<SellerDesignerPageModel> queryListPage(SellerDesignerPageModelPageInfo queryPageInfo) {
        PageInfo<SellerDesignerPageModel> pageInfo = new PageInfo();
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        pageInfo.setList(sellerDesignerPageModelMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(sellerDesignerPageModelMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }
}
