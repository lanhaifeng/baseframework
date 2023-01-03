##### 位移的应用  

使用 AtomicInteger 类型的 ctl 变量，同时记录线程池的运行状态以及已用线程池的容量。  

  > 高三位用来存储线程池运行状态，状态分为RUNNING状态，SHUTDOWN 状态，STOP状态，TIDYING 状态，TERMINATED 状态

  > 剩余29位表示已用容量

常量  
```
@Native public static final int SIZE = 32;
private static final int COUNT_BITS = Integer.SIZE - 3;
```

1.ctl剩余29位表示已用容量  
```
private static final int CAPACITY   = (1 << COUNT_BITS) - 1;
```
此处即CAPACITY等于2的29次幂减去1  

2.ctl高三位表示状态
```
    // runState is stored in the high-order bits
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final int STOP       =  1 << COUNT_BITS;
    private static final int TIDYING    =  2 << COUNT_BITS;
    private static final int TERMINATED =  3 << COUNT_BITS;
```
二进制表示如下，高三位表示状态
```
RUNNING:    11100000000000000000000000000000
SHUTDOWN:   00000000000000000000000000000000
STOP:       00100000000000000000000000000000
TIDYING:    01000000000000000000000000000000
TERMINATED: 01100000000000000000000000000000
```

3.ctl如何获取状态和容量  

3.1runStateOf：由ctl获取状态
```
 private static int runStateOf(int c)     { return c & ~CAPACITY; }
```
c：ctl.get()，高三位是状态位，后29位已用容量位   
~：取反操作  
~CAPACITY：11100000000000000000000000000000  
&：与操作，两者都为1则为1，否则为0  
c与~CAPACITY的结果：高三位由原状态位决定，后29位刚好为0  

3.2workerCountOf：由ctl获取已用容量
```
private static int workerCountOf(int c)  { return c & CAPACITY; }
``` 
CAPACITY：00011111111111111111111111111111  
c & CAPACITY的结果：高三位刚好为0，后29位由已用容量位决定  

3.3ctlOf：由状态和容量生产ctl
```
private static int ctlOf(int rs, int wc) { return rs | wc; }
```
rs：状态高三位有值，后29位为0
wc：工作线程数量
|：或操作，有一个为1，则为1，否则为0
rs | wc的结果：刚好高三位由状态决定，后29位由已用容量决定


##### 状态
RUNNIN状态

+ 在该状态下，线程池接受新任务并会处理阻塞队列中的任务
+ 其二进制表示的，高三位值是 111

SHUTDOWN 状态

+ 在该状态下，线程池不接受新任务，但会处理阻塞队列中的任务
+ 其二进制的高三位为： 000

STOP 状态

+ 在该状态下，线程池不接受新的任务且不会处理阻塞队列中的任务，并且会中断正在执行的任务
+ 其二进制的高三位为： 010。

TIDYING状态

+ 所有任务都执行完成，且工作线程数为0，将要调用terminated方法
+ 其二进制的高三位为： 010

TERMINATED状态

+ 最终状态，为执行terminated()方法后的状态
+ 二进制的高三位为110

其状态的转换关系如下：

+ 当调用：shutdown() 方法时，其状态由 RUNNING 状态 转换为 SHUTDOWN (状态)

+ 当调用：shutdownNow() 方法是，其状态由 (RUNNING or SHUTDOWN) 转换为 STOP

+ 当阻塞队列与线程池两者均为空时，状态由 SHUTDOWN 转换为 TIDYING

+ 当线程池任务为空时，状态由 STOP 转换为 TIDYING 

+ 当 terminated() 方法执行完成后，状态由 TIDYING 转换为 TERMIN


##### 构造器
构造方法有四个，以参数最多的构造器解释其参数
```
public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
        if (corePoolSize < 0 ||
            maximumPoolSize <= 0 ||
            maximumPoolSize < corePoolSize ||
            keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (workQueue == null || threadFactory == null || handler == null)
            throw new NullPointerException();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }
```

