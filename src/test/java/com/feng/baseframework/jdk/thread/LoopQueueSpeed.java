package com.feng.baseframework.jdk.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * baseframework
 * 2019/1/5 10:34
 * 测试遍历速度
 *
 * @author lanhaifeng
 * @since
 **/
public class LoopQueueSpeed extends Thread {

	private LinkedBlockingQueue<String> auditQueue;
	private int type;

	public LoopQueueSpeed(LinkedBlockingQueue<String> auditQueue, int type) {
		this.auditQueue = auditQueue;
		this.type = type;
	}

	@Override
	public void run() {
		if(type == 1){
			Long startTime = System.nanoTime();
			int count = auditQueue.size();
			drainTo();
			Long endTime = System.nanoTime();
			System.out.println("drainTo num:" + count + ",cost:" + (endTime - startTime));
		}
		if(type == 2){
			Long startTime = System.nanoTime();
			int count = auditQueue.size();
			poll();
			Long endTime = System.nanoTime();
			System.out.println("poll num:" + count + ",cost:" + (endTime - startTime));
		}
	}

	private void drainTo(){
		List<String> auditStr = new ArrayList<>();
		while (!auditQueue.isEmpty()){
			auditQueue.drainTo(auditStr, 10000);
		}
	}

	private void poll(){
		try{
			List<String> auditStr = new ArrayList<>();
			while (!auditQueue.isEmpty()){
				auditStr.add(auditQueue.poll(100, TimeUnit.MILLISECONDS));
			}
		}catch(Exception e){
			System.out.println("exception");
		}
	}

}
