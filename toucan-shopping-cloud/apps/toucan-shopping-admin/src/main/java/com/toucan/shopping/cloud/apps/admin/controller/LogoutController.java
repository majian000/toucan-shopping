package com.toucan.shopping.cloud.apps.admin.controller;


import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private AdminServiceAPI adminServiceAPI;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 退出登录
     */
    @RequestMapping(value = "/out", method = RequestMethod.POST)
    public ResultObjectVO out(HttpServletRequest request, HttpServletResponse response) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String adminId = AdminLoginHolder.getCurrentAdminId();
            String loginToken = AuthHeaderUtil.getToken(toucan.getAppCode(), request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader()));
            if (StringUtils.isEmpty(adminId)) {
                resultObjectVO.setMsg("没有找到adminId");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            if (StringUtils.isEmpty(loginToken)) {
                resultObjectVO.setMsg("没有找到loginToken");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            AdminVO adminVO = new AdminVO();
            adminVO.setAdminId(adminId);
            adminVO.setLoginToken(loginToken);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), adminVO);
            return adminServiceAPI.logout(requestJsonVO);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }

        return resultObjectVO;
    }

}
