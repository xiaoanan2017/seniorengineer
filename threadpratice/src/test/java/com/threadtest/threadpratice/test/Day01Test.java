package com.threadtest.threadpratice.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiaoaa
 * @date 2023/2/3 11:37
 **/
@Slf4j
public class Day01Test {

    static int count = 0;

    Random random = new Random();

    @Test
    public void test_线程创建() throws InterruptedException {
        //使用最简单的thread 创建线程
        Thread t1 = new Thread("t1"){
            @Override
            public void run() {
                log.debug("t1");
            }
        };
        t1.start();
        //使用runnable对象
        Runnable task = new Runnable() {
            @Override
            public void run() {
                //要执行的任务
            }
        };
        Thread thread = new Thread(() -> {
            log.debug("task1");
        },"name");

        Runnable task1 = () -> log.debug("task1");

        Thread t2 = new Thread(task1);
        t2.start();
        //futuretask 配合thread
        FutureTask<Integer> task2 = new FutureTask<>(() -> {
            log.debug("task2");
//            TimeUnit.SECONDS.sleep(2);
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                log.error("线程未打断....");
            }
            log.debug("task2 done");
            return 100;
        });
        Thread t3 = new Thread(task2,"t3");
        t3.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("t3的打断标记为：" + t3.isInterrupted());
        t3.interrupt();
        System.out.println("t3的打断标记为：" + t3.isInterrupted());
        try {
            Integer result = task2.get();
            System.out.println("task2的执行结果为 " + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Thread.State state = t3.getState();
        System.out.println(state);
        FutureTask<Integer> futureTask = new FutureTask<>(() -> {
            //执行的方法体 ，并返回值
            return 100;
        });
        new Thread(futureTask, "线程名").start();
        try {
            Integer result = futureTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_join() throws InterruptedException {
        AtomicInteger count = new AtomicInteger(1);
        StopWatch stopWatch = new StopWatch("stopwatch");
        stopWatch.start("计算方法执行时间");
        Thread t1 = new Thread(() -> {
            log.info("t1....正在执行");
            try {
                TimeUnit.SECONDS.sleep(1);
                count.set(100);
                log.info("t1....执行完成");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            log.info("t2....正在执行");
            try {
                TimeUnit.SECONDS.sleep(10);
                count.set(200);
                log.info("t2....执行完成");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
//        t2.setDaemon(true);
        t1.start();
        t2.start();
        t1.join();
//        t2.join();
        TimeUnit.SECONDS.sleep(13);
        System.out.println("最终结果为："  + count.get());
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        System.out.println(stopWatch.prettyPrint());
        TimeUnit.SECONDS.sleep(3);
    }

    @Test
    public void test_synchronized() throws InterruptedException {
        Object room = new Object();
        AtomicInteger num = new AtomicInteger();
        System.out.println(num.get());
        Thread t1 = new Thread(() -> {
            for (int i=0; i<5000; i++) {
                synchronized(room) {
                    count++;
                }
                num.incrementAndGet();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i=0; i<5000; i++) {
                synchronized(room) {
                    count--;
                }
                num.decrementAndGet();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("最终结果为：" + count);
        System.out.println("最终结果为：" + num.get());
    }

    @Test
    public void test_synchronized2() throws InterruptedException {
        Room romm = new Room();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                romm.increment2();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                romm.decrement();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("最终结果为：" + romm.value);
    }


    @Test
    public void test_sellTicket(){
        TicketWindow ticketWindow = new TicketWindow(100);
        List<Thread> list = new ArrayList<>();
        //用来存储卖出去多少张票
        List<Integer> sellCount = new Vector<>();
//        List<Integer> sellCount = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Thread t = new Thread(() -> {
                int count = ticketWindow.sell(random.nextInt(5) + 1);
                sellCount.add(count);
            });
            list.add(t);
        }
        list.forEach(t-> t.start());
        list.forEach(t-> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        int sum = sellCount.stream().mapToInt(Integer::intValue).sum();
        System.out.println("总共卖出去的票数：" + sum);
        System.out.println("剩余票数： " + ticketWindow.getCount());
    }
}