| 序号 | 名称 | 类型 | 含义 |   
| ---- | ---- | ---- | ---- |  
| 1 | corePoolSize | int | 核心线程池大小 |  
| 2 | maximumPoolSize | int | 最大线程池大小 | 
| 3 | keepAliveTime | long | 线程最大空闲时间 | 
| 4 | unit | TimeUnit | 时间单位 | 
| 5 | workQueue | BlockingQueue<Runnable> | 线程等待队列 | 
| 6 | threadFactory | ThreadFactory | 线程创建工厂 | 
| 7 | handler | RejectedExecutionHandler | 拒绝策略 | 

创建线程池工具类：java.util.concurrent.Executors
1.FixedThreadPool
```
    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
    }
```

> corePoolSize与maximumPoolSize相等，即其线程全为核心线程，是一个固定大小的线程池，是其优势；  
> keepAliveTime = 0 该参数默认对核心线程无效，而FixedThreadPool全部为核心线程；  
> workQueue 为LinkedBlockingQueue（无界阻塞队列），队列最大值为Integer.MAX_VALUE。
如果任务提交速度持续大余任务处理速度，会造成队列大量阻塞。因为队列很大，很有可能在拒绝策略前，内存溢出。是其劣势；
> FixedThreadPool的任务执行是无序的；

适用场景：可用于Web服务瞬时高峰，但需注意长时间持续高峰情况造成的队列阻塞。

2.CachedThreadPool
```
     public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
    }
```

> corePoolSize = 0，maximumPoolSize = Integer.MAX_VALUE，即线程数量几乎无限制；
> keepAliveTime = 60s，线程空闲60s后自动结束。
> workQueue 为 SynchronousQueue 同步队列，这个队列类似于一个接力棒，入队出队必须同时传递，
因为CachedThreadPool线程创建无限制，不会有队列等待，所以使用SynchronousQueue；
适用场景：快速处理大量耗时较短的任务，如Netty的NIO接受请求时，可使用CachedThreadPool

3.SingleThreadExecutor
```
    public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>()));
    }
```

> FixedThreadPool可以向下转型为ThreadPoolExecutor，并对其线程池进行配置，
而SingleThreadExecutor被包装后，无法成功向下转型。因此，SingleThreadExecutor被定以后，
无法修改，做到了真正的Single

4.ScheduledThreadPool
```
    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
        return new ScheduledThreadPoolExecutor(corePoolSize);
    }
```

> newScheduledThreadPool调用的是ScheduledThreadPoolExecutor的构造方法，
而ScheduledThreadPoolExecutor继承了ThreadPoolExecutor，构造是还是调用了其父类的构造方法。
```
    public ScheduledThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS,
              new DelayedWorkQueue());
    }
```
初始化线程池时线程数的选择：

如果任务是IO密集型，一般线程数需要设置2倍CPU数以上，以此来尽量利用CPU资源。
如果任务是CPU密集型，一般线程数量只需要设置CPU数加1即可，更多的线程数也只能增加上下文切换，不能增加CPU利用率。
上述只是一个基本思想，如果真的需要精确的控制，还是需要上线以后观察线程池中线程数量跟队列的情况来定。



##### 核心逻辑
1.execute
```
public void execute(Runnable command) {
    if (command == null)
        throw new NullPointerException();
    /*
     * Proceed in 3 steps:
     *
     * 1. If fewer than corePoolSize threads are running, try to
     * start a new thread with the given command as its first
     * task.  The call to addWorker atomically checks runState and
     * workerCount, and so prevents false alarms that would add
     * threads when it shouldn't, by returning false.
     *
     * 2. If a task can be successfully queued, then we still need
     * to double-check whether we should have added a thread
     * (because existing ones died since last checking) or that
     * the pool shut down since entry into this method. So we
     * recheck state and if necessary roll back the enqueuing if
     * stopped, or start a new thread if there are none.
     *
     * 3. If we cannot queue task, then we try to add a new
     * thread.  If it fails, we know we are shut down or saturated
     * and so reject the task.
     */
    int c = ctl.get();
    //情况1
    if (workerCountOf(c) < corePoolSize) {
        if (addWorker(command, true))
            return;
        c = ctl.get();
    }
    //情况2
    if (isRunning(c) && workQueue.offer(command)) {
        int recheck = ctl.get();
        if (! isRunning(recheck) && remove(command))
            reject(command);
        else if (workerCountOf(recheck) == 0)
            addWorker(null, false);
    }
    //情况3
    else if (!addWorker(command, false))
        reject(command);
}
```
+ 第一种情况  
> 线程池的线程数量小于corePoolSize核心线程数量，开启核心线程执行任务

