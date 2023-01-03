package com.feng.baseframework.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * baseframework
 * 2022/5/14 22:09
 * 读取数据-利用linux中fifo文件（命名管道）实现进程间通讯
 *
 * @author lanhaifeng
 * @since
 **/
public class PipeRead {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileInputStream inputStream = new FileInputStream("/home/davinci/fifo");
        byte[] bs = new byte[1024];
        int n = 0;
        while ((n = inputStream.read(bs)) != -1) {
            System.out.println(n);
            System.out.println(new String(bs, 0, n));
        }
        System.out.println(n);
    }
}
