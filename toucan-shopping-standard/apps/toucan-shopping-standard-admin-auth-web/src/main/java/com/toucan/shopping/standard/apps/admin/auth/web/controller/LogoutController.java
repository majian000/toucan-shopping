package com.toucan.shopping.standard.apps.admin.auth.web.controller;


import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.standard.admin.auth.proxy.service.AdminServiceProxy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private AdminServiceProxy adminServiceProxy;

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
            String adminId = AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader()));
            String loginToken = AuthHeaderUtil.getToken(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader()));
            if(StringUtils.isEmpty(adminId))
            {
                resultObjectVO.setMsg("没有找到adminId");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(loginToken))
            {
                resultObjectVO.setMsg("没有找到loginToken");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            AdminVO adminVO = new AdminVO();
            adminVO.setAdminId(adminId);
            adminVO.setLoginToken(loginToken);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),adminVO);
            return adminServiceProxy.logout(requestJsonVO);
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }





}

