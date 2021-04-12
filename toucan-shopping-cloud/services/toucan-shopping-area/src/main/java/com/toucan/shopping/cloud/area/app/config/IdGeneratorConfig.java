package com.toucan.shopping.cloud.area.app.config;

import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SnowflakeIdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdGeneratorConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private Toucan toucan;

    @Bean
    public IdGenerator idGenerator()
    {
        logger.info(" 初始化雪花算法ID生成器 工作ID {} 数据中心ID {}", toucan.getWorkerId(), toucan.getDatacenterId());
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(toucan.getWorkerId(), toucan.getDatacenterId());
        IdGenerator idGenerator = new IdGenerator();
        idGenerator.setSnowflakeIdWorker(snowflakeIdWorker);
        return idGenerator;
    }
}
