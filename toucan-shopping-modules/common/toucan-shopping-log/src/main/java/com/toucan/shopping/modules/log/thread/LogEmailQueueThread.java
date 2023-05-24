package com.toucan.shopping.modules.log.thread;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.EmailHelper;
import com.toucan.shopping.modules.log.message.LogEmailMessage;
import com.toucan.shopping.modules.log.queue.LogEmailQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class LogEmailQueueThread extends Thread {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private LogEmailQueue logEmailQueue;

    @Override
    public void run() {

        while(true)
        {
            try {
                LogEmailMessage logEmailMessage = logEmailQueue.pop();
                if (logEmailMessage != null) {
                    if(logEmailMessage.getEmail()!=null) {
                        EmailHelper.send(logEmailMessage.getEmail());
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
