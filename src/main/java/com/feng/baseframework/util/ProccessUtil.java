package com.feng.baseframework.util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * baseframework
 * 2021/5/13 9:56
 * 进程工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class ProccessUtil {
	private static Logger logger = LoggerFactory.getLogger(ProccessUtil.class);

	static interface Kernel32 extends Library {

		public static Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);

		public int GetProcessId(Long hProcess);
	}


	public static int getPid(Process p) {
		Field f;

		if (Platform.isWindows()) {
			try {
				f = p.getClass().getDeclaredField("handle");
				f.setAccessible(true);
				int pid = Kernel32.INSTANCE.GetProcessId((Long) f.get(p));
				return pid;
			} catch (Exception ex) {
				logger.error("获取进程pid失败，错误{}", ExceptionUtils.getFullStackTrace(ex));
				throw new RuntimeException(ex.getMessage());
			}
		} else if (Platform.isLinux()) {
			try {
				f = p.getClass().getDeclaredField("pid");
				f.setAccessible(true);
				int pid = (Integer) f.get(p);
				return pid;
			} catch (Exception ex) {
				logger.error("获取进程pid失败，错误{}", ExceptionUtils.getFullStackTrace(ex));
				throw new RuntimeException(ex.getMessage());
			}
		}
		return 0;
	}
}
