package com.example.util;

import java.util.concurrent.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @ClassName ThreadPoolUtils
 * @Author sheng.lin
 * @Date 2025/7/14
 * @Version 1.0
 * @修改记录
 **//*

*/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */
public class ThreadPoolUtils {
    public static ThreadPoolTaskExecutor createThreadPoolTaskExecutor(
            String threadNamePrefix,
            int corePoolSize,
            int maxPoolSize,
            int queueCapacity) {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);           // 核心线程数
        executor.setMaxPoolSize(maxPoolSize);             // 最大线程数
        executor.setQueueCapacity(queueCapacity);         // 队列容量
        executor.setThreadNamePrefix(threadNamePrefix);   // 线程名前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 拒绝策略

        // 设置线程池关闭时的等待策略
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();
        return executor;
    }
}


