package com.algorithm.leetcode.test.task;

import java.util.concurrent.*;

/**
 * @author xiaoaa
 * @date 2023/5/6 22:23
 **/
public class CustomerThreadPool {

    private volatile static ThreadPoolExecutor threadPool;

    public static ExecutorService TaskPool() {
        int corePoolSize = 20;
        int maximumPoolSize = 30;
        long keepAliveTime = 600;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        return TaskPool(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory,handler);
    }

    public static ExecutorService TaskPool(int corePoolSize,
                                           int maximumPoolSize,
                                           int workQueueSize) {
        long keepAliveTime = 600;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>(workQueueSize);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        return TaskPool(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory,handler);
    }


    public static ExecutorService TaskPool(int corePoolSize,
                                           int maximumPoolSize,
                                           long keepAliveTime,
                                           TimeUnit unit,
                                           BlockingQueue<Runnable> workQueue,
                                           ThreadFactory threadFactory,
                                           RejectedExecutionHandler handler) {
        if (threadPool != null) {
            return threadPool;
        }
       synchronized (CustomerThreadPool.class){
           if (threadPool == null) {
               threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                       workQueue, threadFactory, handler);
           }
       }
       return threadPool;
    }


}
