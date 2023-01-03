package com.feng.baseframework.jdk8.concurrent;

import com.feng.baseframework.util.JacksonUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * baseframework
 * 2018/11/2 15:30
 * 队列测试
 *
 * @author lanhaifeng
 * @since
 **/
public class QueueTest {

	@Test
	public void testLinkedBlockingQueue(){
		LinkedBlockingQueue<String> logonStrs	= new LinkedBlockingQueue<String>(5);
		String audit = "1234";
		logonStrs.offer(audit);
		logonStrs.offer(audit);
		logonStrs.offer(audit);
		System.out.println(logonStrs.size());
		System.out.println(logonStrs.poll());
		System.out.println(logonStrs.poll());
		System.out.println(logonStrs.size());

		String json = "{\n" +
				"  \"WHERE\": \"WHERE\",\n" +
				"  \"!\": \"！\",\n" +
				"  \"REPLACE\": \"REPLACE\",\n" +
				"  \"DECODE\": \"DECODE\",\n" +
				"  \"SELECT UID, HBUID, AGENT_NAME, VERSION, RECYCLE, STATUS, REGITER_TIME, START_TIME, COMMOD_ID, REMARK, INSTALL_PATH, CONFIG_PATH, START_PATH, IS_START, NOTIFY_URL FROM MC_PROCESS WHERE UID = F2615469DC7711E8B00600505695F654\": \"获取表MC_PROCESS中主键为$(UID,0.0)的记录\",\n" +
				"  \"NVL\": \"NVL\",\n" +
				"  \"SELECT UID, MACHINE_NAME, IP, MAC, OS_NAME, CREATE_TIME, COMMOD_ID, MD5KEY, SERVICE_HOST, SERVICE_PORT, ALIVE_RECYCLE FROM MC_HB WHERE UID = 77F446C4D01011E88D5800505695F654\": \"测试翻译1$(UID,0.0)\",\n" +
				"  \"SELECT ID, MIRROR_NAME, RUN_STATUS, HOST, OS_NAME, START_TIME, VERSION, AGENT_DATA, PROTECT_OBJECT, CERT_STATUS, CREATE_TIME, TOKEN, CERT_KEY, REMARK, AFW_UID FROM MC$MIRROR_INFO WHERE AFW_UID = F2615469DC7711E8B00600505695F654\": \"测试$(AFW_UID,0.0)\",\n" +
				"  \"ROUND\": \"ROUND\",\n" +
				"  \"HAVING\": \"HAVING\",\n" +
				"  \"COUNT\": \"COUNT\",\n" +
				"  \"SUM\": \"SUM\",\n" +
				"  \"DISTINCT\": \"DISTINCT\",\n" +
				"  \">\": \">\",\n" +
				"  \"=\": \"=\",\n" +
				"  \"<\": \"<\"\n" +
				"}";
		System.out.println(JacksonUtil.json2map(json));
	}



	@Test
	public void testDrainTo() throws InterruptedException {
		Integer count = 100000;
		LinkedBlockingQueue<String>	auditStrs = new LinkedBlockingQueue<String>(count);
		String str = "{\"linkSessionID\":7651832137224081835,\"SQLSessionID\":\"\",\"SQLAccessID\":\"\",\"token\":\"\",\"eUser\":\"\",\"eUserID\":\"\",\"eUserName\":\"\",\"certName\":\"\",\"dbWorkMode\":5,\"content\":{\"dbdwAccessControl\":{\"ruleName\":\"JAVA\",\"cmdType\":\"\",\"actionLevel\":\"0\",\"auditLevel\":\"1\",\"riskLevel\":\"0\",\"objectOwner\":\"\",\"objectName\":\"\",\"objectType\":\"\"}}}";
		for (int i = 0; i < count; i++) {
			auditStrs.put(str);
		}

		Long startTime = new Date().getTime();
		List<String> datas = new ArrayList<>();
		while (auditStrs.remainingCapacity() < count){
			auditStrs.drainTo(datas, 1000);
		}
		Long endTime = new Date().getTime();

		System.out.println(endTime - startTime);
	}

	@Test
	public void testPoll() throws InterruptedException {
		Integer count = 100000;
		LinkedBlockingQueue<String>	auditStrs = new LinkedBlockingQueue<String>(count);
		String str = "{\"linkSessionID\":7651832137224081835,\"SQLSessionID\":\"\",\"SQLAccessID\":\"\",\"token\":\"\",\"eUser\":\"\",\"eUserID\":\"\",\"eUserName\":\"\",\"certName\":\"\",\"dbWorkMode\":5,\"content\":{\"dbdwAccessControl\":{\"ruleName\":\"JAVA\",\"cmdType\":\"\",\"actionLevel\":\"0\",\"auditLevel\":\"1\",\"riskLevel\":\"0\",\"objectOwner\":\"\",\"objectName\":\"\",\"objectType\":\"\"}}}";
		for (int i = 0; i < count; i++) {
			auditStrs.put(str);
		}

		Long startTime = new Date().getTime();
		List<String> datas = new ArrayList<>();
		while (auditStrs.remainingCapacity() < count){
			auditStrs.poll(100, TimeUnit.MILLISECONDS);
		}
		Long endTime = new Date().getTime();

		System.out.println(endTime - startTime);
	}
}
