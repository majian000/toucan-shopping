package com.toucan.shopping.modules.seller.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.ShopBanner;
import com.toucan.shopping.modules.seller.mapper.ShopBannerMapper;
import com.toucan.shopping.modules.seller.page.ShopBannerPageInfo;
import com.toucan.shopping.modules.seller.service.ShopBannerService;
import com.toucan.shopping.modules.seller.vo.ShopBannerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopBannerServiceImpl implements ShopBannerService {

    @Autowired
    private ShopBannerMapper shopBannerMapper;


    @Override
    public List<ShopBannerVO> queryList(ShopBannerVO entity) {
        return shopBannerMapper.queryList(entity);
    }

    @Override
    public List<ShopBannerVO> queryIndexList(ShopBannerVO banner) {
        return shopBannerMapper.queryIndexList(banner);
    }

    @Override
    public int save(ShopBanner banner) {
        return shopBannerMapper.insert(banner);
    }

    @Override
    public int deleteById(Long id) {
        return shopBannerMapper.deleteById(id);
    }

    @Override
    public int deleteByIdAndShopId(Long id,Long shopId){
        return shopBannerMapper.deleteByIdAndShopId(id,shopId);
    }

    @Override
    public int update(ShopBanner banner) {
        return shopBannerMapper.update(banner);
    }

    @Override
    public ShopBanner findById(Long id) {
        return shopBannerMapper.findById(id);
    }

    @Override
    public PageInfo<ShopBannerVO> queryListPage(ShopBannerPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<ShopBannerVO> pageInfo = new PageInfo();
        pageInfo.setList(shopBannerMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(shopBannerMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }


}
