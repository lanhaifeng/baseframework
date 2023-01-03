package com.feng.baseframework.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * baseframework
 * 2019/5/28 15:41
 * 网络工具类
 *
 * @author lanhaifeng
 * @since
 **/
@Slf4j
public class NetUtil {

	public static boolean isHostConnectable(String host, int port) {
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(host, port));
		} catch (IOException e) {
			log.error("socket连接失败：" + ExceptionUtils.getFullStackTrace(e));
			return false;
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				log.error("关闭socket失败：" + ExceptionUtils.getFullStackTrace(e));
			}
		}
		return true;
	}

	public static boolean isHostReachable(String host, Integer timeOut) {
		try {
			return InetAddress.getByName(host).isReachable(timeOut);
		} catch (UnknownHostException e) {
			log.error("连接主机失败：" + ExceptionUtils.getFullStackTrace(e));
		} catch (IOException e) {
			log.error("连接主机失败：" + ExceptionUtils.getFullStackTrace(e));
		}
		return false;
	}
}
