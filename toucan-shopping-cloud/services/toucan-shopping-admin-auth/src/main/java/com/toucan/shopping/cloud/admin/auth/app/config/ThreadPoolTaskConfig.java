package com.toucan.shopping.cloud.admin.auth.app.config;


import com.toucan.shopping.modules.admin.auth.constant.LoginHistoryAsyncConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Nonnull;
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

        executor.setTaskDecorator(new ThreadContextDecorator());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}


/**
 * 保存请求上下文到子线程中
 */
class ThreadContextDecorator implements TaskDecorator {
    @Override
    @Nonnull
    public Runnable decorate(@Nonnull Runnable runnable) {

        // 获取主线程中的请求信息（我们的用户信息也放在里面）
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return () -> {
            try {
                // 将主线程的请求信息，设置到子线程中
                RequestContextHolder.setRequestAttributes(attributes);
                // 执行子线程，这一步不要忘了
                runnable.run();
            } finally {
                // 线程结束，清空这些信息，否则可能造成内存泄漏
                RequestContextHolder.resetRequestAttributes();
            }
        };
    }
}
