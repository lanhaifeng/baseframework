package com.feng.baseframework.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Date;

/**
 * baseframework
 * 2022/7/10 17:39
 * 描述一下类的用途
 *
 * @author lanhaifeng
 * @since
 **/
public class HttpServletResponse {

    //两个常量
    public static final String CRLF = "\r\n";
    public static final String BLANK = " ";
    private static final String localCharset = "UTF-8";
    //正文
    private StringBuilder content;
    //存储头信息
    private StringBuilder headInfo;
    //存储正文长度
    private int len = 0;
    private StringBuilder bu;
    private ByteBuffer buffer;
    private SocketChannel sc;

    public HttpServletResponse() {
        bu = new StringBuilder();
        headInfo = new StringBuilder();
        content = new StringBuilder();
        len = 0;
    }

    public HttpServletResponse(SelectionKey key) {
        this();
        sc = (SocketChannel) key.channel();
        buffer = (ByteBuffer) key.attachment();
        buffer.clear();
        buffer.flip();
    }

    /**
     * 构建正文
     */
    public HttpServletResponse print(String info) {
        content.append(info);
        len += info.getBytes().length;
        return this;
    }

    /**
     * 构建正文+回车
     */
    public HttpServletResponse println(String info) {
        content.append(info).append(CRLF);
        len += (info + CRLF).getBytes().length;
        return this;
    }

    /**
     * 构建响应头
     */
    private void createHeadInfo(int code) {
        //1)  HTTP协议版本、状态代码、描述
        headInfo.append("HTTP/1.1").append(BLANK).append(code).append(BLANK);
        switch (code) {
            case 200:
                headInfo.append("OK");
                break;
            case 404:
                headInfo.append("NOT FOUND");
                break;
            case 505:
                headInfo.append("SEVER ERROR");
                break;
        }
        headInfo.append(CRLF);
        //2)  响应头(Response Head)
        headInfo.append("Server:bjsxt Server/0.0.1").append(CRLF);
        headInfo.append("Date:").append(new Date()).append(CRLF);
        headInfo.append("Content-type:text/html;charset=" + localCharset).append(CRLF);
        //正文长度 ：字节长度
        headInfo.append("Content-Length:").append(len).append(CRLF);
        headInfo.append(CRLF); //分隔符
    }

    //推送到客户端
    void pushToClient(int code) throws IOException {
        if (null == headInfo) {
            code = 500;
        }
        createHeadInfo(code);
        //头信息+分割符
        bu.append(headInfo.toString());

        //正文
        bu.append(content.toString());
        buffer = ByteBuffer.wrap(bu.toString().getBytes(localCharset));
        if (sc.isOpen())
            sc.write(buffer);

    }

    /**
     * 关闭
     */
    void close() {
        try {
            if (sc.isOpen()) {
                sc.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
