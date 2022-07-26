package com.toucan.shopping.modules.seller.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.mapper.SellerShopMapper;
import com.toucan.shopping.modules.seller.page.SellerShopPageInfo;
import com.toucan.shopping.modules.seller.service.SellerShopService;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 卖家店铺服务
 * @author majian
 * @date 2021-8-4 17:44:57
 */
@Service
public class SellerShopServiceImpl implements SellerShopService {


    @Autowired
    private SellerShopMapper sellerShopMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public int save(SellerShop entity) {
        return sellerShopMapper.insert(entity);
    }



    @Override
    public int deleteById(Long id) {
        return sellerShopMapper.deleteById(id);
    }

    @Override
    public int update(SellerShop entity) {
        return sellerShopMapper.update(entity);
    }

    @Override
    public int updateLogo(SellerShop entity) {
        return sellerShopMapper.updateLogo(entity);
    }


    @Override
    public int updateInfo(SellerShop entity) {
        return sellerShopMapper.updateInfo(entity);
    }

    @Override
    public List<SellerShop> findListByEntity(SellerShop query) {
        return sellerShopMapper.findListByEntity(query);
    }

    @Override
    public SellerShop findById(Long id) {
        return sellerShopMapper.findById(id);
    }

    @Override
    public List<SellerShop> findEnabledByUserMainId(Long userMainId) {
        return sellerShopMapper.findEnabledByUserMainId(userMainId);
    }

    @Override
    public SellerShop findOneEnabledByUserMainId(Long userMainId) {
        return sellerShopMapper.findOneEnabledByUserMainId(userMainId);
    }



    @Override
    public SellerShop findByUserMainId(Long userMainId) {
        return sellerShopMapper.findByUserMainId(userMainId);
    }


    @Override
    public List<SellerShop> queryList(SellerShopVO queryModel) {
        return sellerShopMapper.queryList(queryModel);
    }


    @Override
    public PageInfo<SellerShop> queryListPage(SellerShopPageInfo queryPageInfo) {
        PageInfo<SellerShop> pageInfo = new PageInfo();
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        pageInfo.setList(sellerShopMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(sellerShopMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

}
