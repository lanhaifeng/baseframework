package com.feng.baseframework.io;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * baseframework
 * nettty服务端
 * 2020/7/5 23:10
 *
 * @author lanhaifeng
 * @version 1.0
 */
public class NettyIoServer {

    private static Logger log = LoggerFactory.getLogger(NettyIoServer.class);

    public static void main(String[] args) {
        int port = 8080;
        String type = "tcp";
        if(args != null && args.length > 0){
            if(StringUtils.isNotBlank(args[0])){
                type = args[0];
            }
            if(args.length > 1 && StringUtils.isNotBlank(args[1]) && StringUtils.isNumeric(args[2])){
                int inputPort = Integer.valueOf(args[2]);
                if(inputPort >= 1024 && inputPort <= 65535){
                    port = inputPort;
                }
            }
        }
        if("tcp".equals(type)){
            startTcpServer(port);
        }
        if("http".equals(type)){
            startHttpServer(port);
        }
    }

    public static void startTcpServer(int port){
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap boot = new ServerBootstrap();
            boot.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });

            // start
            ChannelFuture future = boot.bind().sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("启动netty tcp server失败，错误：" + ExceptionUtils.getFullStackTrace(e));
        } finally {
            // shutdown
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void startHttpServer(int port){
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap boot = new ServerBootstrap();
            boot.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast("decoder", new HttpRequestDecoder())
                                    .addLast("encoder", new HttpResponseEncoder())
                                    .addLast("aggregator", new HttpObjectAggregator(512 * 1024))
                                    .addLast("handler", new NettyServerHttpHandler());
                        }
                    });

            // start
            ChannelFuture future = boot.bind().sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("启动netty http server失败，错误：" + ExceptionUtils.getFullStackTrace(e));
        } finally {
            // shutdown
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private final ByteBuf message;

    public NettyServerHandler() {
        message = Unpooled.buffer(256);
        message.writeBytes("hello netty client\n".getBytes(CharsetUtil.UTF_8));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        message.retain();
        ctx.writeAndFlush(message);
        System.out.println("refCnt1:" + message.refCnt());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("nettyServer receive:" + in.toString(CharsetUtil.UTF_8));

        System.out.println("refCnt2:" + message.refCnt());
        message.retain();
        message.clear();
        System.out.println("refCnt3:" + message.refCnt());
        message.writeBytes("I am is server\n".getBytes(CharsetUtil.UTF_8));
        ctx.writeAndFlush(message);
        System.out.println("refCnt4:" + message.refCnt());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

class NettyServerHttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer("hello netty client".getBytes()));

        HttpHeaders heads = response.headers();
        heads.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN + "; charset=UTF-8");
        heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes()); // 3
        heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        ctx.writeAndFlush(response);
    }
}