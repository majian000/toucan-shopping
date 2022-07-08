package com.toucan.shopping.modules.admin.auth.service.impl;


import com.toucan.shopping.modules.admin.auth.constant.LoginHistoryAsyncConstant;
import com.toucan.shopping.modules.admin.auth.entity.AdminLoginHistory;
import com.toucan.shopping.modules.admin.auth.service.AdminLoginHistoryService;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AdminLoginHistoryAsyncService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private AdminLoginHistoryService adminLoginHistoryService;


    /**
     * 保存登录历史
     * 异步调用
     */
    @Async(LoginHistoryAsyncConstant.DEFAULT_TASK_EXECUTE_NAME)
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 10000, multiplier = 1, maxDelay = 60000))
    public void asyncSave(String adminId,String appCode,String ip,Integer loginSrcType)
    {
        logger.info("异步保存登录日志 adminId {} ",adminId);
        AdminLoginHistory adminLoginHistory = new AdminLoginHistory();
        adminLoginHistory.setId(idGenerator.id());
        adminLoginHistory.setAdminId(adminId);
        adminLoginHistory.setAppCode(appCode);
        adminLoginHistory.setCreateDate(new Date());
        adminLoginHistory.setIp(ip);
        adminLoginHistory.setLoginSrcType(loginSrcType);
        adminLoginHistory.setDeleteStatus((short)0);

        adminLoginHistoryService.save(adminLoginHistory);
//        for(int i=0;i<50000000;i++)
//        {
//            logger.info("测试打印 {}",i);
//        }
    }

}
