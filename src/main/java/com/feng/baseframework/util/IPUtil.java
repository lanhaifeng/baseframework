package com.feng.baseframework.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * baseframework
 * 2018/9/12 10:38
 * ip工具类
 *
 * @author lanhaifeng
 * @since
 **/
@Slf4j
public class IPUtil {
    /**
     * 获取当前网络ip
     * @param request
     * @return
     */
    public String getIpAddr(HttpServletRequest request){
        String ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    log.error("获取ip失败，错误：" + ExceptionUtils.getFullStackTrace(e));
                }
                ipAddress= inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     *  将字符串形式IP地址转换long类型
     * @param ip
     * @return
     */
    public static long getIpToLong(String ip) {
        ip = ip.trim();
        String[] ips = ip.split("\\.");
        long ip1 = Integer.parseInt(ips[0]);
        long ip2 = Integer.parseInt(ips[1]);
        long ip3 = Integer.parseInt(ips[2]);
        long ip4 = Integer.parseInt(ips[3]);
        long ip2long =1L* ip1 * 256 * 256 * 256 + ip2 * 256 * 256 + ip3 * 256 + ip4;
        return ip2long;
    }

    /**
     * 判断一个ip地址是否在某个ip段范围内
     * @param ip
     * @param startIP
     * @param endIP
     * @return
     */
    public static boolean ipExistsInRange(String ip, String startIP, String endIP) {
        return (getIpToLong(startIP)<=getIpToLong(ip)) && (getIpToLong(ip)<=getIpToLong(endIP));
    }

    /**
     * 2019/12/7 19:02
     * 通过socket获取本地ip
     *
     * @param
     * @author lanhaifeng
     * @return java.net.Inet4Address
     */
    public static InetAddress getIpBySocket() {
        try {
            final DatagramSocket socket = new DatagramSocket();
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            if (socket.getLocalAddress() instanceof Inet4Address || socket.getLocalAddress() instanceof Inet6Address) {
                return socket.getLocalAddress();
            }
        }catch (Exception e) {
            log.error("获取ip失败，错误：" + ExceptionUtils.getFullStackTrace(e));
        }
        return null;
    }

    /**
     * 2019/12/9 10:09
     * 获取ip地址
     *
     * @param  netType
     * @author lanhaifeng
     * @return java.util.List<java.net.InetAddress>
     */
    public static List<InetAddress> getLocalIpAddressFromNetworkInterface(NetType netType) throws SocketException {
        List<InetAddress> addresses = new ArrayList<>(1);
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        if (e == null) {
            return addresses;
        }
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            if (!isValidInterface(n)) {
                continue;
            }
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                if (netType == NetType.IPV4 && isValidInet4Address(i)) {
                    addresses.add((Inet4Address) i);
                }
                if (netType == NetType.IPV6 && isValidInet6Address(i)) {
                    addresses.add((Inet6Address) i);
                }
            }
        }
        return addresses;
    }

    /**
     * 过滤回环网卡、点对点网卡、非活动网卡、虚拟网卡并要求网卡名字是eth或ens开头
     *
     * @param ni 网卡
     * @return 如果满足要求则true，否则false
     */
    private static boolean isValidInterface(NetworkInterface ni) throws SocketException {
        return !ni.isLoopback() && !ni.isPointToPoint() && ni.isUp() && !ni.isVirtual()
                && (ni.getName().startsWith("eth") || ni.getName().startsWith("ens"));
    }

    /**
     * 判断是否是IPv4，并且内网地址并过滤回环地址.
     */
    private static boolean isValidInet4Address(InetAddress address) {
        return address instanceof Inet4Address && address.isSiteLocalAddress() && !address.isLoopbackAddress();
    }

    /**
     * 判断是否是IPv6，并且内网地址并过滤回环地址.
     */
    private static boolean isValidInet6Address(InetAddress address) {
        return address instanceof Inet6Address && address.isSiteLocalAddress() && !address.isLoopbackAddress();
    }

    /**
     * 2019/12/9 10:10
     * 获取ip地址
     *
     * @param  netType
     * @author lanhaifeng
     * @return java.net.Inet4Address
     */
    public static InetAddress getLocalIpAddress(NetType netType) throws SocketException {
        final List<InetAddress> ipByNi = getLocalIpAddressFromNetworkInterface(netType);
        if (ipByNi.isEmpty() || ipByNi.size() > 1) {
            final InetAddress ipBySocketOpt = getIpBySocket();
            if (Objects.nonNull(ipBySocketOpt)) {
                return ipBySocketOpt;
            } else {
                return ipByNi.isEmpty() ? null : ipByNi.get(0);
            }
        }
        return ipByNi.get(0);
    }

    /**
     * 2020/8/21 17:32
     * 获取ip
     *
     * @param
     * @author lanhaifeng
     * @return java.lang.String
     */
    public static String getLocalIp(){
       return getLocalIp4();
    }

    /**
     * 2020/8/21 17:32
     * 获取ip
     *
     * @param
     * @author lanhaifeng
     * @return java.lang.String
     */
    public static String getLocalIp4(){
        try{
            InetAddress inetAddress = getLocalIpAddress(NetType.IPV4);
            return inetAddress != null ? inetAddress.getHostAddress() : "";
        }catch(Exception e){
            log.error("获取本地ip失败，错误：" + ExceptionUtils.getFullStackTrace(e));
            return "";
        }
    }

    /**
     * 2020/8/21 17:32
     * 获取ip
     *
     * @param
     * @author lanhaifeng
     * @return java.lang.String
     */
    public static String getLocalIp6(){
        try{
            InetAddress inetAddress = getLocalIpAddress(NetType.IPV6);
            return inetAddress != null ? inetAddress.getHostAddress() : "";
        }catch(Exception e){
            log.error("获取本地ip失败，错误：" + ExceptionUtils.getFullStackTrace(e));
            return "";
        }
    }

    public static void main(String[] args) throws SocketException, UnknownHostException {
        System.out.println(getIpBySocket().getHostAddress());
        System.out.println("a" + null);
        System.out.println(getLocalIpAddress(NetType.IPV4).getHostAddress());
        System.out.println(getLocalIpAddress(NetType.IPV6).getHostAddress());
        InetAddress localhost = InetAddress.getLocalHost();
        System.out.println(localhost.getHostAddress());
    }

    public  static enum NetType{
        IPV4,IPV6
    }
}
