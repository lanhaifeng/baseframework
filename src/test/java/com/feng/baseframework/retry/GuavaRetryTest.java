package com.feng.baseframework.retry;

import com.github.rholder.retry.*;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

class GuavaRetryTest {

    Random randomInt = new Random();

    @Test
    void testRetry() {
        Retryer<String> retryer = RetryerBuilder.<String>newBuilder()
                //无论出现什么异常，都进行重试
                .retryIfException()
                //返回结果为 error时，进行重试
                .retryIfResult(result -> Objects.equals(result, "error"))
                //重试等待策略：等待 2s 后再进行重试
                .withWaitStrategy(WaitStrategies.fixedWait(2, TimeUnit.SECONDS))
                //重试停止策略：重试达到 3 次
                .withStopStrategy(StopStrategies.stopAfterAttempt(10))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        System.out.println("RetryListener: 第" + attempt.getAttemptNumber() + "次调用");
                    }
                })
                .build();
        try {
            retryer.call(() -> testGuavaRetry());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String testGuavaRetry() {
        return randomInt.nextInt() %2 == 0 ? "error" : "success";
    }
}
