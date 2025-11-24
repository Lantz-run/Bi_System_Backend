package com.yupi.springbootinit.config;

import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class RetryConfig {

    // 最大重试3次，指数退避策略
    @Bean
    public Retryer<Boolean> dataProcessRetryer() {
        return RetryerBuilder.<Boolean>newBuilder()
                .retryIfException() // 所有异常都重试
                .withWaitStrategy(WaitStrategies.exponentialWait(1000, 5, TimeUnit.MINUTES))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3)) // 最大重试 3 次
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.warn("Retry attempt: {}, Delay:{}ms",
                            attempt.getAttemptNumber(),
                            attempt.getDelaySinceFirstAttempt());
                    }
                })
                .build();
    }
}