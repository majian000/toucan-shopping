package com.toucan.shopping.cloud.seller.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignFreightTemplateService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 运费模板
 * @author majian
 * @date 2022-9-21 14:12:48
 */
@Component
public class FeignFreightTemplateServiceFallbackFactory implements FallbackFactory<FeignFreightTemplateService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignFreightTemplateService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignFreightTemplateService(){

            @Override
            public ResultObjectVO queryListPage( RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignSellerLoginHistoryService.save失败 params{}",JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存登录历史失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO save(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignSellerLoginHistoryService.save失败 params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO update(RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignSellerLoginHistoryService.update失败 params{}",JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO findById(RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignSellerLoginHistoryService.findById失败 params{}",JSONObject.toJSONString(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignSellerLoginHistoryService.deleteById失败 signHeader{} params{}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("删除失败");
                return resultObjectVO;
            }

        };
    }
}
