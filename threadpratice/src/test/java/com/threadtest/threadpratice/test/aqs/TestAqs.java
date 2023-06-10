package com.threadtest.threadpratice.test.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author xiaoaa
 * @date 2023/3/10 16:57
 **/
@Slf4j
public class TestAqs {

    public static void main(String[] args) {
        MyLock lock = new MyLock();
        new Thread(()-> {
            lock.lock();
            //测试锁是否可重入
//            log.debug("locking1...");
//            lock.lock();
//            log.debug("locking2...");
            try {
                log.debug("locking...");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                log.debug("unlocking...");
                lock.unlock();
            }
        },"t1").start();

        new Thread(()-> {
            lock.lock();
            try {
                log.debug("locking...");
            } finally {
                log.debug("unlocking...");
                lock.unlock();
            }
        },"t2").start();

    }
}

//自定义锁-不可重入锁
class MyLock implements Lock {

    //同步器类
    //独占锁
    class MySync extends AbstractQueuedLongSynchronizer{

        @Override
        protected boolean tryAcquire(long arg) {
            if (compareAndSetState(0,1)) {
                //加上了锁
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(long arg) {
            //解锁
            setExclusiveOwnerThread(null);
            //state是 volatile修饰，放下面 防止指令重排序
            setState(0);
            return true;
        }

        //是否持有
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        public Condition newCondition() {
            return new ConditionObject();
        }
    }

    private MySync sync = new MySync();

    //加锁 尝试不会成功，进入等待队列
    @Override
    public void lock() {
        sync.acquire(1);
    }

    //加锁（可打断）
    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    //尝试加锁 ，加一次；加锁不成功，返回false
    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {

        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    //创建条件变量
    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
