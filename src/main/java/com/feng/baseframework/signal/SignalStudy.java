package com.feng.baseframework.signal;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * 类名:SignalStudy <br/>
 * 描述:进程间通信：信号 <br/>
 * 查看信号：kill -l
 *
 * 时间:2022/2/20 15:27 <br/>
 *
 * @author lanhaifeng
 * @version 1.0
 */
public class SignalStudy implements SignalHandler {

    /**
     * 执行: kill -15
     * 输出: receive signal:TERM
     */
    @Override
    public void handle(Signal signal) {
        System.out.println("receive signal:" + signal.getName());
    }
}
