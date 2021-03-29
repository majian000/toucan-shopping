package com.toucan.shopping.center.admin.auth.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.center.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 管理员服务
 */
@Component
public class FeignAdminServiceFallbackFactory implements FallbackFactory<FeignAdminService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignAdminService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignAdminService(){

            @Override
            public ResultObjectVO login(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时重试");
                    return resultObjectVO;
                }
                logger.warn("调用用户中心登录服务失败 params:"+ JSONObject.toJSON(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryLoginToken(RequestJsonVO requestVo) {

                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时重试");
                    return resultObjectVO;
                }
                logger.warn("调用用户中心查询登录Token服务失败 params:"+ JSONObject.toJSON(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO isOnline(RequestJsonVO requestVo) {

                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时重试");
                    return resultObjectVO;
                }
                logger.warn("调用用户中心查询是否在线服务失败 params:"+ JSONObject.toJSON(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试!");
                return resultObjectVO;
            }
        };
    }
}
