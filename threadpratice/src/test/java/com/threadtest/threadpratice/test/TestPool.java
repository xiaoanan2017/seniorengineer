package com.threadtest.threadpratice.test;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xiaoaa
 * @date 2023/2/6 21:06
 **/
@Slf4j
public class TestPool {

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(1, 1, 1000,TimeUnit.MICROSECONDS ,
                (queue, task)-> {
                task.run();
        });
        for (int i = 0; i < 4; i++) {
            int j = i;
            threadPool.execute(()-> {
                log.debug("执行的是第 {}个线程的任务", j);
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            log.debug("{}", j);
        }
    }
}

@FunctionalInterface
interface RejectPolicy<T>{

    void reject(BlockingQueue<T> queue, T task);
}


@Slf4j
class BlockingQueue<T>{
    //任务队列
    private Deque<T> queue = new ArrayDeque<>();
    //锁
    private ReentrantLock lock = new ReentrantLock();
    //生产者条件变量
    private Condition fullWaitSet = lock.newCondition();
    //消费者条件变量
    private Condition emptyWaitSet = lock.newCondition();
    //容量
    private int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    //阻塞获取
    public T take() {
        lock.lock();
        try {
            while(queue.isEmpty()) {
                //等待
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //被打断了 继续等待即可
                }
            }
            T result = queue.removeFirst();
            //获取完成，唤醒一个生产者线程继续添加
            fullWaitSet.signal();
            return result;
        } finally {
            lock.unlock();
        }
    }

    // 带超时获取
    public T take(long timeout, TimeUnit timeUnit) {
        long nanos = timeUnit.toNanos(timeout);
        lock.lock();
        try {
            while (queue.isEmpty()) {
                //等待
                try {
                    if (nanos <= 0L) {
                        return null;
                    }
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //被打断了 继续等待即可
                }
            }
            T result = queue.removeFirst();
            //获取完成，唤醒一个生产者线程继续添加
            fullWaitSet.signal();
            return result;
        } finally {
            lock.unlock();
        }
    }

    //带超时的阻塞添加
    public boolean put(T task,long timeout, TimeUnit timeUnit) {
        long nanos = timeUnit.toNanos(timeout);
        lock.lock();
        try {
            while(queue.size() == capacity) {
                log.debug("等待加入任务队列..{}", task);
                try {
                    if (nanos <= 0L) {
                        return false;
                    }
                    nanos= fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("加入任务队列 {}", task);
            queue.addLast(task);
            //添加完成，唤醒一个消费者线程继续消费
            emptyWaitSet.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }

    //阻塞添加
    public void put(T task) {
        lock.lock();
        try {
            while(queue.size() == capacity) {
                log.debug("等待加入任务队列..{}", task);
                try {
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("加入任务队列 {}", task);
            queue.addLast(task);
            //添加完成，唤醒一个消费者线程继续消费
            emptyWaitSet.signal();
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }


    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();
        //判断队列是否满
        try {
            if (queue.size() == capacity) {
                rejectPolicy.reject(this, task);
            } else {
                log.debug("加入任务队列 {}", task);
                queue.addLast(task);
                //通知一个消费者
                emptyWaitSet.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}

@Slf4j
class ThreadPool{
    //任务队列
    private BlockingQueue<Runnable> taskQueue;
    private int queueCapacity;
    //线程集合
    private HashSet<Worker> workers = new HashSet<>();
    //核心线程数
    private int coreSize;
    //获取任务的超时时间
    private long timeout;
    //超时时间单位
    private TimeUnit timeUnit;
    //拒绝策略
    private RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool(int queueCapacity, int coreSize, long timeout, TimeUnit timeUnit, RejectPolicy<Runnable> rejectPolicy) {
        this.queueCapacity = queueCapacity;
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.rejectPolicy = rejectPolicy;
        this.taskQueue = new BlockingQueue<>(queueCapacity);
    }

    //执行任务
    public void execute(Runnable task) {
        //当任务数 <= 核心线程数时，提交给 worker对象执行
        //否则，加入任务队列
        synchronized (workers) {
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.debug("新增 worker {}", worker);
                workers.add(worker);
                worker.start();
            } else {
                taskQueue.tryPut(rejectPolicy, task);
            }
        }
    }

    class Worker extends Thread {

        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            //当 task不为空时，执行任务
            //当 task 执行完毕，再接着从任务队列中获取任务并执行
            while (task != null || (task = taskQueue.take(timeout, timeUnit)) != null) {
                try {
                    log.debug("正在执行... {}", task);
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }
            synchronized (workers) {
                log.debug("worker 被移除 {}", this);
                workers.remove(this);
            }
        }
    }

}

