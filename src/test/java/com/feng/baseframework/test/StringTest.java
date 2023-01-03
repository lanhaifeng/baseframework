package com.feng.baseframework.test;

import com.alibaba.fastjson.JSON;
import com.feng.baseframework.util.IPUtil;
import com.feng.baseframework.util.JacksonUtil;
import com.feng.baseframework.util.StringUtil;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import sun.net.util.IPAddressUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * baseframework
 * 2018/9/5 11:15
 * 测试类
 *
 * @author lanhaifeng
 * @since
 **/
public class StringTest {

    @Test
    public void arraysTest(){
        List<String> list = new ArrayList<>();
        list.add("abc");
        list.add("hjk");
        list.add("1231");

        System.out.println(StringUtils.join(list,","));
        System.out.println(Arrays.toString(list.toArray(new String[]{})));

        System.out.println("ROLE_ADMIN,ROLE_AUDIT,ROLE_SECURITY,ROLE_SUPER_ADMIN".length());
        String ip = "192.168.230.23";
        String[] ips = ip.split("\\.");
        System.out.println(ips.length);
        System.out.println(ips[3]);
        StringBuffer jsonStr = new StringBuffer("[qweqw],");
        jsonStr.substring(0,jsonStr.length()-1);
        System.out.println(jsonStr.toString());//[qweqw],
        System.out.println(jsonStr.substring(0,jsonStr.length()-1));//[qweqw]

        String startIp = "192.168.60.1";
        String endIp = "192.168.60.255";
        String ipStr = "192.168.60.14";
        System.out.println(IPUtil.ipExistsInRange(ipStr,startIp,endIp));
    }

    @Test
    public void testJson() throws IOException {
        System.out.println(JacksonUtil.getJsonFromFile("/json/data.json"));
    }

    @Test
    public void testStringSize(){
        String auditId = "13060358572871200550";
        System.out.println(auditId.getBytes().length*10000/1024);

    }

    @Test
    public void testDecimalFormat(){
        String auditId = "13060358572871200550";
        System.out.println(auditId.getBytes().length*10000/1024);

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        System.out.println(decimalFormat.format(1*0.101));
        System.out.println(decimalFormat.format(1*1110.111));

        String a = "1";
        Integer b = 1;
        System.out.println(b.toString().equals(a));

        Integer c = 1;
        System.out.println(b.equals(c));

        String test = "block_action ";
        System.out.println(test.split(" ").length);
    }

    @Test
    public void jsonStr(){
        List<String> datas = Arrays.asList(new String[]{"\"acb\"","'a'","'cb'"});
        System.out.println(toJSONArrayString(datas));
        System.out.println(JSON.toJSONString(datas));
    }

    public static String toJSONArrayString(List<String> objects) {
        if(objects == null || objects.size() < 1){
            return null;
        }
        StringBuilder jsonStr = new StringBuilder("[");
        Object obj = null;
        int count = objects.size();
        for(int i=0; i<count; i++){
            jsonStr.append("\"").append(objects.get(i).replaceAll("\"", "\\\\\"")).append("\"").append(",");
        }
        if(jsonStr.length() == 1){
            return "";
        }else {
            return jsonStr.substring(0,jsonStr.length()-1)+"]";
        }
    }

    @Test
    public void hexStrTest() {
        String hexString = "E6938DE4BD9CE697A5E5BF97";
        System.out.println(new String(StringUtil.parseHexStr2Byte(hexString)));
    }

    @Test
    public void ipv6Test() {
        Assert.state(IPAddressUtil.isIPv6LiteralAddress("2001::192:168:230:206"));
        Assert.state(IPAddressUtil.isIPv6LiteralAddress("2002::192:168:230:211"));
        Assert.state(IPAddressUtil.isIPv6LiteralAddress("2001:4860:4860::8888"));
        Assert.state(IPAddressUtil.isIPv6LiteralAddress("2001:4860:4860::8888%mc1"));
        Assert.state(IPAddressUtil.isIPv6LiteralAddress("2001:4860:4860::8888%mc1"));
        Assert.state(IPAddressUtil.isIPv6LiteralAddress("0001:0002:0003:0004:0005:ffff:111.112.113.114%mc"));
    }

    @Test
    public void testCompare() {
        String time1 = "02:38:01";
        String time2 = "18:59:02";
        Assert.state(time1.compareTo(time2) < 0);

        time1 = "02:38:01";
        time2 = "02:38:01";
        Assert.state(time1.compareTo(time2) == 0);

        time1 = "02:52:02";
        time2 = "02:38:01";
        Assert.state(time1.compareTo(time2) > 0);

        time1 = "2021-01-15 02:59:02";
        time2 = "2020-01-15 02:38:01";
        Assert.state(time1.compareTo(time2) > 0);
    }
}
