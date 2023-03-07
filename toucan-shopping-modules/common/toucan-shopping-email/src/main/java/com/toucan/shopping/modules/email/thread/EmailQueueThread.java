package com.toucan.shopping.modules.email.thread;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.util.EmailHelper;
import com.toucan.shopping.modules.email.message.EmailMessage;
import com.toucan.shopping.modules.email.queue.EmailQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class EmailQueueThread extends Thread {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EmailQueue emailQueue;

    @Override
    public void run() {

        while(true)
        {
            try {
                EmailMessage emailMessage = emailQueue.pop();
                if (emailMessage != null) {
                    logger.info("从邮件队列中拿到对象 {}", JSONObject.toJSONString(emailMessage));
                    if(emailMessage.getEmail()!=null) {
                        EmailHelper.send(emailMessage.getEmail());
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
