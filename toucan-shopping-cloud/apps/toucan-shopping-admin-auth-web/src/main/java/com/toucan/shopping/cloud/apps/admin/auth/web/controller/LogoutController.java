package com.toucan.shopping.cloud.apps.admin.auth.web.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.cloud.apps.admin.auth.web.util.VCodeUtil;
import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.util.VerifyCodeUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignAdminService feignAdminService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public String loginPage()
    {
        return "login.html";
    }





    /**
     * 退出登录
     * @param request
     * @return
     */
    @RequestMapping(value = "/out",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO out(HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            String adminId = AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader()));
            String loginToken = AuthHeaderUtil.getToken(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader()));
            if(StringUtils.isEmpty(adminId))
            {
                resultObjectVO.setMsg("请求失败,没有找到adminId");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(loginToken))
            {
                resultObjectVO.setMsg("请求失败,没有找到loginToken");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            AdminVO adminVO = new AdminVO();
            adminVO.setAdminId(adminId);
            adminVO.setLoginToken(loginToken);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),adminVO);
            return feignAdminService.logout(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }





}

