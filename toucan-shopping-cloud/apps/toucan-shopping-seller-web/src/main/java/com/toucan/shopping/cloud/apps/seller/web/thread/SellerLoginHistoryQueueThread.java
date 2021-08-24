package com.toucan.shopping.cloud.apps.seller.web.thread;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.seller.web.queue.SellerLoginHistoryQueue;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerLoginHistoryService;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.vo.SellerLoginHistoryVO;
import com.toucan.shopping.modules.user.kafka.constant.UserMessageTopicConstant;
import com.toucan.shopping.modules.user.kafka.message.UserCreateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class SellerLoginHistoryQueueThread extends Thread {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SellerLoginHistoryQueue sellerLoginHistoryQueue;

    @Autowired
    private FeignSellerLoginHistoryService feignSellerLoginHistoryService;

    @Autowired
    private Toucan toucan;

    @Override
    public void run() {

        while(true)
        {
            RequestJsonVO requestJsonVO = null;
            ResultObjectVO resultObjectVO = null;
            try {
                SellerLoginHistoryVO sellerLoginHistoryVO = sellerLoginHistoryQueue.pop();
                if (sellerLoginHistoryVO != null) {
                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),sellerLoginHistoryVO);
                    resultObjectVO = feignSellerLoginHistoryService.save(requestJsonVO.sign(),requestJsonVO);
                    if(!resultObjectVO.isSuccess())
                    {
                        logger.warn("保存卖家登录信息失败 {}",requestJsonVO.getEntityJson());
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
