package com.toucan.shopping.cloud.product.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyValueService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 商品属性名值服务
 */
@Component
public class FeignAttributeKeyValueServiceFallbackFactory implements FallbackFactory<FeignAttributeKeyValueService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignAttributeKeyValueService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignAttributeKeyValueService(){
            @Override
            public ResultObjectVO findByCategoryId(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignAttributeKeyValueService findByCategoryId faild  params{}",JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询属性列表失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryAttributeTreePage(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignAttributeKeyValueService queryAttributeTreePage faild  params{}",JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询属性列表失败");
                return resultObjectVO;
            }

        };
    }
}
