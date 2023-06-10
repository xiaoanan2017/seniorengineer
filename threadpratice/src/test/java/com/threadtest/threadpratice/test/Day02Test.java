package com.threadtest.threadpratice.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author xiaoaa
 * @date 2023/2/5 11:49
 **/
@Slf4j
public class Day02Test {


    static boolean hasCigarette = false;
    static boolean hasTakeout = false;
    static  boolean run = true;


    @Test
    public void test2_wait() throws InterruptedException {

        Object obj = new Object();
        new Thread(()-> {
            synchronized(obj){
                log.debug("t1 正在执行...");
                try {
                    obj.wait();// obj 从 owner 进入到 waitSet中
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("t1恢复运行...");
            }
        }).start();

        new Thread(()-> {
            synchronized(obj){
                log.debug("t2 正在执行...");
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("t2恢复运行...");
            }
        }).start();

        TimeUnit.SECONDS.sleep(2);
        log.debug("准备唤醒 obj上其他正在等待的线程...");
        synchronized (obj){
            obj.notify();//唤醒obj上的一个线程 ，也就是在 monitor对象的 entryList上的任一一个等待线程
            //并不会马上唤醒 obj其他线程，很好理解 因为目前obj的锁还在当前线程手上，只有等 当前线程执行完 synchronized代码块之后，释放掉锁之后才会进行唤醒操作
            log.debug("已经执行唤醒命令...");
            TimeUnit.SECONDS.sleep(2);//sleep不会释放锁 ， wait 会释放锁 ，yield也不会释放锁（强调的是释放时间片）
        }
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void test2_wait2() throws InterruptedException {

        new Thread(()-> {
            synchronized (obj){
                log.debug("有烟没？ {}", hasCigarette);
                //带超时时间的等待
                long timeOut = 1000;
                long beginTime = System.currentTimeMillis();
                long passTime = 0;
                while (!hasCigarette) {
                    long waitTime = System.currentTimeMillis() - passTime;
                    if (waitTime <= 0) {
                        break;
                    }
                    log.debug("没烟歇一会 ******");
                    try {
                        obj.wait(waitTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    passTime = System.currentTimeMillis() - beginTime;
                }
                log.debug("有烟没？ {}", hasCigarette);
                if (hasCigarette) {
                    log.debug("可以开始干活了...");
                }
            }
        },"小南").start();

        new Thread(()-> {
            synchronized (obj){
                log.debug("有外卖没？ {}", hasTakeout);
                int count = 0;
                //带唤醒次数的等待
                while (!hasTakeout && count<3) {
                    log.debug("唤醒了我，但还是没外卖继续等待 ******,这是第 {} 次唤醒", count);
                    count++;
                    try {
                        obj.wait();//如果唤醒A的时候条件不满足，A后面的任务执行不了，
                        //岂不是无法实现任务？ 因此需要在wait之后 进行判断的时候做一个循环，如果此次唤醒条件不满足，则再次wait
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (hasTakeout) {
                    log.debug("可以开始吃饭了...");
                } else {
                    log.debug("外卖一直没来，但我最多只能等待3次，等不了了...");
                }
            }
        },"小美").start();

        TimeUnit.SECONDS.sleep(2);
        for (int i = 0; i < 10; i++) {
            if (i>5) {
                hasTakeout = true;
            }
            new Thread(()-> {
                synchronized(obj){
                    log.debug("外卖到了哦...");
//                obj.notify();//obj只唤醒一条线程的话，可能一直唤醒不到 等外卖的那个人 ,z这种情况称为虚假唤醒 所以可以改为 notifyAll
                    obj.notifyAll();//唤醒全部的之后也会有个问题 ，多个线程竞争的时候（A B），如果唤醒A的时候条件不满足，A后面的任务执行不了，
                    //岂不是无法实现任务？ 因此需要在wait之后 进行判断的时候做一个循环，如果此次唤醒条件不满足，则再次wait
                }
            },"送外卖的").start();
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @Test
    public void test_park() throws InterruptedException {
        Thread t1 = new Thread(()-> {
            log.debug("start...");
            try {
                TimeUnit.SECONDS.sleep(2);
                log.debug("park...");
                LockSupport.park();//检查是否干粮充足， 没有干粮 进入休眠
                log.debug("resume...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1");
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        log.debug("unpark...");
        LockSupport.unpark(t1); //补充一份干粮，并通知正在休息的线程 线程去检查到干粮，恢复运行（吃掉一份干粮)
        TimeUnit.SECONDS.sleep(3);
        log.debug("******");
        Thread t2 = new Thread(()-> {
            log.debug("t2 start...");
            try {
                TimeUnit.SECONDS.sleep(3);
                log.debug("t2 park...");
                LockSupport.park(); //检查干粮 ，发现 干粮 = 1，此时不会阻塞，消耗一份干粮 继续运行
                log.debug("t2 resume...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t2");
        t2.start();
        log.debug("unpark...");
        LockSupport.unpark(t2);//先补充一份干粮 ，此时干粮=1 ，多次执行 unpark 只会最多储备一份干粮
        TimeUnit.SECONDS.sleep(3);
        t2.join();
    }

    @Test
    public void test_deadlock() throws InterruptedException {
        Object a = new Object();
        Object b = new Object();
        new Thread(()-> {
            synchronized (a){
                log.debug("t1获得 a的锁，执行方法a...");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (b) {
                    log.debug("t1获得b的锁，执行方法b...");
                }
            }
        },"t1").start();

        new Thread(()-> {
            synchronized (b){
                log.debug("t2获得 b的锁，执行方法b...");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (a) {
                    log.debug("t2获得a的锁，执行方法a...");
                }
            }
        },"t12").start();

        TimeUnit.SECONDS.sleep(100);
    }

    @Test
    public void test_可见性() throws InterruptedException {

        Thread t = new Thread(() -> {
            int i = 0;
            while (run){
//                log.debug("aaa");
                System.out.println("aa");
                i++;
            }
            log.debug("执行完成...");
        }, "t1");
        t.start();
        TimeUnit.SECONDS.sleep(1);
        run = false;
        log.debug("更改变量...");
        TimeUnit.SECONDS.sleep(10);
    }

    static Object obj = new Object();

    @Test
    public void test_synchronized() throws InterruptedException {
        ClassLayout classLayout = ClassLayout.parseInstance(obj);
        new Thread(()-> {
            log.debug("synchronized之前..");
            log.debug(classLayout.toPrintableSimple());
            synchronized (obj) {
                log.debug("synchronized中..");
                log.debug(classLayout.toPrintableSimple());
            }
            log.debug("synchronized之后..");
            log.debug(classLayout.toPrintableSimple());
        },"t1").start();
        TimeUnit.SECONDS.sleep(2);
    }

    public void method1(){
        synchronized(obj) {
            //同步块A
            method2();
        }
    }

    public void method2(){
        synchronized(obj) {
            //同步块B
            method3();
        }
    }

    public void method3(){
        synchronized(obj) {
            //同步块B

        }
    }

}
