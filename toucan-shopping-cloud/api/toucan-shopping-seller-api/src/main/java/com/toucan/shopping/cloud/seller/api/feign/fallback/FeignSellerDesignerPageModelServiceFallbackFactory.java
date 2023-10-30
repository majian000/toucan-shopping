package com.toucan.shopping.cloud.seller.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerDesignerPageModelService;
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
public class FeignSellerDesignerPageModelServiceFallbackFactory implements FallbackFactory<FeignSellerDesignerPageModelService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignSellerDesignerPageModelService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignSellerDesignerPageModelService(){
            @Override
            public ResultObjectVO onlySaveOne(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignSellerDesignerPageModelService.saveOrUpdate失败  params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存设计器页面失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryLastOne(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignSellerDesignerPageModelService.queryLastOne失败  params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存设计器页面失败");
                return resultObjectVO;
            }

        };
    }
}
