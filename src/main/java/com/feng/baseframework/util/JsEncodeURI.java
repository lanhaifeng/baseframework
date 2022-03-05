package com.feng.baseframework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.UnsupportedEncodingException;

/**
 * 类名:JsEncodeURI <br/>
 * 描述:实现js的encodeURI方法 <br/>
 * 时间:2021/12/15 18:41 <br/>
 *
 * @author lanhaifeng
 * @version 1.0
 */
public class JsEncodeURI {
    private static final Logger logger = LoggerFactory.getLogger(JsEncodeURI.class);
    private static final ScriptEngineManager sem = new ScriptEngineManager();
    private static final ScriptEngine engine = sem.getEngineByExtension("js");

    //不参与转义的字符，与js的encodeURI方法保持一致
    //!#$&'()*+,-./:;=?@_~"
    public static final String DEFAULT_ESCAPE_CHARS = "!#$&'()*+,-./:;=?@_~\"";

    /**
     * 描述: 调用js方法encodeURI方法<br/>
     * 时间:19:05 2021/12/15 <br/>
     *
     * @param param
     * @author lanhaifeng
     * @return java.lang.String
     */
    public static final String encodeURIByJs(String param) {
        try {
            if(StringUtils.hasText(param)){
                return engine.eval("encodeURI('" +param+"')").toString();
            }
        } catch (ScriptException e) {
            logger.warn("url参数encodeURIByJs失败，错误{}", e);
        }
        return param;
    }

    /**
     * 描述: 使用java方法实现js的encodeURI方法<br/>
     * 时间:19:00 2021/12/15 <br/>
     *
     * @param param 待处理参数
     * @param charset 字符集
     * @author lanhaifeng
     * @return java.lang.String
     */
    public static final String encodeURIByJava(String param, String charset) {
        return encodeURIByJava(param, charset, DEFAULT_ESCAPE_CHARS);
    }

    /**
     * 描述: 使用java方法实现js的encodeURI方法<br/>
     * 时间:19:00 2021/12/15 <br/>
     *
     * @param param 待处理参数
     * @param charset 字符集
     * @param defaultEscapeChars 不转义的特殊字符窜
     * @author lanhaifeng
     * @return java.lang.String
     */
    public static final String encodeURIByJava(String param, String charset, String defaultEscapeChars) {
        if(!StringUtils.hasText(defaultEscapeChars)){
            defaultEscapeChars = DEFAULT_ESCAPE_CHARS;
        }
        StringBuffer target = new StringBuffer();
        try {
            String temp = new String(param.getBytes(charset), "ISO-8859-1");
            char[] chars = temp.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if ((chars[i] <= 'z' && chars[i] >= 'a') || (chars[i] <= 'Z' && chars[i] >= 'A')
                        || (chars[i] >= '0' && chars[i] <= '9') || defaultEscapeChars.contains(String.valueOf(chars[i]))){
                    target.append(chars[i]);
                } else {
                    target.append("%");
                    target.append(Integer.toHexString(chars[i]).toUpperCase());
                }
            }
            return target.toString();
        } catch (UnsupportedEncodingException e) {
            logger.warn("url参数encodeURIByJava失败，错误{}", e);
        }
        return param;
    }
}
