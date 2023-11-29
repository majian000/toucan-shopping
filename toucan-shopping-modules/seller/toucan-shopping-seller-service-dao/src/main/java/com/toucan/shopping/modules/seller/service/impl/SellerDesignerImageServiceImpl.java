package com.toucan.shopping.modules.seller.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.SellerDesignerImage;
import com.toucan.shopping.modules.seller.mapper.SellerDesignerImageMapper;
import com.toucan.shopping.modules.seller.page.SellerDesignerImagePageInfo;
import com.toucan.shopping.modules.seller.service.SellerDesignerImageService;
import com.toucan.shopping.modules.seller.vo.SellerDesignerImageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerDesignerImageServiceImpl implements SellerDesignerImageService {

    @Autowired
    private SellerDesignerImageMapper sellerDesignerImageMapper;


    @Override
    public SellerDesignerImage findById(Long id) {
        return sellerDesignerImageMapper.findById(id);
    }

    @Override
    public PageInfo<SellerDesignerImageVO> queryListPage(SellerDesignerImagePageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<SellerDesignerImageVO> pageInfo = new PageInfo();
        pageInfo.setList(sellerDesignerImageMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(sellerDesignerImageMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }
}
