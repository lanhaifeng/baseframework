package com.feng.baseframework.autoconfig;

import com.feng.baseframework.signal.SignalStudy;
import org.springframework.context.annotation.Configuration;
import sun.misc.Signal;

import javax.annotation.PostConstruct;

/**
 * 类名:SignalConfig <br/>
 * 描述:进程通信配置类 <br/>
 * 时间:2022/2/20 15:31 <br/>
 *
 * @author lanhaifeng
 * @version 1.0
 */
@Configuration
public class ProcessorCommConfig {

    @PostConstruct
    public void initSignal() {
        Signal.handle(new Signal("TERM"), new SignalStudy());
    }
}
