package com.feng.baseframework.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * baseframework
 * 2022/5/14 22:15
 * 写数据-利用linux中fifo文件（命名管道）实现进程间通讯
 *
 * @author lanhaifeng
 * @since
 **/
public class PipeWrite {
    public static void main(String[] args) throws IOException, InterruptedException, FileNotFoundException {
        FileOutputStream outputStream = new FileOutputStream("/home/davinci//fifo");
        for(;;){
            outputStream.write("abcdef".getBytes());
            outputStream.flush();
            Thread.sleep(1000);
        }
//        outputStream.close();
    }
}
