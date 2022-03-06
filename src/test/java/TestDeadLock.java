/**
 * 类名:TestDeadLock <br/>
 * 描述:测试死锁 <br/>
 * 时间:2021/11/8 16:51 <br/>
 *
 * @author lanhaifeng
 */
public class TestDeadLock {

    public static final Object lock1 = new Object();
    public static final Object lock2 = new Object();
    public static void main(String[] args) {
        new Thread(
                () -> {
                    synchronized (lock1) {
                        try {
                            Thread.sleep(3000);
                            synchronized (lock2) {
                                System.out.println("thread1");
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, "thread1").start();

        new Thread(
                () -> {
                    synchronized (lock2) {
                        try {
                            Thread.sleep(3000);
                            synchronized (lock1) {
                                System.out.println("thread2");
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, "thread2").start();
    }
}
