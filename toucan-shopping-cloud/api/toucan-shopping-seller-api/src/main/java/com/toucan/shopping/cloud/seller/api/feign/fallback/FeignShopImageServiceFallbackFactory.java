package com.toucan.shopping.cloud.seller.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopBannerService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopImageService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 店铺图片
 */
@Component
public class FeignShopImageServiceFallbackFactory implements FallbackFactory<FeignShopImageService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignShopImageService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignShopImageService(){


            @Override
            public ResultObjectVO findById(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignShopImageService.findById失败  params{}",JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询轮播图失败");
                return resultObjectVO;
            }

        };
    }
}
