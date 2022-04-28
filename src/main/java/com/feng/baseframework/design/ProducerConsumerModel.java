package com.feng.baseframework.design;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * baseframework
 * 2022/4/28 23:56
 * 生产者消费者模式:使用阻塞队列
 *
 * @author lanhaifeng
 * @since
 **/
public class ProducerConsumerModel {

    public static void main(String[] args) {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>(10);
        BlockQueueHandle blockQueueHandle = new BlockQueueHandle(queue);

        new Producer(blockQueueHandle).start();
        new Consumer(blockQueueHandle).start();
    }

    static class Producer extends Thread {
        private BlockQueueHandle blockQueueHandle;

        public Producer(BlockQueueHandle blockQueueHandle) {
            this.blockQueueHandle = blockQueueHandle;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2000L);
                int i = 0;
                while (true) {
                    blockQueueHandle.produce(UUID.randomUUID().toString());
                    i++;
                    if(i > 1000) {
                        return;
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("生产数据失败：" + ExceptionUtils.getStackTrace(e));
            }
        }
    }

    static class Consumer extends Thread {
        private BlockQueueHandle blockQueueHandle;

        public Consumer(BlockQueueHandle blockQueueHandle) {
            this.blockQueueHandle = blockQueueHandle;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2000L);
                int i = 0;
                while (true) {
                    blockQueueHandle.consume();
                    i++;
                    if(i > 1000) {
                        return;
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("生产数据失败：" + ExceptionUtils.getStackTrace(e));
            }
        }
    }

    static class BlockQueueHandle {
        private final BlockingQueue<String> blockingQueue;
        private final StopWatch produceWatch;
        private final StopWatch consumerWatch;

        public BlockQueueHandle(BlockingQueue<String> blockingQueue) {
            this.blockingQueue = blockingQueue;
            this.consumerWatch = new StopWatch();
            this.produceWatch = new StopWatch();
            produceWatch.setKeepTaskList(false);
            consumerWatch.setKeepTaskList(false);

            Assert.notNull(blockingQueue, "数据队列为空");
            Assert.notNull(produceWatch, "produceWatch为空");
            Assert.notNull(consumerWatch, "consumerWatch为空");
        }

        public void produce(String message) throws InterruptedException {
            produceWatch.start();
            blockingQueue.put(message);
            produceWatch.stop();
            System.out.println("【produce：" + message + "】" + produceWatch.prettyPrint());
        }

        public void consume() throws InterruptedException {
            consumerWatch.start();
            String message = blockingQueue.take();
            consumerWatch.stop();
            System.out.println("【consume：" + message + "】" + consumerWatch.prettyPrint());
        }
    }


}
