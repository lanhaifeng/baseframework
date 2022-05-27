package com.feng.baseframework.io;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

/**
 * baseframework
 * 2022/5/24 23:11
 * 共享内存用于进程间通信，原理是将文件映射到内存中实现的
 * 注意：与linux中共享内存并不相同，通过ipcs -m可以看到，文件映射方式并没有使用linux的共享内存
 *      常用进程通信指令：ipcs, ipcrm, ipcmk, msgrcv, msgsnd, semget, semop, shmat, shmdt, shmget
 *
 * @author lanhaifeng
 * @since
 **/
public class SharedMemory {

    public enum CmdType {
        all,write,read;
    }

    public static void main(String[] args) {
        try {
            String type = "";
            String filePath = "";
            if(args.length == 0) {
                type = "all";
            }
            if(args.length == 0 || args.length == 1) {
                File file = File.createTempFile("shared-memory", ".tmp");
                filePath = file.getAbsolutePath();
            }
            //args[1] = "C:\\Users\\Admin\\Desktop\\test.txt";
            if(args.length >= 2) {
                type = args[0];
                filePath = args[1];
            }
            String fileName = filePath;
            if(CmdType.all.name().equals(type)) {
                CountDownLatch countDownLatch = new CountDownLatch(2);
                new Thread(() -> {
                    Write.write(fileName);
                    countDownLatch.countDown();
                }).start();
                new Thread(() -> {
                    Read.read(fileName);
                    countDownLatch.countDown();
                }).start();

                countDownLatch.await();
            }
            if(CmdType.write.name().equals(type)) {
                Write.write(fileName);
            }
            if(CmdType.read.name().equals(type)) {
                Read.read(fileName);
            }
        } catch (Exception e) {
            System.out.println(getStackTrace(e));
        }
    }

    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    public static String reverse(String str) {
        return str == null ? null : (new StringBuilder(str)).reverse().toString();
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            System.out.println("sleep fail:" + getStackTrace(e));
        }
    }

    /**
     * 阻塞获取文件锁
     * FileChannel.tryLock非阻塞获取锁，当锁被占用windows下会抛出OverlappingFileLockException异常
     * FileChannel.lock阻塞获取锁
     *
     * @param fc            文件管道
     * @param position      起始位置
     * @param size          大小
     * @param shared        是否共享锁
     * @return  返回锁
     */
    public static FileLock lock(FileChannel fc, long position, long size, boolean shared) {
        FileLock lock = null;
        while (true) {
            try {
                lock = fc.tryLock(position, size, shared);
                break;
            } catch (OverlappingFileLockException e) {
                System.out.println("FileLock lock is using");
            } catch (IOException e) {
                System.out.println("FileLock lock fail:" + getStackTrace(e));
            }
        }
        return lock;
    }


    static class Read {
        public static void read(String fileName) {
            // 获得一个只读的随机存取文件对象
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(fileName, "r");
            } catch (Exception e) {
                System.out.println("read fail:" + getStackTrace(e));
                return;
            }
            // 获得相应的文件通道
            FileChannel fc = raf.getChannel();

            //获取锁
            try (FileLock flock = lock(fc, 0L, Long.MAX_VALUE, true)) {
                // 取得文件的实际大小，以便映像到共享内存
                int size = (int) fc.size();

                // 获得共享内存缓冲区，该共享内存只读
                MappedByteBuffer mapBuf = fc.map(FileChannel.MapMode.READ_ONLY, 0, size);

                byte[] bytes = new byte[size];
                mapBuf.get(bytes);
                System.out.println(new String(Arrays.copyOf(bytes, bytes.length)));
            } catch (Exception e) {
                System.out.println("read fail:" + getStackTrace(e));
            }
            SharedMemory.sleep(10000);
        }
    }

    static class Write {
        public static void write(String fileName) {
            // 获得一个只读的随机存取文件对象
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(fileName, "rw");
            } catch (FileNotFoundException e) {
                System.out.println("write fail:" + getStackTrace(e));
                return;
            }
            // 获得相应的文件通道
            FileChannel fc = raf.getChannel();

            try (FileLock flock = lock(fc,0L, Long.MAX_VALUE, true)) {
                // 取得文件的实际大小，以便映像到共享内存
                int fileSize = (int) fc.size();

                //映射的起始位置，为0就是覆盖写，为size既追加写
                int position = fileSize;

                //写入文件信息
                String message = "\nhow are you, mike\n";
                byte[] datas = message.getBytes(StandardCharsets.UTF_8);

                //映射到内存中大小，可以比要写入的数据更大，但是不能更小否则无法写入
                int size = datas.length;
                // 获得共享内存缓冲区，该共享内存可读写
                MappedByteBuffer mapBuf = fc.map(FileChannel.MapMode.READ_WRITE, position, size);

                mapBuf.put(datas);
                //定位回到初始位置
                mapBuf.flip();
                mapBuf.put(reverse(message).getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                System.out.println("write fail:" + getStackTrace(e));
            }
            SharedMemory.sleep(10000);
        }
    }
}