+ 第二种情况  
> 线程池的线程数量不小于corePoolSize核心线程数量，或者开启核心线程失败，尝试将任务以非阻塞的方式添加到任务队列
  重新检测一次线程池状态，当状态不为running时，将任务从任务队列移除，并调用拒绝方法处理；或线程池数量等于0，创建一个非核心线程处理任务

+ 第三种情况
> 任务队列已满导致添加任务失败，开启新的非核心线程执行任务，开启非核心线程失败，调用拒绝方法处理

2.reject  
```
final void reject(Runnable command) {
        handler.rejectedExecution(command, this);
}
```
RejectedExecutionHandler：任务拒绝策略，当运行线程数已达到maximumPoolSize，队列也已经装满时会调用该参数拒绝任务，
默认情况下是AbortPolicy，表示无法处理新任务时抛出异常。以下是JDK1.5提供的四种策略。  

+ AbortPolicy：直接抛出异常。
+ CallerRunsPolicy：只用调用者所在线程来运行任务。
+ DiscardOldestPolicy：丢弃队列里最老的一个任务，并执行当前任务。
+ DiscardPolicy：不处理，丢弃掉。
+ 当然也可以根据应用场景需要来实现RejectedExecutionHandler接口自定义策略。如记录日志或持久化不能处理的任务。


3.addWorker方法/Worker类
1.addWorker
```
private boolean addWorker(Runnable firstTask, boolean core) {
        //标号
        retry:
        /**
        *   使用CAS机制轮询线程池的状态
        *   1.如果线程池处于SHUTDOWN并且任务不为空或工作队列为空则拒绝执行任务
        *   2.如果线程池大于SHUTDOWN状态则拒绝执行任务
        **/
        for (;;) {
            int c = ctl.get();
            int rs = runStateOf(c);

            // Check if queue empty only if necessary.
            if (rs >= SHUTDOWN &&
                ! (rs == SHUTDOWN &&
                   firstTask == null &&
                   ! workQueue.isEmpty()))
                return false;

            //使用CAS机制尝试将当前线程数+1
            //如果是核心线程当前线程数必须小于corePoolSize 
            //如果是非核心线程则当前线程数必须小于maximumPoolSize
            //如果当前线程数不小于线程池支持的最大线程数CAPACITY 也会返回失败
            //如果线程状态发生改变CAS失败继续外层循环
            for (;;) {
                int wc = workerCountOf(c);
                if (wc >= CAPACITY ||
                    wc >= (core ? corePoolSize : maximumPoolSize))
                    return false;
                if (compareAndIncrementWorkerCount(c))
                    break retry;
                c = ctl.get();  // Re-read ctl
                if (runStateOf(c) != rs)
                    continue retry;
                // else CAS failed due to workerCount change; retry inner loop
            }
        }

        //成功执行了CAS操作将线程池数量+1，下面创建线程
        boolean workerStarted = false;
        boolean workerAdded = false;
        Worker w = null;
        try {
            w = new Worker(firstTask);
            final Thread t = w.thread;
            if (t != null) {
                final ReentrantLock mainLock = this.mainLock;
                mainLock.lock();
                try {
                    // Recheck while holding lock.
                    // Back out on ThreadFactory failure or if
                    // shut down before lock acquired.
                    int rs = runStateOf(ctl.get());

                    if (rs < SHUTDOWN ||
                        (rs == SHUTDOWN && firstTask == null)) {
                        // 校验线程是否处于处于活跃状态
                        if (t.isAlive()) // precheck that t is startable
                            throw new IllegalThreadStateException();
                        // 将 worker 添加到线程池中，其实现为：HashSet<Worker> workers = new HashSet<Worker>();
                        workers.add(w);
                        int s = workers.size();
                        if (s > largestPoolSize)
                            largestPoolSize = s;
                        workerAdded = true;
                    }
                } finally {
                    mainLock.unlock();
                }
                // 添加成功时，则调用start进行执行
                if (workerAdded) {
                    t.start();
                    workerStarted = true;
                }
            }
        } finally {
            // 如果线程未启动成功，则执行addWorkerFailed方法。
            if (! workerStarted)
                addWorkerFailed(w);
        }
        return workerStarted;
    }
```

