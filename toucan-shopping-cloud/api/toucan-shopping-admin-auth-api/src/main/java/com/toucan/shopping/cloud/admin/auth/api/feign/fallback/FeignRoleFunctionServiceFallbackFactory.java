package com.toucan.shopping.cloud.admin.auth.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignRoleFunctionService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignRoleService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 角色功能项服务
 */
@Component
public class FeignRoleFunctionServiceFallbackFactory implements FallbackFactory<FeignRoleFunctionService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignRoleFunctionService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignRoleFunctionService(){

            @Override
            public ResultObjectVO saveFunctions(String signHeader, RequestJsonVO requestJsonVO) {

                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时重试");
                    return resultObjectVO;
                }

                logger.warn("FeignRoleFunctionService.saveFunctions faild sign {} params {}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryRoleFunctionList(String signHeader, RequestJsonVO requestJsonVO) {

                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时重试");
                    return resultObjectVO;
                }

                logger.warn("FeignRoleFunctionService.queryRoleFunctionList faild sign {} params {}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO list(String signHeader, RequestJsonVO requestVo) {

                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时重试");
                    return resultObjectVO;
                }

                logger.warn("FeignRoleFunctionService.list faild sign {} params {}",signHeader,JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试!");
                return resultObjectVO;
            }
        };
    }
}
