package com.toucan.shopping.user.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.user.api.feign.service.FeignUserService;
import com.toucan.shopping.user.export.entity.User;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务
 */
@Component
public class FeignUserServiceFallbackFactory implements FallbackFactory<FeignUserService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignUserService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignUserService(){


            @Override
            public ResultObjectVO registByMobilePhone(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.registByMobile faild header {} params {} ",signHeader,requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO loginByPassword(String signHeader,RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.loginByPassword失败 params: {} ",requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }


            @Override
            public ResultObjectVO loginByVCode(User user) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(user==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.loginByVCode失败 params:"+JSONObject.toJSONString(user));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO isOnline(String signHeader,RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.isOnline失败 header{} params {}",signHeader,requestVo.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                resultObjectVO.setData(false);
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO findByMobilePhone(String signHeader,RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.findByMobilePhone失败 header{} params {}",signHeader,requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO list(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.list失败 header{} params {}",signHeader,requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }
        };
    }
}
