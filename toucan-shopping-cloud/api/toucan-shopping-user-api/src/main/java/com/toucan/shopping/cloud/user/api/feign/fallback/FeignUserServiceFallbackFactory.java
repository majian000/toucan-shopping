package com.toucan.shopping.cloud.user.api.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.entity.User;
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
                logger.warn("调用FeignUserService.registByMobile 失败 header {} params {} ",signHeader,requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO resetPassword(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.resetPassword 失败 header {} params {} ",signHeader,requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO connectUsername(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.connectUsername 失败 header {} params {} ",signHeader,requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO connectEmail(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.connectEmail 失败 header {} params {} ",signHeader,requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO connectMobilePhone(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.connectMobilePhone 失败 header {} params {} ",signHeader,requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO updateDetail(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.updateDetail 失败 header {} params {} ",signHeader,requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO registByUsername(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.registByUsername 失败 header {} params {} ",signHeader,requestJsonVO.getEntityJson());
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
            public ResultObjectVO queryLoginInfo(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.queryLoginInfo失败 header{} params {}",signHeader,requestVo.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                resultObjectVO.setData(false);
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO findByUserMainIdForCacheOrDB(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.findByUserMainIdForCacheOrDB失败 header{} params {}",signHeader,requestVo.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                resultObjectVO.setData(false);
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO verifyRealName(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.verifyRealName失败 header{} params {}",signHeader,requestVo.getEntityJson());
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

            @Override
            public ResultObjectVO mobilePhoneList(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.mobilePhoneList失败 header{} params {}",signHeader,requestVo.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO emailList(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.emailList失败 header{} params {}",signHeader,requestVo.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO usernameList(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.usernameList header{} params {}",signHeader,requestVo.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO findUsernameListByUsername(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.findUsernameListByUsername失败 header{} params {}",signHeader,requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO findEmailListByEmail(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.findEmailListByEmail失败 header{} params {}",signHeader,requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO disabledEnabledById(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.disabledEnabledById失败 header{} params {}",signHeader,requestVo.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO disabledEnabledMobilePhoneByUserMainIdAndMobilePhone(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.disabledEnabledMobilePhoneByUserMainIdAndMobilePhone失败 header{} params {}",signHeader,requestVo.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO disabledEnabledEmailByUserMainIdAndEmail(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.disabledEnabledEmailByUserMainIdAndEmail失败 header{} params {}",signHeader,requestVo.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO disabledEnabledUsernameByUserMainIdAndUsername(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.disabledEnabledUsernameByUserMainIdAndUsername失败 header{} params {}",signHeader,requestVo.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO disabledByIds(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.disabledByIds失败 header{} params {}",signHeader,requestVo.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO findByUserMainId(String signHeader, RequestJsonVO requestVo) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestVo==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.findListByUserMainId失败 header{} params {}",signHeader,requestVo.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

            @Override
            public ResultObjectVO flushCache(String signHeader, RequestJsonVO requestJsonVO) {
                ResultObjectVO resultObjectVO = new ResultObjectVO();
                if(requestJsonVO==null)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("超时请重试");
                    return resultObjectVO;
                }
                logger.warn("调用FeignUserService.flushCache header{} params {}",signHeader,requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时请重试");
                return resultObjectVO;
            }

        };
    }
}
