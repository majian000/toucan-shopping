package com.toucan.shopping.modules.log.helper;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import com.toucan.shopping.modules.log.vo.LogParam;
import com.toucan.shopping.modules.log.vo.LogParamHeader;
import com.toucan.shopping.modules.log.vo.LogParamRequest;
import com.toucan.shopping.modules.log.vo.LogParamResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
        logBuilder.append("\n=================log param=================\n");
        LogParamRequest request = logParam.getRequest();
        LogParamResponse response = logParam.getResponse();
        if(request!=null) {
            logBuilder.append("-------------request------------\n");
            logBuilder.append("uri:"+ request.getUri()+"\n");
            logBuilder.append("ip:"+request.getIp()+"\n");
            logBuilder.append("method:"+ request.getMethod()+"\n");
            logBuilder.append("contentType:"+ request.getContentType()+"\n");
            if(CollectionUtils.isNotEmpty(request.getHeaders()))
            {
                logBuilder.append("---------header---------\n");
                for(LogParamHeader header:request.getHeaders())
                {
                    logBuilder.append(header.getName()+":"+header.getValue()+"\n");
                }
                logBuilder.append("\n");
            }
            if(StringUtils.isNotEmpty(request.getBody())) {
                logBuilder.append(request.getBody() + "\n");
            }
        }
        if(response!=null)
        {
            logBuilder.append("-------------response------------\n");
            logBuilder.append("uri:"+ response.getUri()+"\n");
            logBuilder.append("contentType:"+ response.getContentType()+"\n");
            logBuilder.append("\n");
            if(StringUtils.isNotEmpty(response.getBody())) {
                logBuilder.append(response.getBody() + "\n");
            }
        }
        logBuilder.append("\n=======================================\n");
        logger.info(logBuilder.toString());
    }


}
