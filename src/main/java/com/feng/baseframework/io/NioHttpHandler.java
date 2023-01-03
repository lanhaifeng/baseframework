package com.feng.baseframework.io;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * baseframework
 * 2022/7/10 17:34
 * 描述一下类的用途
 *
 * @author lanhaifeng
 * @since
 **/
public class NioHttpHandler implements Runnable {

    private SelectionKey key;
    private int bufferSize = 1024;
    private int code = 200;

    public NioHttpHandler(SelectionKey key) {
        this.key = key;
    }

    /**
     * 接收连接处理
     *
     * @throws IOException
     */
    public void handleAccept() throws IOException {
        SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
        clientChannel.configureBlocking(false);     //线程不阻塞
        clientChannel.register(key.selector(), SelectionKey.OP_READ | SelectionKey.OP_WRITE, ByteBuffer.allocate(bufferSize));       //注册读写

    }

    public void handle() {
        try {
            HttpServletRequest req = new HttpServletRequest(key);                //封装Request
            HttpServletResponse rep = new HttpServletResponse(key);             //封装Response
            String name = req.getParameter("name");

            if (StringUtils.isNotBlank(name) && "1".equals(name)) {
                Thread.sleep(5000l);
            }
            rep.println("获取的参数name=" + name);
            rep.println("请求的url是:" + req.getUrl());


            rep.pushToClient(code);
            req.close();
            rep.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        handle();
        /*// 接收到连接请求时
        if (key.isAcceptable()) {
            try {
                handleAccept();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (key.isReadable() && key.isWritable())   //可读可写
        {
            handle();

        }*/
    }
}
