package com.toucan.shopping.cloud.admin.auth.app.config;


import com.toucan.shopping.modules.admin.auth.constant.LoginHistoryAsyncConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class ThreadPoolTaskConfig {


    @Value("${spring.task.execution.pool.core-threads}")
    private int corePoolSize;

    @Value("${spring.task.execution.pool.max-threads}")
    private int maxPoolSize;

    @Value("${spring.task.execution.pool.queue-capacity}")
    private int queueCapacity;

    @Value("${spring.task.execution.pool.keep-alive}")
    private int keepAliveSeconds;

    @Value("${spring.task.execution.pool.name-prefix}")
    private String threadNamePrefix;


    @Bean(LoginHistoryAsyncConstant.DEFAULT_TASK_EXECUTE_NAME)
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(threadNamePrefix);

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
