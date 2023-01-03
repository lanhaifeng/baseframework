package com.feng.baseframework.jdk.net;

import com.feng.baseframework.util.JsEncodeURI;
import org.junit.Test;
import org.unbescape.uri.UriEscape;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.UnsupportedEncodingException;

/**
 * 类名:testURI <br/>
 * 描述:测试uri <br/>
 * 时间:2021/12/15 17:15 <br/>
 *
 * @author lanhaifeng
 * @version 1.0
 */
public class testURI {

    @Test
    public void testURI() throws ScriptException, UnsupportedEncodingException {
        String param = "batteryLevel=40&batteryStatus=0&bluetoothEnabled=1&brand=iPhone&brightness=0.6638098955154419" +
                "&currentWifi=[940c46341794a212cbe8a57411dd092f-5c:c9:99:0f:36:87]&custID=Cmbp0001&devicePixelRatio=3&" +
                "language=zh_CN&locationEnabled=1&model=iPhoneX(GSM+CDMA)<iPhone10,3>&networkType=wifi&osVersion=iOS13.3" +
                "&platform=WMP&screenHeight=812&screenWidth=375&sdkVersion=2.1.4&statusBarHeight=44&systemPlatform=ios" +
                "&userInfo=e5dca9d4a427ebf3e31cdbbe25f1f260&version=8.0.10&wifiEnable=1&windowHeight=730&windowWidth=375" +
                "&wxSmartID=10e5854737345a4a0cdd7df412b35c9f&algID=BHEoRttbAX&hashCode=H0OGGdEt9lAz4p1ba7n6Hb56Pvg0pcH8bd5GTrAKtwc";

        System.out.println(UriEscape.escapeUriQueryParam(param, "UTF-8"));

        ScriptEngineManager sem = new ScriptEngineManager();

        ScriptEngine engine = sem.getEngineByExtension("js");
        Object res = engine.eval("encodeURI('" +param+"')");

        System.out.println(res);

        System.out.println(JsEncodeURI.encodeURIByJava(param, "UTF-8"));

        System.out.println(JsEncodeURI.encodeURIByJava("!#$&'()*+,-./:;=?@_~\"0-9a-zA-Z哈", "UTF-8"));

    }

    //"!#$&'()*+,-./:;=?@_~0-9a-zA-Z"
    public static String encode(String str) throws UnsupportedEncodingException {
        String isoStr = new String(str.getBytes("UTF-8"), "ISO-8859-1");
        char[] chars = isoStr.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if ((chars[i] <= 'z' && chars[i] >= 'a') || (chars[i] >= '0' && chars[i] <= '9')
                    || (chars[i] <= 'Z' && chars[i] >= 'A') || chars[i] == '-'
                    || chars[i] == '_' || chars[i] == '.' || chars[i] == '!'
                    || chars[i] == '~' || chars[i] == '*' || chars[i] == '\''
                    || chars[i] == '(' || chars[i] == ')' || chars[i] == ';'
                    || chars[i] == '/' || chars[i] == '?' || chars[i] == ':'
                    || chars[i] == '@' || chars[i] == '&' || chars[i] == '='
                    || chars[i] == '+' || chars[i] == '$' || chars[i] == ','
                    || chars[i] == '#') {
                sb.append(chars[i]);
            } else {
                sb.append("%");
                sb.append(Integer.toHexString(chars[i]).toUpperCase());
            }
        }
        return sb.toString();
    }
    public static String escape(String src) {
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);
        for (i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j)
                    || Character.isUpperCase(j))
                tmp.append(j);
            else if (j < 256) {
                tmp.append("%");
                if (j < 16)
                    tmp.append("0");
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }
}
