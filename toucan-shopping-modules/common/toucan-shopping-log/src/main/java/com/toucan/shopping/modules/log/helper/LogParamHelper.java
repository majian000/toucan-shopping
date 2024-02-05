package com.toucan.shopping.modules.log.helper;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import com.toucan.shopping.modules.log.vo.LogParam;
import com.toucan.shopping.modules.log.vo.LogParamHeader;
import com.toucan.shopping.modules.log.vo.LogParamRequest;
import com.toucan.shopping.modules.log.vo.LogParamResponse;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 参数日志
 */
@Component
public class LogParamHelper {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void execute(LogParam logParam){
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append("log param=================");
        LogParamRequest request = logParam.getRequest();
        LogParamResponse response = logParam.getResponse();
        if(request!=null) {
            logBuilder.append("-------------request------------");
            logBuilder.append("uri:"+ request.getUri());
            logBuilder.append("ip:"+request.getIp());
            logBuilder.append("method:"+ request.getMethod());
            logBuilder.append("contentType:"+ request.getContentType());
            logBuilder.append("---------header---------");
            if(CollectionUtils.isNotEmpty(request.getHeaders()))
            {
                for(LogParamHeader header:request.getHeaders())
                {
                    logBuilder.append(header.getName()+":"+header.getValue());
                }
            }
            logBuilder.append("-----------------------");
            logBuilder.append("body:"+request.getBody());
            logBuilder.append("--------------------------------");
        }
        if(response!=null)
        {
            logBuilder.append("-------------response------------");
            logBuilder.append("uri:"+ request.getUri());
            logBuilder.append("contentType:"+ request.getContentType());
            logBuilder.append("body:"+request.getBody());
            logBuilder.append("--------------------------------");
        }
        logger.info(logBuilder.toString());
    }


}
