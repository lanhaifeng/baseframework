package com.feng.baseframework.semaphore;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 类名:PrinterUser <br/>
 * 描述:使用打印机用户 <br/>
 * 时间:2022/3/20 20:26 <br/>
 *
 * @author lanhaifeng
 * @version 1.0
 */
public class PrinterUser implements Runnable {

    private String userName;
    private String content;
    private PrinterManage printerManage;

    public PrinterUser(@NotEmpty String userName, @NotEmpty String content, @NotNull PrinterManage printerManage) {
        this.userName = userName;
        this.content = content;
        this.printerManage = printerManage;
    }

    @Override
    public void run() {
        printerManage.useWindow(userName, content);
    }
}
