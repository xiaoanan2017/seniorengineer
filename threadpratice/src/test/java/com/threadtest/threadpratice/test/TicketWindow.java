package com.threadtest.threadpratice.test;

import java.util.concurrent.TimeUnit;

/**
 * @author xiaoaa
 * @date 2023/2/4 14:09
 **/
public class TicketWindow {

    private int count;

    public TicketWindow(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public int sell(int amount){
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized(this){
            if (this.count >= amount) {
                this.count -= amount;
                return amount;
            }
            return 0;
        }
    }
}
