package com.toucan.shopping.modules.log.config;


import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.properties.modules.log.ReceiverProperty;
import com.toucan.shopping.modules.common.util.EmailUtils;
import com.toucan.shopping.modules.common.vo.email.EmailConfig;
import com.toucan.shopping.modules.common.vo.email.Receiver;
import com.toucan.shopping.modules.log.appender.LogEmailAppender;
import com.toucan.shopping.modules.log.queue.LogEmailQueue;
import com.toucan.shopping.modules.log.validate.LogConfigValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class LogEmailConfig {


    @Autowired
    private Toucan toucan;

    @Autowired
    private LogEmailQueue logEmailQueue;


    @PostConstruct
    public void initConfig()
    {

        //启动的时候并没有先加载配置中心的文件后再初始化对象,所以配置会有延迟,所以采用全局配置对象的这种方式处理
        LogEmailAppender.flushConfig(toucan.getModules().getLog());
        LogEmailAppender.logEmailQueue = logEmailQueue;
    }

}
