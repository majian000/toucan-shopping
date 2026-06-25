package com.toucan.shopping.cloud.content.api.single;

import com.toucan.shopping.cloud.content.api.feign.service.FeignBannerService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.content.business.service.BannerBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerServiceSingleImpl implements FeignBannerService {

    @Autowired
    private BannerBusinessService bannerBusinessService;

    @Override
    public ResultObjectVO queryListPage(String signHeader, RequestJsonVO requestJsonVO) {
        return bannerBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryList(String signHeader, RequestJsonVO requestJsonVO) {
        return bannerBusinessService.queryList(requestJsonVO);
    }

    @Override
    public ResultObjectVO flushWebIndexCache(String signHeader, RequestJsonVO requestVo) {
        return bannerBusinessService.flushWebIndexCache(requestVo);
    }

    @Override
    public ResultObjectVO queryIndexList(String signHeader, RequestJsonVO requestJsonVO) {
        return bannerBusinessService.queryIndexList(requestJsonVO);
    }

    @Override
    public ResultObjectVO clearWebIndexCache(String signHeader, RequestJsonVO requestVo) {
        return bannerBusinessService.clearWebIndexCache(requestVo);
    }

    @Override
    public ResultObjectVO save(String signHeader, RequestJsonVO requestVo) {
        return bannerBusinessService.save(requestVo);
    }

    @Override
    public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
        return bannerBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
        return bannerBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
        return bannerBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
        return bannerBusinessService.deleteByIds(requestVo);
    }

}
