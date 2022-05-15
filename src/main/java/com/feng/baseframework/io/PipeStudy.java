package com.feng.baseframework.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

/**
 * baseframework
 * 2022/5/13 23:58
 * 管道通信
 * 1.java提供的管道只能用于线程间通信
 *  1.1 字节流管道 PipedInputStream PipedOutputStream
 *  1.2 字符流管道 PipedReader PipedWriter
 *  1.3 Pipe管道 Pipe.SourceChannel Pipe.SinkChannel
 *  1.4 java父子进程通过匿名管道通信
 *   1.4.1 父进程见PipeParentProcess
 *   1.4.2 子进程PipeChildProcess
 *
 * 2.利用linux的管道文件实现进程间通信
 *  2.1 匿名管道单向通信
 *  2.2 命名管道双向通信
 *  2.3 步骤
 *   2.3.1 mkfifo /tmp/fifo 创建 fifo 文件，可以利用new ProcessBuilder(command).inheritIO().start()执行创建fifo文件命令
 *   2.3.2 进程使用FileOutputStream写 /tmp/fifo 文件
 *   2.3.3 进程使用FileInputStream读 /tmp/fifo 文件
 *
 * @author lanhaifeng
 * @since v1.0
 **/
public class PipeStudy {

    private static Logger logger = LoggerFactory.getLogger(PipeStudy.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Pipe pipe = Pipe.open();

        new Thread(() -> {
            Pipe.SinkChannel sinkChannel = pipe.sink();
            String newData = "New String to write to file..." + System.currentTimeMillis();
            ByteBuffer buf = ByteBuffer.allocate(48);
            buf.clear();
            buf.put(newData.getBytes());
            buf.flip();

            System.out.println(System.currentTimeMillis() + " start write");
            StopWatch stopWatch = new StopWatch("write");
            stopWatch.start();
            while(buf.hasRemaining()) {
                try {
                    sinkChannel.write(buf);
                } catch (IOException e) {
                    logger.error("管道写数据失败", e);
                }
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                logger.error("sleep失败", e);
            }
            stopWatch.stop();
            System.out.println(stopWatch.prettyPrint());
            countDownLatch.countDown();
        }).start();

        new Thread(()->{
            Pipe.SourceChannel sourceChannel = pipe.source();
            ByteBuffer buf = ByteBuffer.allocate(48);
            try {
                System.out.println(System.currentTimeMillis() + " start read");
                StopWatch stopWatch = new StopWatch("read");
                stopWatch.start();

                int bytesRead = sourceChannel.read(buf);
                System.out.println(new String(Arrays.copyOf(buf.array(), bytesRead)));
                Thread.sleep(1000L);

                stopWatch.stop();
                System.out.println(stopWatch.prettyPrint());
            } catch (Exception e) {
                logger.error("管道读数据失败", e);
            }
            countDownLatch.countDown();
        }).start();

        countDownLatch.await();

        CountDownLatch latch = new CountDownLatch(2);
        PipedOutputStream output = new PipedOutputStream();
        PipedInputStream  input  = new PipedInputStream(output);
        new Thread(()-> {
            try {
                output.write("Hello world, pipe!".getBytes());
                System.err.println(Thread.currentThread().getName()+"已输出!");
            } catch (IOException ignored) {
            }
            latch.countDown();
        }).start();

        new Thread(()-> {
            try {
                int data = input.read();
                System.err.println(Thread.currentThread().getName());
                while(data != -1){
                    System.out.print((char) data);
                    data = input.read();
                }
            } catch (IOException e) {
            }
            latch.countDown();
        }).start();

        latch.await();

        CountDownLatch downLatch = new CountDownLatch(2);
        PipedWriter pipedWriter = new PipedWriter();
        PipedReader pipedReader = new PipedReader(pipedWriter);

        new Thread(()->{
            try {
                pipedWriter.write("pipedWriter write msg!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            downLatch.countDown();
        }).start();


        new Thread(()->{
            char[] str = new char[1024];
            try {
                int size = pipedReader.read(str);
                System.err.println(new String(Arrays.copyOf(str, size)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            downLatch.countDown();
        }).start();
        downLatch.await();
    }
}
