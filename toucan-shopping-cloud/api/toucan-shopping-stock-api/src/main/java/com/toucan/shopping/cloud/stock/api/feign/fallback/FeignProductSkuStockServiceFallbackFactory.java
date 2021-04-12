package com.toucan.shopping.cloud.stock.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.cloud.stock.api.feign.service.FeignProductSkuStockService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 商品服务
 */
@Component
public class FeignProductSkuStockServiceFallbackFactory implements FallbackFactory<FeignProductSkuStockService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignProductSkuStockService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignProductSkuStockService(){

            @Override
            public ResultObjectVO inventoryReduction(String signHeader,RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时重试");
                    return resultObjectVO;
                }
                logger.warn("扣库存服务 sign {} , params {}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("购买失败,请重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO restoreStock(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("设置库存服务失败 params:"+JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("设置库存服务失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO restoreCacheStock(String signHeader, RequestJsonVO requestJsonVO) {

                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("恢复预扣库存服务失败 restoreCacheStock params:"+JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("恢复预扣库存服务失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryBySkuUuidList(String signHeader, RequestJsonVO requestJsonVO) {

                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("查询库存服务失败 queryBySkuUuidList params:"+JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询库存服务失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryStockCacheBySkuUuidList(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("查询库存服务失败 queryStockCacheBySkuUuidList params:"+JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询库存服务失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO deductCacheStock(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("预扣库存服务失败 deductCacheStock params:"+JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("预扣库存服务失败");
                return resultObjectVO;
            }

        };
    }
}
