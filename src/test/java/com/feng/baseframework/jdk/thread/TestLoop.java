package com.feng.baseframework.jdk.thread;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * baseframework
 * 2019/1/5 10:57
 * 测试类
 *
 * @author lanhaifeng
 * @since
 **/
public class TestLoop {

	private static LinkedBlockingQueue<String> auditQueue;
	private static int queueCount;
	private static String data;

	public static void setUp(){
		try{
			queueCount = 100;
			if(auditQueue == null){
				auditQueue = new LinkedBlockingQueue<String>(queueCount);
			}
			auditQueue.clear();
			data = "{\"linkSessionID\":15731489897925079135,\"SQLSessionID\":\"1401383901\n" +
					"    3392633502\",\"SQLAccessID\":\"20190104153056845841\",\"token\":\"\",\"eUser\":\"\",\"eUserID\":\"\",\"eUserName\":\"\",\"certName\":\"\",\"dbWorkMode\":5,\"content\":{\"riskEngine\":[{\"matched\":[\"1\"],\"riskClass\":\"90\",\"ris\n" +
					"    kLevel\":0,\"sql\":\"DROP DATABASES\",\"actionLevel\":0,\"ruleId\":2011000100000001901,\"ruleName\":\"[运维流量全审计]\"}],\"dbdwAccessControl\":{\"ruleName\":\"PUBLIC\",\"cmdType\":\"DROP\",\"actionLevel\":\"0\",\"audi\n" +
					"    tLevel\":\"1\",\"riskLevel\":\"0\",\"objectOwner\":\"\",\"objectName\":\"\",\"objectType\":\"\"}}},{\"linkSessionID\":15663229985521174557,\"SQLSessionID\":\"13596223318731062139\",\"SQLAccessID\":\"20190104153110232715\n" +
					"    \",\"token\":\"\",\"eUser\":\"\",\"eUserID\":\"\",\"eUserName\":\"\",\"certName\":\"\",\"dbWorkMode\":5,\"content\":{\"riskEngine\":[{\"matched\":[\"1\"],\"riskClass\":\"90\",\"riskLevel\":0,\"sql\":\"SELECT * FROM USERS WHERE UTL_\n" +
					"    INADDR.GET_HOST_NAME ( ( SELECT USER FROM DUAL ) )\",\"actionLevel\":0,\"ruleId\":2011000100000001901,\"ruleName\":\"[运维流量全审计]\"}]}}";
			for (int i = 0; i < queueCount; i++) {
				auditQueue.put(data);
			}
		}catch(Exception e){
			System.out.println("build data error");
		}
	}

	public static void main(String[] args) {
		setUp();
		/*new LoopQueueSpeed(auditQueue, 1).start();
		new LoopQueueSpeed(auditQueue, 1).start();*/
		new LoopQueueSpeed(auditQueue, 1).start();
		new LoopQueueSpeed(auditQueue, 1).start();
		new LoopQueueSpeed(auditQueue, 1).start();
		new LoopQueueSpeed(auditQueue, 1).start();
	}

}
