package com.feng.baseframework.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

/**
 * baseframework
 * 2018/9/11 17:39
 * 异常工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class ExceptionUtil {

    /**
     * 2018/9/11 17:39
     * 将错误栈转为字符串
     *
     * @param t
     * @author lanhaifeng
     * @return java.lang.String
     */
    public static String getStackTrace(Throwable t) {
        return Optional.ofNullable(t).map((e)->{
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            return sw.toString();
        }).orElse("");
    }
}
