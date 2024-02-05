package com.toucan.shopping.modules.log.queue;

import com.toucan.shopping.modules.log.message.LogEmailMessage;
import com.toucan.shopping.modules.log.vo.LogParam;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 日志参数队列
 */
@Component
public class LogParamQueue {


    private ConcurrentLinkedQueue<LogParam> logParamQueue = new ConcurrentLinkedQueue<LogParam>();

    public void push(LogParam logParam)
    {
        logParamQueue.add(logParam);
    }

    public LogParam pop()
    {
        return logParamQueue.poll();
    }

}
