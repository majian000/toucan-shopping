package com.toucan.shopping.cloud.content.api.single;

import com.toucan.shopping.cloud.content.api.feign.service.FeignBannerAreaService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.content.business.service.BannerAreaBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerAreaServiceSingleImpl implements FeignBannerAreaService {

    @Autowired
    private BannerAreaBusinessService bannerAreaBusinessService;

    @Override
    public ResultObjectVO queryBannerAreaList(String signHeader, RequestJsonVO requestJsonVO) {
        return bannerAreaBusinessService.queryBannerAreaList(requestJsonVO);
    }

}
