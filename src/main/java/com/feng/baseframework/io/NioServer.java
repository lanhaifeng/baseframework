package com.feng.baseframework.io;

import com.feng.baseframework.util.IOUtils;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;

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
 * 多路复用io服务端
 * 2020/6/20 23:02
 *
 * @author lanhaifeng
 * @version 1.0
 */
@Slf4j
public class NioServer {

    private Selector selector;

    /**
     * 获得一个ServerSocket通道，并对该通道做一些初始化的工作
     * @param port  绑定的端口号
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
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void listen() throws IOException {
        log.info("服务端启动成功！");
        // 轮询访问selector
        while (true) {
            //当注册的事件到达时，方法返回；否则,该方法会一直阻塞
            selector.select();

            // 获得selector中选中的项的迭代器，选中的项为注册的事件
            Iterator ite = this.selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = (SelectionKey) ite.next();

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

                    //在这里可以给客户端发送信息哦
                    String msg = "向客户端发送了一条信息";
                    channel.write(ByteBuffer.wrap(msg.getBytes("UTF-8")));

                    //在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。
                    channel.register(this.selector, SelectionKey.OP_READ);

                    // 获得了可读的事件
                } else if (key.isValid() && key.isReadable()) {
                    readByte(key);
                }
            }
        }
    }

    /**
     * 处理读取客户端发来的信息 的事件
     * @param key
     * @throws IOException
     */
    public void read(SelectionKey key) throws IOException {
        // 服务器可读取消息:得到事件发生的Socket通道
        SocketChannel channel = (SocketChannel) key.channel();

        // 创建读取的缓冲区
        int byteSize = 8;
        ByteBuffer buffer = ByteBuffer.allocate(byteSize);
        ByteOutputStream bos = new ByteOutputStream(byteSize);
        int sumSize = 0;
        int numBytesRead;
        byte[] bytes;
        try {
            while ((numBytesRead = channel.read(buffer)) > 0) {
                // 转到最开始
                buffer.flip();
                bytes = buffer.array();
                sumSize += numBytesRead;
                bos.write(bytes);
                // 复位，清空
                buffer.clear();
            }
        } catch (Exception e) {
            log.error("读取数据异常退出：" + ExceptionUtils.getFullStackTrace(e));
            key.cancel();
            channel.socket().close();
            channel.close();
        }
        byte[] datas = bos.getBytes();

        //log.info(Arrays.toString(datas));
        String msg = new String(datas, 0, sumSize, "UTF-8");
        log.info("服务端收到信息：" + msg);

        //在这里可以向客户端发送消息
        /*msg = "收到了客户端发送的消息";
        ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes("UTF-8"));
        channel.write(outBuffer);*/
    }

    public void readByte(SelectionKey key) throws IOException {
        // 服务器可读取消息:得到事件发生的Socket通道
        SocketChannel channel = (SocketChannel) key.channel();

        // 创建读取的缓冲区
        int byteSize = 8;
        ByteBuffer buffer = ByteBuffer.allocate(byteSize);
        int sumIndex = 0;
        int numBytesRead;
        byte[] allData = new byte[byteSize];
        byte byteValue;
        try {
            while ((numBytesRead = channel.read(buffer)) > 0) {
                // 转到最开始
                buffer.flip();
                if (allData.length <= sumIndex + numBytesRead - 1) {
                    allData = IOUtils.ensureCapacity(allData, sumIndex, sumIndex + byteSize);
                }
                while (buffer.hasRemaining()) {
                    byteValue = buffer.get();
                    if (((char) byteValue) == '\n') {
                        String msg = new String(allData, 0, sumIndex, "UTF-8");
                        log.info("服务端收到信息：" + msg);
                        sumIndex = 0;
                    } else {
                        allData[sumIndex] = byteValue;
                        sumIndex++;
                    }
                }
                // 复位，清空
                buffer.clear();
            }
        } catch (Exception e) {
            log.error("读取数据异常退出：" + ExceptionUtils.getFullStackTrace(e));
            key.cancel();
            channel.socket().close();
            channel.close();
        }
    }

    /**
     * 启动服务端测试
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        NioServer server = new NioServer();
        server.initServer(8000);
        server.listen();
    }
}
