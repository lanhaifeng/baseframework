package com.feng.baseframework.io;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * baseframework
 * bio服务端
 * 2020/6/18 23:42
 *
 * @author lanhaifeng
 * @version 1.0
 */
@Slf4j
public class BioServer {

    public static void main(String[] args) {
        start(18080);
    }

    /**
     * 1.jps -l   查找pid
     * 2.jstack pid
     * @param port
     */
    private static void start(int port){
        try {
            ServerSocket socket = new ServerSocket(port);
            log.info("server start!");
            boolean status = true;
            while (status){
                log.info("server wait client connect!");
                Socket client = socket.accept();
                log.info("client connect!");
                buildThreadPool().execute(new BioClientHandler(client));
            }
        } catch (IOException e) {
            log.error("启动bio失败，错误：" + ExceptionUtils.getFullStackTrace(e));
        }
    }

    private static ExecutorService buildThreadPool(){
        return Executors.newFixedThreadPool(10);
    }
}
