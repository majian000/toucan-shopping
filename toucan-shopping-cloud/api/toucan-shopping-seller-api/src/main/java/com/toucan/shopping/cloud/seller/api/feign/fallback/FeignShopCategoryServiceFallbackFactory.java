package com.toucan.shopping.cloud.seller.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 商户店铺分类服务
 */
@Component
public class FeignShopCategoryServiceFallbackFactory implements FallbackFactory<FeignShopCategoryService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignShopCategoryService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignShopCategoryService(){
            @Override
            public ResultObjectVO save(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("保存店铺分类失败");
                    return resultObjectVO;
                }
                logger.warn("FeignShopCategoryService.save失败 sign{} params{}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存店铺分类失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO update(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("编辑店铺分类失败");
                    return resultObjectVO;
                }
                logger.warn("FeignShopCategoryService.update失败 sign{} params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("编辑店铺分类失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("删除店铺分类失败");
                    return resultObjectVO;
                }
                logger.warn("FeignShopCategoryService.deleteById失败 sign{} params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("删除店铺分类失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryAllList(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("查询店铺分类失败");
                    return resultObjectVO;
                }
                logger.warn("FeignShopCategoryService.queryAllList失败 sign{} params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询店铺分类失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryById(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("查询店铺分类失败");
                    return resultObjectVO;
                }
                logger.warn("FeignShopCategoryService.queryById失败 sign{} params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询店铺分类失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryByIdList(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("查询店铺分类失败");
                    return resultObjectVO;
                }
                logger.warn("FeignShopCategoryService.queryByIdList失败 sign{} params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询店铺分类失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO flushCache(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("刷新店铺分类缓存失败");
                    return resultObjectVO;
                }
                logger.warn("FeignShopCategoryService.flushCache失败 sign{} params{}",JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("刷新店铺分类缓存失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO clearCache(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("清空店铺分类缓存失败");
                    return resultObjectVO;
                }
                logger.warn("FeignShopCategoryService.clearCache失败 sign{} params{}",JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("清空店铺分类缓存失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO findById(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("查询店铺分类失败");
                    return resultObjectVO;
                }
                logger.warn("FeignShopCategoryService.findById失败 sign{} params{}",JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询店铺分类失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO findByIdArray(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("查询店铺分类失败");
                    return resultObjectVO;
                }
                logger.warn("FeignShopCategoryService.findByIdArray失败 sign{} params{}",JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询店铺分类失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryTree(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("查询店铺分类树失败");
                    return resultObjectVO;
                }
                logger.warn("FeignShopCategoryService.queryTree失败 sign{} params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询店铺分类树失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryWebIndexTree(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("查询店铺首页分类树失败");
                    return resultObjectVO;
                }
                logger.warn("FeignShopCategoryService.queryWebIndexTree失败 sign{} params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询店铺首页分类树失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryTreeTable(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("查询店铺分类树表格失败");
                    return resultObjectVO;
                }
                logger.warn("FeignShopCategoryService.queryTreeTable失败 sign{} params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询店铺分类树表格失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryListByPid(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("查询店铺分类列表失败");
                    return resultObjectVO;
                }
                logger.warn("FeignShopCategoryService.queryListByPid失败 sign{} params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询店铺分类列表失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("查询店铺分类树列表失败");
                    return resultObjectVO;
                }
                logger.warn("FeignShopCategoryService.queryTreeTableByPid失败 sign{} params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询店铺分类树列表失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("删除店铺分类失败");
                    return resultObjectVO;
                }
                logger.warn("FeignShopCategoryService.deleteById失败 sign{} params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("删除店铺分类失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("删除店铺分类失败");
                    return resultObjectVO;
                }
                logger.warn("FeignShopCategoryService.deleteByIds失败 sign{} params{}",JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("删除店铺分类失败");
                return resultObjectVO;
            }
        };
    }
}
