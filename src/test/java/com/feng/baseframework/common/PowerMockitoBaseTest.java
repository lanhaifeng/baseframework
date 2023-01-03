package com.feng.baseframework.common;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**     
  *
  * @ProjectName:    svc-search-biz
  * @Description:    单元测试基础类，用于继承
  * @Author:         兰海峰
  * @CreateDate:     2018/4/28 17:20
  * @UpdateUser:     
  * @UpdateDate:     2018/4/28 17:20
  * @UpdateRemark:   
 */
@RunWith(PowerMockRunner.class)
@Category(FastTests.class)
public class PowerMockitoBaseTest {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Test
    public void powerMockit() {
    }

}
