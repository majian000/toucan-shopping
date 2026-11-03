package com.toucan.shopping.cloud.apps.h5.config;

import com.toucan.shopping.modules.common.context.ToucanApplicationContext;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.user.util.LoginTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * 启动后参数替换（设置登录 token 盐、会话 cookie 前缀、最大上传大小等）
 */
@Configuration
public class AfterStartConfig {

    @Autowired
    private Toucan toucan;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @PostConstruct
    public void initAppCode() {
        // H5 与 PC web 共用同一套 user 登录逻辑，token 盐一致；
        // 会话独立靠 cookie 前缀不同：PC=tss_，H5=h5_（鉴权层只认 h5_*）
        LoginTokenUtil.salt = toucan.getUserAuth().getLoginSalt();
        UserAuthHeaderUtil.cookiePrefix = "h5_";
        ToucanApplicationContext.setMaxFileSize(maxFileSize);
    }
}
