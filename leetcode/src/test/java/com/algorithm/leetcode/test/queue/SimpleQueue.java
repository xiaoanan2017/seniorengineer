package com.algorithm.leetcode.test.queue;

import com.algorithm.leetcode.test.task.CustomerThreadPool;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author xiaoaa
 * @date 2023/5/6 22:01
 *  简单队列实现
 *  使用 p_start 来取值
 *  删除元素时，p_start ++ ,实际上未删除元素
 **/
@Slf4j
public class SimpleQueue<T> {

    //store elements
    private List<T> data = new ArrayList<T>();

    //element pointer
    private int p_start = 0;

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private ReentrantReadWriteLock.ReadLock readLock = lock.readLock();

    private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public SimpleQueue() {

    }
    //入队
    public boolean enQueue(T element) {
        try {
            writeLock.lock();
            data.add(element);
            log.info("入队元素 {}", element);
            sleep(100);
        } finally {
            writeLock.unlock();
        }
        return true;
    }

    public boolean deQueue() {
        try {
            writeLock.lock();
            if (isEmpty()) {
                return false;
            }
            p_start++;
            log.info("执行出队动作");
            sleep(100);
        } finally {
            writeLock.unlock();
        }
        return true;
    }

    public T front() {
        T value = null;
        try {
            readLock.lock();
            value =  data.get(p_start);
            log.info("获取队尾元素 {}", value);
        } finally {
            readLock.unlock();
        }
        return value;
    }

    public boolean isEmpty() {
        return p_start >= data.size();
    }

    private void sleep(int sleep){
        try {
            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class SimpleQueueTest {

        public static void main(String[] args) {
            SimpleQueue<Integer> simpleQueue = new SimpleQueue<>();
            int count = 500;
//            for (int i = 0; i < count; i++) {
//                CompletableFuture.runAsync(() -> {
//                    Long id = Thread.currentThread().getId();
//                    //入列
//                    simpleQueue.enQueue(id.intValue());
//                }, CustomerThreadPool.TaskPool())
//                .thenRunAsync(() -> {
//                    //取出队尾数据
//                    simpleQueue.front();
//                }, CustomerThreadPool.TaskPool())
//                .thenRunAsync(() -> {
//                    //出列
//                    simpleQueue.deQueue();
//                }, CustomerThreadPool.TaskPool());
//
//            }

            ExecutorService executorService = CustomerThreadPool.TaskPool(100,100,10000);
            for (int i = 0; i < count; i++) {
                executorService.submit(()-> {
                    Long id = Thread.currentThread().getId();
                    //入列
                    simpleQueue.enQueue(id.intValue());
                });
                executorService.submit(()-> {
                    //展示队尾数据
                    simpleQueue.front();
                });
                executorService.submit(()->{
                    //出列
                    simpleQueue.deQueue();
                });
            }
        }
    }
}
