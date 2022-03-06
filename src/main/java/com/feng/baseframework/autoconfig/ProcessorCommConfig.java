package com.feng.baseframework.autoconfig;

import com.feng.baseframework.signal.SignalStudy;
import org.springframework.context.annotation.Configuration;
import sun.misc.Signal;
import sun.misc.SignalHandler;

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

    /**
     *  SignalHandler.SIG_IGN:The default signal handler
     *  SignalHandler.SIG_DFL:Ignore the signal
     *  SignalStudy:自定义处理行为
     */
    @PostConstruct
    public void initSignal() {
        Signal.handle(new Signal("TERM"), new SignalStudy());
        Signal.handle(new Signal("USR2"), SignalHandler.SIG_IGN);
        Signal.handle(new Signal("CONT"), SignalHandler.SIG_DFL);
    }
}
