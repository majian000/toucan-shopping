package com.toucan.shopping.cloud.product.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultListVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 商品服务
 */
@Component
public class FeignProductSkuServiceFallbackFactory implements FallbackFactory<FeignProductSkuService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignProductSkuService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignProductSkuService(){
            @Override
            public ResultListVO queryShelvesList(@RequestHeader("toucan-sign-header") String signHeader, RequestJsonVO requestJsonVO) {
                ResultListVO resultListVO = new ResultListVO();
                if(requestJsonVO==null)
                {
                    resultListVO.setCode(ResultObjectVO.FAILD);
                    resultListVO.setMsg("请求超时,请稍后重试");
                    return resultListVO;
                }
                logger.warn("查询上架商品列表服务失败 headers:{} params:{}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultListVO.setCode(ResultObjectVO.FAILD);
                resultListVO.setMsg("请求超时,请稍后重试");
                return resultListVO;
            }


            @Override
            public ResultObjectVO queryById(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("查询商品服务 params:"+JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,请重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryByIdList(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignProductSkuService.queryByIdList faild sign {} , params {}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,请重试!");
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
                logger.warn("FeignProductSkuService queryListPage faild   params {}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,请重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryList(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignProductSkuService queryList faild   params {}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,请重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryByIdForFront(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignProductSkuService queryByIdForFront faild   params {}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,请重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO updateStock(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignProductSkuService updateStock faild   params {}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("修改库存失败,请重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO updatePrice(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignProductSkuService updatePrice faild   params {}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("修改价格失败,请重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryOneByShopProductIdForFrontPreview(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignProductSkuService queryOneByShopProductIdForFrontPreview faild   params {}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,请重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryByIdForFrontPreview(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignProductSkuService queryByIdForFrontPreview faild   params {}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,请重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryOneByShopProductIdForFront(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignProductSkuService queryOneByShopProductIdForFront faild   params {}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,请重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO shelves(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignProductSkuService shelves faild  params {}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,请重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO inventoryReduction(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignProductSkuService inventoryReduction faild   params {}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("扣库存失败,请重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO restoreStock(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignProductSkuService restoreStock faild   params {}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("还原库存失败,请重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO updatePreviewPhoto(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignProductSkuService updatePreviewPhoto faild   params {}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("更新预览图失败,请重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO updateDescriptionPhoto(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignProductSkuService updateDescriptionPhoto faild   params {}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("更新介绍图失败,请重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO removeDescriptionPhoto(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignProductSkuService removeDescriptionPhoto faild   params {}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("删除介绍图失败,请重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryListByShopProductIdList(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求超时,请稍后重试");
                    return resultObjectVO;
                }
                logger.warn("FeignProductSkuService queryListByShopProductIdList faild   params {}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,请重试!");
                return resultObjectVO;
            }
        };
    }
}
