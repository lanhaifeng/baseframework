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
        //在SignalStudy需要用注册相同信号返回的默认信号处理handler执行默认行为，若直接使用SignalHandler.SIG_DFL执行默认行为会报错
        SignalHandler defaultHandler = Signal.handle(new Signal("TERM"), SignalHandler.SIG_DFL);
        //相同的信号，注册不同的处理器，后注入的处理器会覆盖前边的处理器
        Signal.handle(new Signal("TERM"), new SignalStudy(defaultHandler));
        Signal.handle(new Signal("USR2"), SignalHandler.SIG_IGN);
        Signal.handle(new Signal("CONT"), SignalHandler.SIG_DFL);
    }
}
