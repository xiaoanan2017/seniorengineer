package com.threadtest.threadpratice.test;

import sun.misc.VM;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xiaoaa
 * @date 2023/2/6 22:20
 **/
public class ThreadPoolExecutorTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10,
                new ThreadPoolExecutor.AbortPolicy());
//        executor.prestartCoreThread()
        Thread.State state = VM.toThreadState(0);
        System.out.println(state);
        Thread.State state1 = VM.toThreadState(1);
        System.out.println(state1);
    }
}
