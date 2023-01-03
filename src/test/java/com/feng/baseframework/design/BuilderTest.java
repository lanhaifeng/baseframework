package com.feng.baseframework.design;

import com.feng.baseframework.exception.Assert;
import com.feng.baseframework.exception.BusinessException;
import com.feng.baseframework.model.Version;
import org.junit.Test;

public class BuilderTest {
    @Test
    public void testBuilder1() {
        Version version = Builder.of(Version::new)
                .with(Version::setVersion, "1.10")
                .with(Version::setBuildVersion, "2.3.0")
                .with(Version::setBranch, "1.0")
                .with(Version::setName, "baseframework")
                .with(Version::setCommit, "adfasdf")
                .with(Version::setBuildTime, "2022-08-12 13:11:01")
                .build();

        System.out.println(version.toString());
        String result = "Version{version='1.10', name='baseframework', commit='adfasdf', buildTime='2022-08-12 13:11:01', branch='1.0', buildVersion='2.3.0'}";

        Assert.state(result.equals(version.toString()), BusinessException::new, "使用Builder构建Version对象失败");
    }

    @Test
    public void testBuilder2() {
        Version version = Builder.of(new Version())
                .with(Version::setVersion, "1.10")
                .with(Version::setBuildVersion, "2.3.0")
                .with(Version::setBranch, "1.0")
                .with(Version::setName, "baseframework")
                .with(Version::setCommit, "adfasdf")
                .with(Version::setBuildTime, "2022-08-12 13:11:01")
                .build();

        System.out.println(version.toString());
        String result = "Version{version='1.10', name='baseframework', commit='adfasdf', buildTime='2022-08-12 13:11:01', branch='1.0', buildVersion='2.3.0'}";

        Assert.state(result.equals(version.toString()), BusinessException::new, "使用Builder构建Version对象失败");
    }
}