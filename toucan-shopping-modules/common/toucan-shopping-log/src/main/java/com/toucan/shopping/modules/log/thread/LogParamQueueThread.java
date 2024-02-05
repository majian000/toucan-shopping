package com.toucan.shopping.modules.log.thread;

import com.toucan.shopping.modules.common.util.EmailHelper;
import com.toucan.shopping.modules.log.helper.LogParamHelper;
import com.toucan.shopping.modules.log.message.LogEmailMessage;
import com.toucan.shopping.modules.log.queue.LogEmailQueue;
import com.toucan.shopping.modules.log.queue.LogParamQueue;
import com.toucan.shopping.modules.log.vo.LogParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class LogParamQueueThread extends Thread {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private LogParamQueue logParamQueue;

    @Autowired
    private LogParamHelper logParamHelper;

    @Override
    public void run() {

        while(true)
        {
            try {
                LogParam logParam = logParamQueue.pop();
                if (logParam != null) {
                    logParamHelper.execute(logParam);
                } else {
                    //休眠5秒钟
                    this.sleep(5000);
                }
            }catch(Exception e)
            {
                logger.error(e.getMessage(),e);
            }
        }
    }


}
