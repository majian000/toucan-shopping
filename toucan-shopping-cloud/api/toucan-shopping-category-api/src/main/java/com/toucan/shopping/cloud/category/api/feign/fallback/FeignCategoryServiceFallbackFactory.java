package com.toucan.shopping.cloud.category.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.category.api.feign.service.FeignCategoryService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 商品服务
 */
@Component
public class FeignCategoryServiceFallbackFactory implements FallbackFactory<FeignCategoryService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignCategoryService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignCategoryService(){

            @Override
            public ResultObjectVO save(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryService.save faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryById(String signHeader,RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryService.queryById faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryByIdList(String signHeader,RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryService.queryByIdList faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO findByIdArray(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryService.findByIdArray faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
                return resultObjectVO;
            }


            @Override
            public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryService.findById faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryTreeTable(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryService.queryTreeTable faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryService.update faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryTreeTableByPid(String signHeader,RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryService.queryTreeTableByPid faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryTree(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryService.queryTree faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryWebIndexTree(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryService.queryWebIndexTree faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO flushWebIndexCache(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryService.flushWebIndexCache faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
                return resultObjectVO;
            }


            @Override
            public ResultObjectVO clearWebIndexCache(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryService.clearWebIndexCache faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryService.deleteByIds faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryService.deleteById faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryListByPid(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryService.queryListByPid faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryChildListByPid(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignCategoryService.queryChildListByPid faild  params:{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
                return resultObjectVO;
            }
        };
    }
}
