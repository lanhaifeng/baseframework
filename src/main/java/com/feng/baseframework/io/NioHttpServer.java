package com.feng.baseframework.io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * baseframework
 * 2022/7/13 22:13
 * nio模拟http server端逻辑
 *
 * @author lanhaifeng
 * @since
 **/
@Slf4j
public class NioHttpServer {
    private Selector selector;

    /**
     * 获得一个ServerSocket通道，并对该通道做一些初始化的工作
     *
     * @param port 绑定的端口号
     * @throws IOException
     */
    public void initServer(int port) throws IOException {
        // 获得一个ServerSocket通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();

        // 设置通道为非阻塞
        serverChannel.configureBlocking(false);

        // 将该通道对应的ServerSocket绑定到port端口
        serverChannel.socket().bind(new InetSocketAddress(port));

        // 获得一个通道管理器
        this.selector = Selector.open();

        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，
        //当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞。
        //validOps说明当前通道支持的事件
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }


    /**
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
     *
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void listen() throws IOException {
        log.info("服务端启动成功！");
        // 轮询访问selector
        while (true) {
            //当注册的事件到达时，方法返回；否则,该方法会一直阻塞
            if (selector.select() > 0) {
                // 获得selector中选中的项的迭代器，选中的项为注册的事件
                Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
                while (ite.hasNext()) {
                    SelectionKey key = ite.next();
                    // 删除已选的key,以防重复处理
                    ite.remove();
                    // 客户端请求连接事件
                    if (key.isValid() && key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key
                                .channel();

                        // 获得和客户端连接的通道
                        SocketChannel channel = server.accept();

                        // 设置成非阻塞
                        channel.configureBlocking(false);

                        //在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。
                        channel.register(this.selector, SelectionKey.OP_READ);
                        channel.register(key.selector(), SelectionKey.OP_READ | SelectionKey.OP_WRITE, ByteBuffer.allocate(1024));       //注册读写


                        // 获得了可读的事件
                    } else if (key.isValid() && key.isReadable() && key.isWritable()) {
                        //取消读事件的监控
                        key.cancel();
                        /**
                         * 处理读写事件
                         * 1.这里使用的是多线程，可以改用线程池
                         * 2.也可以使用单线程处理，用于处理类似聊天之类业务场景
                         */
                        new Thread(new NioHttpHandler(key)).start();
                    }
                }
            }
        }
    }

    /**
     * 启动服务端测试
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        NioHttpServer server = new NioHttpServer();
        server.initServer(8000);
        server.listen();
    }
}
