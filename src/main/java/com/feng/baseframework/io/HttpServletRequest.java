package com.feng.baseframework.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;

/**
 * baseframework
 * 2022/7/10 17:40
 * 描述一下类的用途
 *
 * @author lanhaifeng
 * @since
 **/
public class HttpServletRequest {

    private final String DEFULT_CODE = "UTF-8";
    private final String CRLF = "\r\n";      //回车换行
    private final String SPACE = " ";
    private SocketChannel sc;
    private ByteBuffer buffer;
    private String requestInfo;
    private String method;
    private String url;
    private Map<String, List<String>> parameterMapValues;

    public HttpServletRequest() {
        parameterMapValues = new HashMap<String, List<String>>();
    }

    public HttpServletRequest(SelectionKey key) throws IOException {
        this();
        sc = (SocketChannel) key.channel();
        buffer = (ByteBuffer) key.attachment();
        buffer.clear();
        if (sc.read(buffer) == -1) {
            sc.close();
        } else {
            buffer.flip();
            requestInfo = Charset.forName(DEFULT_CODE).newDecoder().decode(buffer).toString();
        }
        parseRequestInfo();
    }

    /**
     * 分析请求，主要是获取资源地址、封装专递的参数
     */
    private void parseRequestInfo() {
        String paramInfo = null;
        if (requestInfo == null || "".equals(requestInfo)) {
            return;
        }
        String[] requestMessage = requestInfo.split(CRLF);        //得到请求的消息
        String[] firstLine = requestMessage[0].split(SPACE);      //获取首行头文件
        this.method = firstLine[0];                  //获取提交方式
        String tempUrl = firstLine[1];               //获取资源地址
        if (method.equals("POST")) {
            this.url = tempUrl;
            paramInfo = requestMessage[requestMessage.length - 1];
        } else if (method.equals("GET")) {
            if (tempUrl.contains("?")) {       //如果有参数
                String params[] = tempUrl.split("\\?");
                this.url = params[0];
                paramInfo = params[1];//接收请求参数
            } else
                this.url = tempUrl;
        }

        if (paramInfo == null || "".equals(paramInfo)) {
            return;
        } else
            parseParams(paramInfo);
    }

    /**
     * 保存传递的参数
     *
     * @param paramInfo
     */
    private void parseParams(String paramInfo) {
        StringTokenizer token = new StringTokenizer(paramInfo, "&");
        while (token.hasMoreTokens()) {
            String keyValue = token.nextToken();
            String[] keyValues = keyValue.split("=");
            if (keyValues.length == 1) {
                keyValues = Arrays.copyOf(keyValues, 2);
                keyValues[1] = null;
            }

            String key = keyValues[0].trim();
            String value = null == keyValues[1] ? null : keyValues[1].trim();
            //转换成Map 分拣
            if (!parameterMapValues.containsKey(key)) {
                parameterMapValues.put(key, new ArrayList<String>());
            }

            List<String> values = parameterMapValues.get(key);
            values.add(value);
        }
    }

    /**
     * 根据页面的name 获取对应的多个值
     *
     * @param name 名
     */
    public String[] getParameterValues(String name) {
        List<String> values = null;
        if ((values = parameterMapValues.get(name)) == null) {
            return null;
        } else {
            return values.toArray(new String[]{});
        }
    }

    /**
     * 返回单个值
     *
     * @param name 名
     * @return
     */
    public String getParameter(String name) {
        String[] values = getParameterValues(name);
        if (null == values) {
            return null;
        }
        return values[0];
    }

    /**
     * 获取请求方法
     *
     * @return
     */
    public String getMethod() {
        return method;
    }

    /**
     * 获取url资源访问地址
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * 关闭连接
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
