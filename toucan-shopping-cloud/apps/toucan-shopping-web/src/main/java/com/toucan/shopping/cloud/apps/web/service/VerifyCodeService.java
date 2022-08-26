package com.toucan.shopping.cloud.apps.web.service;

import com.toucan.shopping.modules.common.vo.ResultObjectVO;

import javax.servlet.http.HttpServletRequest;

public interface VerifyCodeService {

    /**
     * 校验验证码
     * @param request
     * @param vode
     * @return
     */
    ResultObjectVO verifyCode(HttpServletRequest request, String vode,String appCode) throws Exception;
}
