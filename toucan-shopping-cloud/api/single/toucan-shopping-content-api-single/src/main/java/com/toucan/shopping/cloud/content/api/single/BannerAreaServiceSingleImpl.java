package com.toucan.shopping.cloud.content.api.single;

import com.toucan.shopping.cloud.content.api.BannerAreaServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.content.business.service.BannerAreaBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerAreaServiceSingleImpl implements BannerAreaServiceAPI {

    @Autowired
    private BannerAreaBusinessService bannerAreaBusinessService;

    @Override
    public ResultObjectVO queryBannerAreaList(RequestJsonVO requestJsonVO) {
        return bannerAreaBusinessService.queryBannerAreaList(requestJsonVO);
    }

}
