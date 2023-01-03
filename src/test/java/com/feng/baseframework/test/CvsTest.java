package com.feng.baseframework.test;

import com.feng.baseframework.util.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Date;

/**
 * baseframework
 * 2018/10/8 15:58
 * cvs读写测试
 *
 * @author lanhaifeng
 * @since
 **/
public class CvsTest {

	@Test
	public void writeTest(){
		Long start = new Date().getTime();
		File file = FileUtils.getFileByRelativePath("/svs/test.csv");
		RandomAccessFile fout = null;
		FileChannel fcout = null;
		try {
			fout = new RandomAccessFile(file, "rw");
			long filelength = fout.length();//获取文件的长度
			fout.seek(filelength);//将文件的读写指针定位到文件的末尾
			fcout = fout.getChannel();//打开文件通道
			FileLock flout = null;
			while (true) {
				try {
					flout = fcout.tryLock();//不断的请求锁，如果请求不到，等一秒再请求
					break;
				} catch (Exception e) {
					System.out.print("lock is exist ......");
					Thread.sleep(1000);
				}
			}
			String head = "name,sex,age\n";
			String str = "张三,男,12\n";//需要写入的内容

			fout.write(head.getBytes());
			//将需要写入的内容写入文件
			for(int i = 0; i < 100000; i++){
				fout.write(str.getBytes());
			}

			flout.release();
			fcout.close();
			fout.close();
			Long end = new Date().getTime();
			System.out.println((end - start) + "ms");
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.print("file no find ...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (fcout != null) {
				try {
					fcout.close();
				} catch (IOException e) {
					e.printStackTrace();
					fcout = null;
				}
			}
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
					fout = null;
				}
			}
		}

	}
}
