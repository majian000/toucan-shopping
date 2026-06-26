package com.toucan.shopping.cloud.common.data.api.cloud.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.common.data.api.cloud.feign.service.FeignCategoryHotService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 热门分类服务
 */
@Component
public class FeignCategoryHotServiceFallbackFactory implements FallbackFactory<FeignCategoryHotService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignCategoryHotService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignCategoryHotService(){

            @Override
            public ResultObjectVO save(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryHotService.save faild params:{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryById(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryHotService.queryById faild params:{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryByIdList(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryHotService.queryByIdList faild params:{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO findByIdArray(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryHotService.findByIdArray faild params:{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }


            @Override
            public ResultObjectVO findById(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryHotService.findById faild params:{}",JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryTreeTable(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryHotService.queryTreeTable faild params:{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO update(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryHotService.update faild params:{}",JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryHotService.queryTreeTableByPid faild params:{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryTree(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryHotService.queryTree faild params:{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryWebIndexTree(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryHotService.queryWebIndexTree faild params:{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO flushWebIndexCache(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryHotService.flushWebIndexCache faild params:{}",JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }


            @Override
            public ResultObjectVO clearWebIndexCache(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryHotService.clearWebIndexCache faild params:{}",JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryHotService.deleteByIds faild params:{}",JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO deleteById(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryHotService.deleteById faild params:{}",JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryListByPid(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryHotService.queryListByPid faild params:{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryChildListByPid(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryHotService.queryChildListByPid faild params:{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }
        };
    }
}
