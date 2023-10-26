package com.toucan.shopping.cloud.seller.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerDesignerPageService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopBannerService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 设计器页面
 */
@Component
public class FeignSellerDesignerPageServiceFallbackFactory implements FallbackFactory<FeignSellerDesignerPageService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignSellerDesignerPageService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignSellerDesignerPageService(){
            @Override
            public ResultObjectVO onlySaveOne(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignSellerDesignerPageService.saveOrUpdate失败  params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存设计器页面失败");
                return resultObjectVO;
            }

        };
    }
}
