package com.feng.baseframework.io;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * baseframework
 * 服务器客户端处理器
 * 2020/6/18 23:52
 *
 * @author lanhaifeng
 * @version 1.0
 */
@Slf4j
public class BioClientHandler implements Runnable {

    private Socket client;
    private Scanner scanner;
    private PrintStream out;
    private boolean status = true;

    public BioClientHandler(Socket client) {
        this.client = client;
        try {
            this.scanner = new Scanner(this.client.getInputStream());
            this.scanner.useDelimiter("\n");
            this.out = new PrintStream(this.client.getOutputStream());
        } catch (IOException e) {
            log.error("客户处理器构造失败：" + ExceptionUtils.getFullStackTrace(e));
        }
    }

    @Override
    public void run() {
        while (status) {
            if (this.scanner.hasNext()) {
                String var = this.scanner.next().trim();
                log.info("收到客户端消息：" + var);
                if ("byeBye".equals(var)) {
                    this.out.print("886");
                    status = false;
                } else {
                    out.println("【echo】" + var);
                }
            }
        }
        try {
            this.scanner.close();
            this.out.close();
            this.client.close();
        } catch (IOException e) {
            log.error("关闭流失败，错误：" + ExceptionUtils.getFullStackTrace(e));
        }
    }
}
