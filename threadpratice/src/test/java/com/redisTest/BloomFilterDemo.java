package com.redisTest;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * @author xiaoaa
 * @date 2023/3/21 23:08
 **/
public class BloomFilterDemo {

    public static void main(String[] args) {
        int total = 100_0000;
        test(total, 0);
        test(total, 0.02D);
    }

    private static void test(int total, double fpp) {
        BloomFilter<CharSequence> bf;
        if (fpp>0) {
             bf = BloomFilter
                    .create(Funnels.stringFunnel(Charsets.UTF_8), total, fpp);
        } else {
            bf = BloomFilter
                    .create(Funnels.stringFunnel(Charsets.UTF_8), total);
        }
        //初始化 100W条数据到过滤器中
        for (int i = 0; i < total; i++) {
            bf.put("" + i);
        }
        //判断值是否存在过滤器中
        int count = 0;
        for (int i = 0; i < total + 10000; i++) {
            if (bf.mightContain("" + i)) {
                count++;
            }
        }
        //输出结果为 100W + 100W * 0.03% = 309
        //如果想减少，可以手动设置 fpp
        System.out.println("已匹配数量 " + count);
    }
}
