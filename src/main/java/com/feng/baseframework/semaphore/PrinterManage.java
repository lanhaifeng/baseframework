package com.feng.baseframework.semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotEmpty;
import java.util.concurrent.Semaphore;

/**
 * 类名:PrinterManage <br/>
 * 描述:打印机-模拟多打印机 <br/>
 * 信号量-用于控制共享资源的访问
 *
 * 时间:2022/3/20 16:35 <br/>
 *
 * @author lanhaifeng
 * @version 1.0
 */
public class PrinterManage {

    private Logger logger = LoggerFactory.getLogger(PrinterManage.class);

    //窗口
    private final Semaphore printer;

    /**
     * 初始化一个数量为size打印机个数
     * @param size
     */
    public PrinterManage(int size) {
        printer = new Semaphore(size, true);
    }

    /**
     * 使用打印机
     * @param user          用户
     * @param content       打印内容
     */
    public void useWindow(@NotEmpty String user, @NotEmpty String content) {
        try {
            printer.acquire();
            System.out.printf("user %s is use printer, print %s%n", user, content);
        } catch (InterruptedException e) {
            logger.error("获取打印机打印失败", e);
        } finally {
            printer.release();
        }
    }

}
