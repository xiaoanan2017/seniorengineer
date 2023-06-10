package com.threadtest.threadpratice.test;

import java.lang.reflect.Constructor;

/**
 * @author xiaoaa
 * @date 2023/2/28 21:38
 **/
public class SingletonTest {
    public static void main(String[] args) throws Exception {
        Constructor<? extends EnumSingleton> constructor = EnumSingleton.INSTANCE.getClass()
                .getConstructor();
        constructor.setAccessible(true);
        EnumSingleton secondObj = constructor.newInstance(null);
        System.out.println(secondObj);
    }
}

/**
 * 懒汉式，线程不安全
 * 是否延迟加载：是
 * 是否线程安全： 否
 */
class SingleTonOne{

    private static SingleTonOne instance ;

    public SingleTonOne() {
    }

    public static SingleTonOne getInstance() {
        if (instance == null) {
            instance = new SingleTonOne();
        }
        return instance;
    }
}

/**
 * 懒汉式，线程安全
 * 是否延迟加载：是
 * 是否线程安全： 是
 */
class SingleTonTwo {
    private static SingleTonTwo instance ;

    public SingleTonTwo() {
    }

    public static synchronized SingleTonTwo getInstance() {
        if (instance == null) {
            instance = new SingleTonTwo();
        }
        return instance;
    }
}

/**
 *  * 饿汉式，线程安全
 *  * 是否延迟加载：否
 *  * 是否线程安全： 是
 */
class HungrySingleTon{
    //只要类被加载 instance就会被实例化，因此没有延迟加载的特性
    private static HungrySingleTon instance = new HungrySingleTon();

    public HungrySingleTon() {
    }
    public static HungrySingleTon getInstance() {
        return instance;
    }
}

/**
 * 双重锁/双重检验所
 * 是否延迟加载：是
 * 是否线程安全：是
 */
class DoubleCheckSingleTon{

    //volatile 保证变量的可见性，指示JVM，这个变量是共享且不稳定的，每次使用它时都需要到主存中进行读取
    //volatile有两个作用：1.保证变量的可见性 2.禁止jvm的指令重排序
    private volatile static DoubleCheckSingleTon instance = null;

    public DoubleCheckSingleTon() {
    }

    public static DoubleCheckSingleTon getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized(DoubleCheckSingleTon.class) {
            if (instance == null) {
                instance = new DoubleCheckSingleTon();
                /** instance = new DoubleCheckSingleTon();
                 * 这块代码实际是分为三步：
                 * 1. 为 instance 对象分配内存空间
                 * 2. 初始化 instance
                 * 3.将 instance指向分配的内存地址
                 *
                 * 由于JVM具有指令重排的特性，执行顺序可能是 1-> 3 -> 2 。
                 * 指令重排在单线程下不会出现问题，
                 * 但是在多线程环境下会导致一个线程获得还没有初始化的实例
                 */
            }
        }
        return instance;
    }
}

/**
 * 登记式/静态内部类
 * 是否延迟加载：是
 * 是否多线程安全：是
 */
class StaticInnerClass {

   private static class SingleTonHolder{
       //StaticInnerClass 被加载的时候， instance不会被实例化 ，
       // 只有显示调用 getInstance 时才会显式装载  SingleTonHolder，此时才会实例化，因此也实现了延迟加载
       private static final StaticInnerClass INSTANCE = new StaticInnerClass();
   }

    public StaticInnerClass() {
    }

    public static StaticInnerClass getInstance(){
        return SingleTonHolder.INSTANCE;
    }
}

/**
 * 枚举
 *
 */
enum EnumSingleton{
    INSTANCE;
}