package com.toucan.shopping.starter.sign.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.auth.Sign;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.spring.context.SpringContextHolder;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.common.wrapper.RequestWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 签名校验
 */
@Component
public class SignInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private SpringContextHolder springContextHolder;

    public void responseWrite(HttpServletResponse response, String content) throws IOException {
        response.setContentType("application/json");
        response.getWriter().write(content);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ResultObjectVO resultVO = new ResultObjectVO();
        resultVO.setCode(ResultVO.SUCCESS);
        if (handler!=null&&handler instanceof HandlerMethod&&toucan.getSign().isEnabled()) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Sign signAnnotation = method.getAnnotation(Sign.class);
            if(signAnnotation!=null)
            {
                //ajax请求
                if (signAnnotation.requestType() == Sign.REQUEST_JSON) {
                    logger.info("request uri {} " , request.getRequestURI());


                    String requestSign = request.getHeader(toucan.getSign().getSignHeader());
                    if (StringUtils.isEmpty(requestSign)) {
                        logger.warn(toucan.getSign().getSignHeader()+" 签名请求头为空 ");
                        resultVO.setCode(ResultVO.FAILD);
                        resultVO.setMsg("请求失败,签名请求头为空");
                        responseWrite(response, JSONObject.toJSONString(resultVO));
                        return false;
                    }

                    //JSON类型请求
                    RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest) request);
                    String jsonBody = new String(requestWrapper.body);
                    logger.info(" request body {} " , jsonBody);

                    if (StringUtils.isEmpty(jsonBody)) {
                        logger.warn(" 请求体为 {}",jsonBody);
                        resultVO.setCode(ResultVO.FAILD);
                        resultVO.setMsg("请求失败,请求体不能为空");
                        responseWrite(response, JSONObject.toJSONString(resultVO));
                        return false;
                    }
                    RequestJsonVO requestJsonVO = JSONObject.parseObject(jsonBody, RequestJsonVO.class);
                    String sign = SignUtil.sign(requestJsonVO.getAppCode(), requestJsonVO.getEntityJson());
                    if(!StringUtils.equals(requestSign,sign))
                    {
                        logger.warn("签名校验失败");
                        resultVO.setCode(ResultVO.FAILD);
                        resultVO.setMsg("请求失败,签名校验失败");
                        responseWrite(response, JSONObject.toJSONString(resultVO));
                        return false;
                    }
                }
            }

        }

        return true;
    }
}
