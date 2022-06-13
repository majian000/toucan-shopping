package com.toucan.shopping.starter.admin.auth.log.thread;

import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignOperateLogService;
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
    private FeignOperateLogService feignOperateLogService;

    @Autowired
    private Toucan toucan;

    private static final int GROUP_ITEM_COUNT = 20; //20个为一组

    @Override
    public void run() {
        List<OperateLogVO> requestLogVOList = new LinkedList<>();
        ResultObjectVO resultObjectVO = null;
        RequestJsonVO requestJsonVO = null;
        while(true)
        {
            try {
                OperateLogVO requestLogVO = operateLogQueue.pop();
                if (requestLogVO != null) {
                    requestLogVOList.add(requestLogVO);
                    if(requestLogVOList.size()>=GROUP_ITEM_COUNT) {
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), requestLogVOList);
                        resultObjectVO = feignOperateLogService.saves(requestJsonVO);
                        if (!resultObjectVO.isSuccess()) {
                            logger.warn("保存访问日志失败 {}", resultObjectVO.getData());
                        }
                        requestLogVOList.clear();
                    }
                } else {
                    //将不满20个的也保存上
                    if(CollectionUtils.isNotEmpty(requestLogVOList))
                    {
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), requestLogVOList);
                        resultObjectVO = feignOperateLogService.saves(requestJsonVO);
                        if (!resultObjectVO.isSuccess()) {
                            logger.warn("保存访问日志失败 {}", resultObjectVO.getData());
                        }
                        requestLogVOList.clear();
                    }
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
