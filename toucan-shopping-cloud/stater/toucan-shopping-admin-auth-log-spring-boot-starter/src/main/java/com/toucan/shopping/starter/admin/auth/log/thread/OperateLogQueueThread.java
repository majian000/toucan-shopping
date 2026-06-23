package com.toucan.shopping.starter.admin.auth.log.thread;

import com.toucan.shopping.cloud.admin.auth.api.OperateLogServiceAPI;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.starter.admin.auth.log.queue.OperateLogQueue;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
@Component
public class OperateLogQueueThread extends Thread {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OperateLogQueue operateLogQueue;

    @Autowired
    private OperateLogServiceAPI operateLogServiceAPI;

    @Autowired
    private Toucan toucan;

    private static final int GROUP_ITEM_COUNT = 20; //20个为一组

    private static final int MAX_CONSECUTIVE_FAILURES = 3; //连续失败最大次数

    @Override
    public void run() {
        List<OperateLogVO> requestLogVOList = new LinkedList<>();
        ResultObjectVO resultObjectVO = null;
        RequestJsonVO requestJsonVO = null;
        int consecutiveFailures = 0;
        while(true)
        {
            try {
                OperateLogVO requestLogVO = operateLogQueue.pop();
                if (requestLogVO != null) {
                    requestLogVOList.add(requestLogVO);
                    if(requestLogVOList.size()>=GROUP_ITEM_COUNT) {
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), requestLogVOList);
                        resultObjectVO = operateLogServiceAPI.saves(requestJsonVO);
                        if (!resultObjectVO.isSuccess()) {
                            logger.warn("保存访问日志失败 {}", resultObjectVO.getData());
                        }
                        requestLogVOList.clear();
                        consecutiveFailures = 0; //成功后重置
                    }
                } else {
                    //将不满20个的也保存上
                    if(CollectionUtils.isNotEmpty(requestLogVOList))
                    {
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), requestLogVOList);
                        resultObjectVO = operateLogServiceAPI.saves(requestJsonVO);
                        if (!resultObjectVO.isSuccess()) {
                            logger.warn("保存访问日志失败 {}", resultObjectVO.getData());
                        }
                        requestLogVOList.clear();
                        consecutiveFailures = 0; //成功后重置
                    }
                    //休眠5秒钟
                    this.sleep(5000);
                }
            }catch(Exception e)
            {
                consecutiveFailures++;
                logger.warn("保存操作日志异常(第{}次连续失败): {}", consecutiveFailures, e.getMessage());
                if(consecutiveFailures >= MAX_CONSECUTIVE_FAILURES) {
                    logger.error("保存操作日志连续失败{}次,停止重试,线程退出", MAX_CONSECUTIVE_FAILURES);
                    break;
                }
                //失败后休眠5秒再重试
                try {
                    this.sleep(5000);
                } catch (InterruptedException ie) {
                    logger.warn("线程休眠被中断", ie);
                }
            }
        }
    }
}
