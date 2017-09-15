package com.test.utils;

import org.apache.ibatis.cache.Cache;
import org.mybatis.caches.memcached.StringUtils;

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
            String keyString=toKeyString(key);
            //Object ret;
           // if(keys.contains(keyString)){
                //已经存在，覆盖前面的数据
                memcachedUtils.set(keyString,value);
          //  }else{
           //     keys.add(keyString);
             //   memcachedUtils.add(keyString,value);
           // }
        }

    }

    public Object getObject(Object key) {
        String k=toKeyString(key);
        //if(keys.contains(k)){
           return memcachedUtils.get(k);
      //  }else
       //     return null;
    }

    public Object removeObject(Object key) {
        String keyString= toKeyString(key);
        Object obj=getObject(keyString);
        boolean isDelete=false;
        if(null!=obj)
            isDelete=memcachedUtils.delete(key+"");
        if(isDelete){
          //  keys.remove(keyString);
            return obj;
        }else
            return null;
    }

    public void clear() {
        memcachedUtils.flushAll();
    }

    public int getSize() {
        return 0; //keys.size();
    }

    public ReadWriteLock getReadWriteLock() {
        return null;
    }


    private String toKeyString(Object obj){
        if(null==obj)
            return "";
        String key=StringUtils.sha1Hex(obj.toString());
        return key;
    }

}
