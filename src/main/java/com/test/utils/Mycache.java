package com.test.utils;

import org.apache.ibatis.cache.Cache;
import org.mybatis.caches.memcached.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Created by Seed on 2017/9/13.
 */
public final class Mycache implements Cache{


    private static MemcachedUtils memcachedUtils=new MemcachedUtils();

    private final ReadWriteLock readWriteLock = new DummyReadWriteLock();

    private final String id;

   // private static Set<String> keys=new HashSet<>();


    public Mycache(final String id) {
        this.id=id;
    }


    public String getId() {
        return this.id;
    }

    public void putObject(Object key, Object value) {
        synchronized (Mycache.class){
            if(null==key)
                throw new RuntimeException("key 不能为空");
            memcachedUtils.putObject(key,value,this.id);

        }

    }

    public Object getObject(Object key) {
        return memcachedUtils.get(key);
    }

    public Object removeObject(Object key) {
        memcachedUtils.remove(key);
        return null;
    }

    public void clear() {
        memcachedUtils.clear(this.id);
    }

    public int getSize() {
        return 0; //keys.size();
    }

    public ReadWriteLock getReadWriteLock() {
        return null;
    }



}


