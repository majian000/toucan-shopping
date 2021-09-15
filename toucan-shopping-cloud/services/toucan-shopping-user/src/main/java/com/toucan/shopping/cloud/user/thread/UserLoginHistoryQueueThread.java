package com.toucan.shopping.cloud.user.thread;

import com.toucan.shopping.cloud.user.queue.UserLoginHistoryQueue;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.service.UserLoginHistoryService;
import com.toucan.shopping.modules.user.vo.UserLoginHistoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class UserLoginHistoryQueueThread extends Thread {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserLoginHistoryQueue userLoginHistoryQueue;

    @Autowired
    private UserLoginHistoryService userLoginHistoryService;

    @Autowired
    private Toucan toucan;

    @Override
    public void run() {

        int row = 0;
        while(true)
        {
            RequestJsonVO requestJsonVO = null;
            ResultObjectVO resultObjectVO = null;
            try {
                UserLoginHistoryVO userLoginHistoryVO = userLoginHistoryQueue.pop();
                if (userLoginHistoryVO != null) {
                    row = userLoginHistoryService.save(userLoginHistoryVO);
                    if(row<=0)
                    {
                        logger.warn("保存登录信息失败 {}",requestJsonVO.getEntityJson());
                    }
                } else {
                    //休眠5秒钟
                    this.sleep(5000);
                }
            }catch(Exception e)
            {
                logger.warn(e.getMessage(),e);
            }
        }
    }
}