2.Worker类
```
Worker(Runnable firstTask) {
    setState(-1); // inhibit interrupts until runWorker
    this.firstTask = firstTask;
    //通过ThreadFactory()工厂创建线程
    this.thread = getThreadFactory().newThread(this);
}

//实现了Runable接口，在调用start()方法候，实际执行的是run方法，委托给外边的runWorker方法
/** Delegates main run loop to outer runWorker  */
public void run() {
    runWorker(this);
}
```
runWorker方法
```
final void runWorker(Worker w) {
    Thread wt = Thread.currentThread();
    Runnable task = w.firstTask;
    w.firstTask = null;
    w.unlock(); // allow interrupts
    boolean completedAbruptly = true;
    try
        while (task != null || (task = getTask()) != null) {
            w.lock();
            //如果线程池状态大于等于stop状态，确保线程是中断
            //如果线程池状态小于stop状态，确保线程未中断
            if ((runStateAtLeast(ctl.get(), STOP) ||
                 (Thread.interrupted() &&
                  runStateAtLeast(ctl.get(), STOP))) &&
                !wt.isInterrupted())
                wt.interrupt();
            try {
                //执行前的操作方法，空实现。可根据需求进行实现。
                beforeExecute(wt, task);
                Throwable thrown = null;
                try {
                    //任务执行
                    task.run();
                } catch (RuntimeException x) {
                    thrown = x; throw x;
                } catch (Error x) {
                    thrown = x; throw x;
                } catch (Throwable x) {
                    thrown = x; throw new Error(x);
                } finally {
                   // 执行后方法，空实现。可根据实际需求进行实现。
                    afterExecute(task, thrown);
                }
            } finally {
                //这里设为null，也就是循环体再执行的时候会调用getTask方法
                task = null;
                w.completedTasks++;
                w.unlock();
            }
        }
        completedAbruptly = false;
    } finally {
        //当指定任务执行完成，阻塞队列中也取不到可执行任务时，会进入这里，做一些善后工作
        //比如在corePoolSize跟maximumPoolSize之间的woker会进行回收
        processWorkerExit(w, completedAbruptly);
    }
}
```
getTask方法
```
private Runnable getTask() {
    boolean timedOut = false; // Did the last poll() time out?

    for (;;) {
        int c = ctl.get();
        int rs = runStateOf(c);

        // Check if queue empty only if necessary.
        if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
            decrementWorkerCount();
            return null;
        }

        int wc = workerCountOf(c);

        // Are workers subject to culling?
        boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;

        if ((wc > maximumPoolSize || (timed && timedOut))
            && (wc > 1 || workQueue.isEmpty())) {
            if (compareAndDecrementWorkerCount(c))
                return null;
            continue;
        }

        try {
            //根据超时配置有两种方法取出任务
            Runnable r = timed ?
                workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                workQueue.take();
            if (r != null)
                return r;
            timedOut = true;
        } catch (InterruptedException retry) {
            timedOut = false;
        }
    }
}
```
这里getTask()方法是要重点说明的，它的实现跟我们构造参数keepAliveTime存活时间有关。我们都知道
keepAliveTime代表了线程池中的线程（即woker线程）的存活时间，如果到期则回收woker线程，
这个逻辑的实现就在getTask中。

getTask()方法就是去阻塞队列中取任务，用户设置的存活时间，就是从这个阻塞队列中取任务等待的最大时
间，如果getTask返回null，意思就是woker等待了指定时间仍然没有取到任务，此时就会跳过循环体，
进入woker线程的销毁逻辑。

这个getTask()方法通过一个循环不断轮询任务队列有没有任务到来，首先判断线程池是否处于正常运行状态，根据超时配置有两种方法取出任务：

- BlockingQueue.poll 阻塞指定的时间尝试获取任务，如果超过指定的时间还未获取到任务就返回null。
- BlockingQueue.take 这种方法会在取到任务前一直阻塞。