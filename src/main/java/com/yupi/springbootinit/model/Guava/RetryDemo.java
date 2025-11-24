package com.yupi.springbootinit.model.Guava;

import com.github.rholder.retry.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class RetryDemo {

    public static void main(String[] args) {
        // 定义重试任务
        Callable<Boolean> task = () -> {
            // 模拟可能失败的操作
            boolean result = doSomething();
            System.out.println("本次任务执行结果: " + result);
            return result;
        };

        // 配置重试策略
        Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
                .retryIfException() // 所有异常都重试
                .retryIfResult(result -> !result) // 返回false时重试
                .withStopStrategy(StopStrategies.stopAfterAttempt(3)) // 最多重试3次
                .withWaitStrategy(WaitStrategies.fixedWait(1000, TimeUnit.MILLISECONDS)) // 每次间隔1秒
                .build();

        try {
            retryer.call(task);
        } catch (RetryException | ExecutionException e) {
            System.out.println("最终任务执行失败，原因: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Boolean doSomething() {
        // 业务逻辑
        return Math.random() > 0.5; // 50% 概率失败
    }
}