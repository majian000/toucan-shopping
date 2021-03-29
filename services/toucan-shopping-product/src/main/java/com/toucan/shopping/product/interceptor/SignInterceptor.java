package com.toucan.shopping.product.interceptor;


import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.common.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 应用编码、签名校验
 */
@Component
public class SignInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ResultObjectVO resultVO = new ResultObjectVO();
        resultVO.setCode(ResultVO.SUCCESS);


//        try {
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            Method method = handlerMethod.getMethod();
//            Sign signAnnotation = method.getAnnotation(Sign.class);
//            if (signAnnotation != null&&signAnnotation.sign()) {
//                response.setCharacterEncoding(Charset.defaultCharset().name());
//
//                if(signAnnotation.type()==Sign.JSON) {
//                    //JSON类型请求
//                    RequestWrapper RequestWrapper = new RequestWrapper((HttpServletRequest) request);
//                    String jsonBody = new String(RequestWrapper.body);
//                    logger.info("auth interceptor param " + jsonBody);
//
//                    RequestJsonVO requestVo = JSONObject.parseObject(jsonBody, RequestJsonVO.class);
//                    if (StringUtils.isEmpty(requestVo.getSign())) {
//                        logger.info("sign不能为空 " + jsonBody);
//                        resultVO.setMsg("签名不能为空");
//                        response.setContentType("application/json");
//                        response.getWriter().write(JSONObject.toJSONString(resultVO));
//                        return false;
//                    }
//                    if (StringUtils.isEmpty(requestVo.getAppCode())) {
//                        logger.info("appCode不能为空 " + jsonBody);
//                        resultVO.setMsg("应用编码不能为空");
//                        response.setContentType("application/json");
//                        response.getWriter().write(JSONObject.toJSONString(resultVO));
//                        return false;
//                    }
//                    String entityJson = requestVo.getEntityJson();
//                    String sign = SignUtil.sign(requestVo.getAppCode(), entityJson);
//                    if (!StringUtils.equals(sign, requestVo.getSign())) {
//                        logger.info("sign不合法 " + jsonBody);
//                        logger.info("正确签名为 " + sign);
//                        resultVO.setMsg("签名不合法");
//                        response.setContentType("application/json");
//                        response.getWriter().write(JSONObject.toJSONString(resultVO));
//                        return false;
//                    }
//                }
//            }
            return true;
//        }catch (Exception e)
//        {
//            resultVO.setMsg("操作失败,请检查传入参数");
//            logger.warn(e.getMessage(),e);
//        }
//        return false;
    }
}
