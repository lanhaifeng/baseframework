package com.feng.baseframework.exception;

import com.feng.baseframework.constant.ResultEnum;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

public class AssertTest {
    @Test
    public void testAssert() {
        // 异常抽取为供给型函数式接口,可以定制异常
        // 单个对象判空
        long now = 1657725420000L;
        Assert.state(now < Instant.now().toEpochMilli(), () -> new RuntimeException("要求非空"));
        Assert.isTrue(null, Assert::isNull, () -> new RuntimeException("要求非空"));
        Assert.isTrue(1, 1, Assert::isEqual, () -> new BusinessException("要求相等"));
        Assert.isTrue(2f, 1f, Assert::isGt, () -> new BusinessException("要求大于"));

        Assert.isTrue(1L, 2l, Assert::isLe, () -> new BusinessException("要求xxx"));
        Assert.isTrue(1d, 2d, Assert::isLe, () -> new RuntimeException("要求xxx"));
        Assert.isTrue(new BigDecimal(1), new BigDecimal(2), Assert::isLe, () -> new RuntimeException("要求xxx"));
        Assert.isTrue(new Date(), new Date(), Assert::isLt, () -> new RedisException(ResultEnum.RESPONSE_RESULT_ERROR, null));
    }
}