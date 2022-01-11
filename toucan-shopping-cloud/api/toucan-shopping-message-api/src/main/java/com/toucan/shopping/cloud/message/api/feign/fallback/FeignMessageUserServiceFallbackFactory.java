package com.toucan.shopping.cloud.message.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.message.api.feign.service.FeignMessageUserService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 消息服务
 */
@Component
public class FeignMessageUserServiceFallbackFactory implements FallbackFactory<FeignMessageUserService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignMessageUserService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignMessageUserService(){

            @Override
            public ResultObjectVO send(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignMessageService.save失败  params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignMessageService.queryListPage失败  params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryListPageByUserMianId(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignMessageService.queryListPageByUserMianId失败  params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO update(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignMessageService.update失败  params{}",JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO findById(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignMessageService.findById失败  params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryUnreadCountByUserMainId(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignMessageService.queryUnreadCountByUserMainId失败  params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO updateReadStatus(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignMessageService.updateReadStatus失败  params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO updateAllReadStatus(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignMessageService.updateAllReadStatus失败  params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }
        };
    }
}
