package com.feng.baseframework.io;

import java.io.ByteArrayOutputStream;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * baseframework
 * 多路复用io客户端
 * 2020/6/20 23:03
 *
 * @author lanhaifeng
 * @version 1.0
 */
@Slf4j
public class NioClient {

    private Selector selector;

    /**
     * 获得一个Socket通道，并对该通道做一些初始化的工作
     *
     * @param ip   连接的服务器的ip
     * @param port 连接的服务器的端口号
     * @throws IOException
     */
    public void initClient(String ip, int port) throws IOException {
        // 获得一个Socket通道
        SocketChannel channel = SocketChannel.open();

        // 设置通道为非阻塞
        channel.configureBlocking(false);

        // 获得一个通道管理器
        this.selector = Selector.open();

        // 客户端连接服务器,其实方法执行并没有实现连接，需要在listen（）方法中调
        //用channel.finishConnect();才能完成连接
        channel.connect(new InetSocketAddress(ip, port));

        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_CONNECT事件。
        //validOps说明当前通道支持的事件
        channel.register(selector, SelectionKey.OP_CONNECT);
    }


    /**
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
     *
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void listen() throws IOException {
        // 轮询访问selector
        while (true) {
            selector.select();

            // 获得selector中选中的项的迭代器
            Iterator ite = this.selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = (SelectionKey) ite.next();

                // 删除已选的key,以防重复处理
                ite.remove();

                // 连接事件发生
                if (key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) key.channel();

                    // 如果正在连接，则完成连接
                    if (channel.isConnectionPending()) {
                        channel.finishConnect();
                    }

                    // 设置成非阻塞
                    channel.configureBlocking(false);

                    //在这里可以给服务端发送信息哦
                    channel.write(ByteBuffer.wrap("向服务端发送了一条信息".getBytes("UTF-8")));

                    //在和服务端连接成功之后，为了可以接收到服务端的信息，需要给通道设置读的权限。
                    //使用‘位或’|可以监听多个事件
                    channel.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

                    // 获得了可读的事件
                } else if (key.isReadable()) {
                    read(key);
                } else if (key.isWritable()) {
                    String message = "hello server\n";
                    SocketChannel channel = (SocketChannel) key.channel();
                    channel.write(ByteBuffer.wrap(message.getBytes("UTF-8")));
                }
            }
        }
    }

    /**
     * 处理读取服务端发来的信息 的事件
     *
     * @param key
     * @throws IOException
     */
    public void read(SelectionKey key) throws IOException {
        // 客户端可读取消息:得到事件发生的Socket通道
        SocketChannel channel = (SocketChannel) key.channel();

        // 创建读取的缓冲区
        int byteSize = 8;
        ByteBuffer buffer = ByteBuffer.allocate(byteSize);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(byteSize);
        int sumSize = 0;
        int numBytesRead;
        byte[] allData = new byte[byteSize];
        //这里循环读取数据，会读取到服务端多次的数据
        //如果不循环读取，不知道一次发送数据的长度，可能读取部分数据，转字符串会乱码
        while ((numBytesRead = channel.read(buffer)) > 0) {
            // 转到最开始
            buffer.flip();

            buffer.get(allData, 0, numBytesRead);

            sumSize += numBytesRead;
            bos.write(allData);

            // 复位，清空
            buffer.clear();
        }
        byte[] datas = bos.toByteArray();

        //log.info(Arrays.toString(datas));
        String msg = new String(datas, 0, sumSize, "UTF-8");
        log.info("客户端收到信息：" + msg);

        //在这里可以向服务端发送消息
        /*msg = "收到了服务端发送的消息";
        ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes("UTF-8"));
        channel.write(outBuffer);*/
    }

    /**
     * 启动客户端测试
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        NioClient client = new NioClient();
        client.initClient("localhost", 8000);
        client.listen();
       /* byte[] datas = "向服务端发送了一条信息".getBytes("UTF-8");
        System.out.println(Arrays.toString(datas));*/
    }
}
