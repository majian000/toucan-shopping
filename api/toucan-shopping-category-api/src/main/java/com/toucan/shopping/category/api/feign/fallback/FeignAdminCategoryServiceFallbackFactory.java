package com.toucan.shopping.category.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.category.api.feign.service.FeignAdminCategoryService;
import com.toucan.shopping.category.api.feign.service.FeignCategoryService;
import com.toucan.shopping.category.export.entity.Category;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 商品服务
 */
@Component
public class FeignAdminCategoryServiceFallbackFactory implements FallbackFactory<FeignAdminCategoryService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignAdminCategoryService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignAdminCategoryService(){

            @Override
            public ResultObjectVO save(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignAdminCategoryService save faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("创建类别失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryById(String signHeader,RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignAdminCategoryService queryById faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("创建类别失败");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryByIdList(String signHeader,RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请重试");
                    return resultObjectVO;
                }
                logger.warn("FeignAdminCategoryService queryByIdList faild header:{} params:{}",signHeader,JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("创建类别失败");
                return resultObjectVO;
            }
        };
    }
}
