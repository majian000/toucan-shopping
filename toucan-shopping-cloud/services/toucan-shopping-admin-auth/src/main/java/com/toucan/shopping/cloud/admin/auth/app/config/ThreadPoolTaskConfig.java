package com.toucan.shopping.cloud.admin.auth.app.config;


import com.toucan.shopping.modules.admin.auth.constant.LoginHistoryAsyncConstant;
import com.toucan.shopping.modules.common.constant.TraceConstants;
import com.toucan.shopping.modules.common.context.TraceContext;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import jakarta.annotation.Nonnull;
import java.time.Duration;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class ThreadPoolTaskConfig {


    @Value("${spring.task.execution.pool.core-size}")
    private int corePoolSize;

    @Value("${spring.task.execution.pool.max-size}")
    private int maxPoolSize;

    @Value("${spring.task.execution.pool.queue-capacity}")
    private int queueCapacity;

    @Value("${spring.task.execution.pool.keep-alive}")
    private Duration keepAlive;

    @Value("${spring.task.execution.pool.name-prefix}")
    private String threadNamePrefix;


    @Bean(LoginHistoryAsyncConstant.DEFAULT_TASK_EXECUTE_NAME)
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds((int) keepAlive.getSeconds());
        executor.setThreadNamePrefix(threadNamePrefix);

        executor.setTaskDecorator(new ThreadContextDecorator());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); //如果线程池已满,由调用线程执行该任务
        executor.initialize();
        return executor;
    }
}


/**
 * 保存请求上下文、TraceId 到子线程中
 */
class ThreadContextDecorator implements TaskDecorator {
    @Override
    @Nonnull
    public Runnable decorate(@Nonnull Runnable runnable) {

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        String traceId = TraceContext.get();
        if (traceId == null) {
            traceId = MDC.get(TraceConstants.TRACE_ID_KEY);
        }
        String finalTraceId = traceId;
        return () -> {
            try {
                RequestContextHolder.setRequestAttributes(attributes);
                if (finalTraceId != null) {
                    TraceContext.set(finalTraceId);
                    MDC.put(TraceConstants.TRACE_ID_KEY, finalTraceId);
                }
                runnable.run();
            } finally {
                RequestContextHolder.resetRequestAttributes();
                TraceContext.remove();
                MDC.remove(TraceConstants.TRACE_ID_KEY);
            }
        };
    }
}
