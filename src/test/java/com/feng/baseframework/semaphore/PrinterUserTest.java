package com.feng.baseframework.semaphore;

import com.feng.baseframework.common.MockitoBaseTest;
import org.junit.Test;
import org.mockito.Spy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrinterUserTest extends MockitoBaseTest {

    @Spy
    private PrinterManage printerManage = new PrinterManage(2);

    @Test
    public void run() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        String userName = "test";
        String content = "content";
        for (int i = 0; i < 10; i++) {
            executorService.execute(new PrinterUser(userName + i, content + i, printerManage) {
                @Override
                public void run() {
                    super.run();
                    latch.countDown();
                }
            });

        }

        latch.await();
//            executorService.execute(new PrinterUser(userName + i, content + i, printerManage));
    }
}