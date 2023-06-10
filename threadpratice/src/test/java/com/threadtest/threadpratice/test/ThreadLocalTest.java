package com.threadtest.threadpratice.test;

/**
 * @author xiaoaa
 * @date 2023/3/10 22:07
 **/
public class ThreadLocalTest {
    public static void main(String[] args) {
        test2();
        System.out.println(111);
    }

    public static void test2() {
        ThreadLocal<Room> local = new ThreadLocal<>();
        local.set(new Room());
//        local.remove();
        System.out.println(local);
    }
}
