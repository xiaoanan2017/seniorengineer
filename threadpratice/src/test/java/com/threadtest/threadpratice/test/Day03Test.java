package com.threadtest.threadpratice.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/**
 * @author xiaoaa
 * @date 2023/2/6 15:43
 **/
@Slf4j
public class Day03Test {

    public static void main(String[] args) throws InterruptedException {
        //等待4s
        Room one = new Room();
        ClassLayout test = ClassLayout.parseInstance(one);
        log.debug("程序刚启动的..");
        log.debug(test.toPrintableSimple());
        TimeUnit.SECONDS.sleep(4);
        Room obj = new Room();
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
    }

    static Object obj = new Object();

    @Test
    public void test_锁消除() {
        int i = 0;
        synchronized(obj) {
            i++;
        }
    }
}
