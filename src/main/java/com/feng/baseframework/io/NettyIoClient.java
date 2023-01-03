package com.feng.baseframework.io;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * baseframework
 * netty client
 * 2020/7/5 23:51
 *
 * @author lanhaifeng
 * @version 1.0
 */
public class NettyIoClient {

    private static Logger log = LoggerFactory.getLogger(NettyIoClient.class);

    public static void main(String[] args) {
        int port = 8080;
        String ip = "localhost";
        if(args != null && args.length > 0){
            if(StringUtils.isNotBlank(args[0])){
                ip = args[0];
            }
            if(args.length > 1 && StringUtils.isNotBlank(args[1]) && StringUtils.isNumeric(args[2])){
                int inputPort = Integer.valueOf(args[2]);
                if(inputPort >= 1024 && inputPort <= 65535){
                    port = inputPort;
                }
            }
        }
        startClient(ip, port);
    }

    public static void startClient(String ip, int port){
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            //p.addLast(new LoggingHandler(LogLevel.ERROR));
                            p.addLast(new NettyClientHandler());
                        }
                    });

            // Start the client.
            ChannelFuture f = bootstrap.connect(ip, port).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("启动netty client失败，错误：" + ExceptionUtils.getFullStackTrace(e));
        } finally {
            group.shutdownGracefully();
        }
    }
}

class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(NettyClientHandler.class);
    private final ByteBuf message;

    public NettyClientHandler() {
        message = Unpooled.buffer(256);
        message.writeBytes("hello netty server\n".getBytes(CharsetUtil.UTF_8));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("nettyClient receive:"+ ((ByteBuf) msg).toString(CharsetUtil.UTF_8));

        ByteBuf out = Unpooled.buffer(256);
        out.writeBytes("I am is client\n".getBytes(CharsetUtil.UTF_8));
        ctx.writeAndFlush(out);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("启动netty client hander sleep失败，错误：" + ExceptionUtils.getFullStackTrace(e));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
