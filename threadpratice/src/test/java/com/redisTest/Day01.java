package com.redisTest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xiaoaa
 * @date 2023/3/21 22:24
 **/
public class Day01 {

    //    LRUCache<Integer, Integer> lruCache = new LRUCache<>(10);


    public static void main(String[] args) {
        int cacheSize = 7;
        int v = (int)Math.ceil(cacheSize/0.75)+1;
        System.out.println(v);

        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, 1);
        map.put(2, 1);
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            map.remove(entry.getKey());
        }

    }


}
class LRUCache<K,V> extends LinkedHashMap<K,V> {

    private final int CACHE_SIZE;

    public LRUCache(int cacheSize) {
        //true 表示让 LinkedHashMap 按照访问顺序来进行排序，最近访问的放在头部，最老访问的放在尾部
        super((int)Math.ceil(cacheSize/0.75)+1, 0.75f, true);
        this.CACHE_SIZE = cacheSize;
    }


    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        //map中的数据量大于指定的缓存个数的时候，就自动删除最老的数据
        return size() > CACHE_SIZE;
    }

    public int getCACHE_SIZE() {
        return CACHE_SIZE;
    }

}
