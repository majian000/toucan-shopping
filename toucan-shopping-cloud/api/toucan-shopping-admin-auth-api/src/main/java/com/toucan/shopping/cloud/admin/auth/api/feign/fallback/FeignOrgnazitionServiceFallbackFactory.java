package com.toucan.shopping.cloud.admin.auth.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignOrgnazitionService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignOrgnazitionService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 组织机构服务
 */
@Component
public class FeignOrgnazitionServiceFallbackFactory implements FallbackFactory<FeignOrgnazitionService> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FeignOrgnazitionService create(Throwable throwable) {
        logger.warn(throwable.getMessage(),throwable);
        return new FeignOrgnazitionService(){

            @Override
            public ResultObjectVO save(String signHeader,RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrgnazitionService.save faild params:"+ JSONObject.toJSON(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO update(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrgnazitionService.update faild params:"+ JSONObject.toJSON(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryAppOrgnazitionTreeTable(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrgnazitionService.update faild sign {} params {} ",signHeader,JSONObject.toJSON(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试!");
                return resultObjectVO;
            }


            @Override
            public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrgnazitionService.deleteById faild params:"+ JSONObject.toJSON(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO findById(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrgnazitionService.findById faild params:"+ JSONObject.toJSON(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO deleteByIds(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrgnazitionService.deleteByIds faild sign {}  params {}", signHeader,JSONObject.toJSON(requestVo));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试!");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO queryOrgnazationTree(String signHeader, RequestJsonVO requestJsonVO) {

                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时重试");
                    return resultObjectVO;
                }
                logger.warn("FeignOrgnazitionService.queryOrgnazationTree faild sign {}  params {}", signHeader,JSONObject.toJSON(requestJsonVO));
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试!");
                return resultObjectVO;
            }

        };
    }
}
