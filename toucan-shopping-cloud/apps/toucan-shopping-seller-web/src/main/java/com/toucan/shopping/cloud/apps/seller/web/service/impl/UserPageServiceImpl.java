package com.toucan.shopping.cloud.apps.seller.web.service.impl;

import com.toucan.shopping.cloud.apps.seller.web.redis.UserLoginRedisKey;
import com.toucan.shopping.cloud.apps.seller.web.service.UserPageService;
import com.toucan.shopping.modules.common.util.IPUtil;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserPageServiceImpl implements UserPageService {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Override
    public String loginPage(HttpServletRequest request) {
        try {
            //查询登录次数,失败3次要求输入验证码
            String loginFaildCountKey = UserLoginRedisKey.getLoginFaildCountKey(IPUtil.getRemoteAddr(request));
            Object loginFaildCountValueObject = toucanStringRedisService.get(loginFaildCountKey);
            if (loginFaildCountValueObject != null) {
                Integer faildCount = Integer.parseInt(String.valueOf(loginFaildCountValueObject));
                if (faildCount >= 3) {
                    request.setAttribute("isShowInputVcode", true);
                } else {
                    request.setAttribute("isShowInputVcode", false);
                }
            } else {
                request.setAttribute("isShowInputVcode", false);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            request.setAttribute("isShowInputVcode", false);
        }
        return "user/login";
    }
}
