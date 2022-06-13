package com.toucan.shopping.starter.admin.auth.log.queue;

import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogVO;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 访问日志队列
 */
@Component
public class OperateLogQueue {

    private ConcurrentLinkedQueue<OperateLogVO> operateLogVOS = new ConcurrentLinkedQueue<OperateLogVO>();

    public void push(OperateLogVO RequestLogVO)
    {
        operateLogVOS.add(RequestLogVO);
    }

    public OperateLogVO pop()
    {
        return operateLogVOS.poll();
    }
}
