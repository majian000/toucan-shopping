package com.toucan.shopping.cloud.content.api.single;

import com.toucan.shopping.cloud.content.api.BannerServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.content.business.service.BannerBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerServiceSingleImpl implements BannerServiceAPI {

    @Autowired
    private BannerBusinessService bannerBusinessService;

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return bannerBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryList(RequestJsonVO requestJsonVO) {
        return bannerBusinessService.queryList(requestJsonVO);
    }

    @Override
    public ResultObjectVO flushWebIndexCache(RequestJsonVO requestVo) {
        return bannerBusinessService.flushWebIndexCache(requestVo);
    }

    @Override
    public ResultObjectVO queryIndexList(RequestJsonVO requestJsonVO) {
        return bannerBusinessService.queryIndexList(requestJsonVO);
    }

    @Override
    public ResultObjectVO clearWebIndexCache(RequestJsonVO requestVo) {
        return bannerBusinessService.clearWebIndexCache(requestVo);
    }

    @Override
    public ResultObjectVO save(RequestJsonVO requestVo) {
        return bannerBusinessService.save(requestVo);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestVo) {
        return bannerBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return bannerBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        return bannerBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return bannerBusinessService.deleteByIds(requestVo);
    }

}
