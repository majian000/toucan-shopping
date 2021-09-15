package com.toucan.shopping.cloud.user.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserLoginHistoryService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.entity.User;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 用户登录历史服务
 */
@Component
public class FeignUserLoginHistoryServiceFallbackFactory implements FallbackFactory<FeignUserLoginHistoryService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignUserLoginHistoryService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignUserLoginHistoryService(){

            @Override
            public ResultObjectVO queryListPage(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignSmsService.queryListPage失败 signHeader{} params{}",signHeader,JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryListByLatest10(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignSmsService.queryListByLatest10失败 signHeader{} params{}",signHeader,JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }
        };
    }
}
