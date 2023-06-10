package com.threadtest.threadpratice.test;

/**
 * @author xiaoaa
 * @date 2023/2/4 11:49
 **/
public class Room {

    int value = 0;
    static int age = 0;

    public static void push() {
        synchronized (Room.class) {
            age++;
        }
    }
    //push 和 push2 是一样的效果
    public synchronized static void push2() {
        age++;
    }
    public void increment() {
        synchronized (this) {
            value++;
        }
    }

    public void increment2() {
        value++;
    }

    //increment 等价于 add
    public synchronized void add() {
        value++;
    }

    public void decrement() {
        synchronized (this) {
            value--;
        }
    }


}
