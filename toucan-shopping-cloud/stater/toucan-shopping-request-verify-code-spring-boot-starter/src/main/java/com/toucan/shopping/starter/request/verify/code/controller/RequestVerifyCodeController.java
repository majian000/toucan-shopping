package com.toucan.shopping.starter.request.verify.code.controller;

import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.util.VerifyCodeUtil;
import com.toucan.shopping.starter.request.verify.code.redis.RequestVerifyCodeRedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;


@RestController("apiRequestVerifyCodeController")
@RequestMapping("/api/request/verify/code")
public class RequestVerifyCodeController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping(value="/vcode", method = RequestMethod.GET)
    public void verifyCode(HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            int w = 200, h = 80;
            //生成4位验证码
            String code = VerifyCodeUtil.generateVerifyCode(4);


            //生成客户端验证码ID
            String vcodeUuid = GlobalUUID.uuid();
            String vcodeRedisKey = RequestVerifyCodeRedisKey.getRequestVerifyCodeKey(toucan.getAppCode(),vcodeUuid);
            stringRedisTemplate.opsForValue().set(vcodeRedisKey,code);
            stringRedisTemplate.expire(vcodeRedisKey,60, TimeUnit.SECONDS);

            Cookie clientVCodeId = new Cookie(toucan.getAppCode()+"_requestClientVCodeId",vcodeUuid);
            clientVCodeId.setPath("/");
            //60秒过期
            clientVCodeId.setMaxAge(60);
            response.addCookie(clientVCodeId);

            VerifyCodeUtil.outputImage(w, h, outputStream, code);
        } catch (IOException e) {
            logger.warn(e.getMessage(),e);
        }finally{
            if(outputStream!=null)
            {
                try {
                    outputStream.flush();
                    outputStream.close();
                }catch(Exception e)
                {
                    logger.warn(e.getMessage(),e);
                }
            }
        }
    }
}
