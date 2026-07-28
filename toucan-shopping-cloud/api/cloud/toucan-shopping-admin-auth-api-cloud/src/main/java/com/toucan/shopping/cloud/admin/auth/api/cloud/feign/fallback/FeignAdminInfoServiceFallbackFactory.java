package com.toucan.shopping.cloud.admin.auth.api.cloud.feign.fallback;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.cloud.feign.service.FeignAdminInfoService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class FeignAdminInfoServiceFallbackFactory implements FallbackFactory<FeignAdminInfoService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignAdminInfoService create(Throwable throwable) {
        logger.warn(throwable.getMessage(), throwable);
        return new FeignAdminInfoService() {

            @Override
            public ResultObjectVO saveOrUpdate(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if (requestVo == null) {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignAdminInfoService.saveOrUpdate failed params:" + JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO findByAdminId(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if (requestVo == null) {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignAdminInfoService.findByAdminId failed params:" + JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                return resultObjectVO;
            }
        };
    }
}
