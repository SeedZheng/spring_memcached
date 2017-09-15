package com.test.utils;

import com.danga.MemCached.MemCachedClient;
import org.apache.log4j.Logger;
import org.mybatis.caches.memcached.StringUtils;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.*;

public class MemcachedUtils
{  
    private static final Logger logger = Logger.getLogger(MemcachedUtils.class);
    private static MemCachedClient cachedClient;
    private static final String prefix="_mybatis_";


    private static String toKeyString(Object obj){
        if(null==obj)
            return "";
        String key=prefix+ StringUtils.sha1Hex(obj.toString());
        return key;
    }

  
    public MemcachedUtils()
    {
        synchronized (MemcachedUtils.class) {
            if (cachedClient == null)
                cachedClient = new MemCachedClient("memcachedPool");
            logger.info("cachedClient 初始化成功");
            cachedClient.flushAll();
           // System.out.println("cachedClient 初始化成功");
        }
    }


    /**
     *
     * @param key
     * @param value
     * @param id  namespace的ID
     */
    public static void putObject(Object key,Object value,String id){
        String keyString=toKeyString(key);
        String groupKeyString=toKeyString(id);
        set(keyString,value);//存储
        Group group=getGroup(groupKeyString);
        //group中的对象
        Set<String> groupValue;
        if(group.getValue()==null){
            groupValue=new HashSet<>();
            groupValue.add(keyString);  //将已经存储入缓存的对象的KEY存入那么namespace对应的组中
        }else{
            groupValue=group.getValue();
            groupValue.add(keyString);
        }
        set(groupKeyString,groupValue);
    }

    /**
     *
     * @param id namespace的ID
     */
    public static void clear(String id){
        String groupKeyString=toKeyString(id);
        Group group=getGroup(groupKeyString);
        if(group.getValue()!=null){
            Set<String> keySet=group.getValue();
            for(String k:keySet){
                delete(k);
            }
            delete(groupKeyString);
        }
    }

    public static void remove(Object key){
        String keyString =toKeyString(key);
        delete(keyString);
    }

    public static Object get(Object key){
        String keyString=toKeyString(key);
        Object o=get(keyString);
        return o;
    }

    private static Group getGroup(String groupKeyString){
        Group group;
        Object tempValue=get(groupKeyString);
        if(null==tempValue){
            //还不存在该组信息
            group=new Group(groupKeyString);
        }else{
            Set<String> set= (Set<String>) tempValue;
            group=new Group(groupKeyString,set);
        }
        return group;
    }


    /**
     * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对。
     *
     * @param key
     *            键
     * @param value
     *            值
     * @return
     */
    private static boolean add(String key, Object value)
    {
        return addExp(key, value, null);
    }

    /**
     * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对。
     *
     * @param key
     *            键
     * @param value
     *            值
     * @param expire
     *            过期时间 New Date(1000*10)：十秒后过期
     * @return
     */
    private static boolean add(String key, Object value, Date expire)
    {
        return addExp(key, value, expire);
    }


    /**
     * 仅当键已经存在时，replace 命令才会替换缓存中的键，如果不存在，则什么也不会做
     *
     * @param key 键
     * @param value 值
     * @return
     */
    private static boolean replace(String key, Object value)
    {
        return replaceExp(key, value, null);
    }

    /**
     * 仅当键已经存在时，replace 命令才会替换缓存中的键，如果不存在，则什么也不会做
     *
     * @param key 键
     * @param value 值
     * @param expire 过期时间 New Date(1000*10)：十秒后过期
     * @return
     */
    private static boolean replace(String key, Object value, Date expire)
    {
        return replaceExp(key, value, expire);
    }



    /**
     * get 命令用于检索与之前添加的键值对相关的值。
     *
     * @param key
     *            键
     * @return
     */
    public static Object get(String key)
    {
        Object obj = null;
        try
        {
            obj = cachedClient.get(key);
        }
        catch (Exception e)
        {
            MemcachedLog.writeLog("Memcached get方法报错，key值：" + key + "\r\n" + exceptionWrite(e));
        }
        return obj;
    }

    /**
     * 删除 memcached 中的任何现有值。
     *
     * @param key
     *            键
     * @return
     */
    private static boolean delete(String key)
    {
        return deleteExp(key, null);
    }

    /**
     * 删除 memcached 中的任何现有值。
     *
     * @param key
     *            键
     * @param expire
     *            过期时间 New Date(1000*10)：十秒后过期
     * @return
     */
    private static boolean delete(String key, Date expire)
    {
        return deleteExp(key, expire);
    }

    /**
     * 清理缓存中的所有键/值对
     *
     * @return
     */
    private static boolean flushAll()
    {
        boolean flag = false;
        try
        {
            flag = cachedClient.flushAll();
        }
        catch (Exception e)
        {
            MemcachedLog.writeLog("Memcached flashAll方法报错\r\n" + exceptionWrite(e));
        }
        return flag;
    }

    /**
     *   flush_all 实际上没有立即释放项目所占用的内存，而是在随后陆续有新的项目被储存时执行（这是由memcached的懒惰检测和删除机制决定的）
     */
    private static void getAll(){
        Map<String, Map<String,String>> slabs=cachedClient.statsItems();
        Iterator<String> keySet =slabs.keySet().iterator();
        Map<String,String> secondMap=new HashMap<>();
        List<String> valueList=new ArrayList<>();

        while(keySet.hasNext()){
            String key=keySet.next();
            secondMap=slabs.get(key);
            Iterator i=secondMap.keySet().iterator();
            while(i.hasNext()){
                Object key_2=i.next();
                String value=secondMap.get(key_2);
                valueList.add(value);
            }
        }

        for(String s:valueList){
            System.out.print("值为："+s);
            System.out.println();
        }


    }




    /** 
     * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @return 
     */
    private static boolean set(String key, Object value)
    {  
        return setExp(key, value, null);  
    }  
  
    /** 
     * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换,如果不存在，则会新增
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @param expire 
     *            过期时间 New Date(1000*10)：十秒后过期 
     * @return 
     */
    private static boolean set(String key, Object value, Date expire)
    {  
        return setExp(key, value, expire);  
    }  
  
    /** 
     * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换,如果不存在，则会新增
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @param expire 
     *            过期时间 New Date(1000*10)：十秒后过期 
     * @return 
     */  
    private static boolean setExp(String key, Object value, Date expire)  
    {  
        boolean flag = false;  
        try  
        {  
            flag = cachedClient.set(key, value, expire);  
        }  
        catch (Exception e)  
        {  
            // 记录Memcached日志  
            MemcachedLog.writeLog("Memcached set方法报错，key值：" + key + "\r\n" + exceptionWrite(e));  
        }  
        return flag;  
    }  
  

  
    /** 
     * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @param expire 
     *            过期时间 New Date(1000*10)：十秒后过期 
     * @return 
     */  
    private static boolean addExp(String key, Object value, Date expire)  
    {  
        boolean flag = false;  
        try  
        {  
            flag = cachedClient.add(key, value, expire);  
        }  
        catch (Exception e)  
        {  
            // 记录Memcached日志  
            MemcachedLog.writeLog("Memcached add方法报错，key值：" + key + "\r\n" + exceptionWrite(e));  
        }  
        return flag;  
    }  
  

    /** 
     * 仅当键已经存在时，replace 命令才会替换缓存中的键。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @param expire 
     *            过期时间 New Date(1000*10)：十秒后过期 
     * @return 
     */  
    private static boolean replaceExp(String key, Object value, Date expire)  
    {  
        boolean flag = false;  
        try  
        {  
            flag = cachedClient.replace(key, value, expire);  
        }  
        catch (Exception e)  
        {  
            MemcachedLog.writeLog("Memcached replace方法报错，key值：" + key + "\r\n" + exceptionWrite(e));  
        }  
        return flag;  
    }  

    /** 
     * 删除 memcached 中的任何现有值。 
     *  
     * @param key 
     *            键 
     * @param expire 
     *            过期时间 New Date(1000*10)：十秒后过期 
     * @return 
     */  
    private static boolean deleteExp(String key, Date expire)  
    {  
        boolean flag = false;  
        try  
        {  
            flag = cachedClient.delete(key, expire);  
        }  
        catch (Exception e)  
        {  
            MemcachedLog.writeLog("Memcached delete方法报错，key值：" + key + "\r\n" + exceptionWrite(e));  
        }  
        return flag;  
    }  
  

    /** 
     * 返回异常栈信息，String类型 
     *  
     * @param e 
     * @return 
     */  
    private static String exceptionWrite(Exception e)  
    {  
        StringWriter sw = new StringWriter();  
        PrintWriter pw = new PrintWriter(sw);  
        e.printStackTrace(pw);  
        pw.flush();  
        return sw.toString();  
    }



  
    /** 
     *  
     * @ClassName: MemcachedLog 
     * @Description: Memcached日志记录 
     *  
     */  
    private static class MemcachedLog  
    {  
        private final static String MEMCACHED_LOG = "D:\\memcached.log";  
        private final static String LINUX_MEMCACHED_LOG = "/usr/local/logs/memcached.log";  
        private static FileWriter fileWriter;  
        private static BufferedWriter logWrite;  
        // 获取PID，可以找到对应的JVM进程  
        private final static RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();  
        private final static String PID = runtime.getName();  
  
        /** 
         * 初始化写入流 
         */  
        static  
        {  
            try  
            {  
                String osName = System.getProperty("os.name");  
                if (osName.indexOf("Windows") == -1)  
                {  
                    fileWriter = new FileWriter(MEMCACHED_LOG, true);  
                }  
                else  
                {  
                    fileWriter = new FileWriter(LINUX_MEMCACHED_LOG, true);  
                }  
                logWrite = new BufferedWriter(fileWriter);  
            }  
            catch (IOException e)  
            {  
                logger.error("memcached 日志初始化失败", e);  
                closeLogStream();  
            }  
        }  
  
        /** 
         * 写入日志信息 
         *  
         * @param content 
         *            日志内容 
         */  
        public static void writeLog(String content)  
        {  
            try  
            {  
                logWrite.write("[" + PID + "] " + "- ["  
                        + new SimpleDateFormat("yyyy年-MM月-dd日 hh时:mm分:ss秒").format(new Date().getTime()) + "]\r\n"  
                        + content);  
                logWrite.newLine();  
                logWrite.flush();  
            }  
            catch (IOException e)  
            {  
                logger.error("memcached 写入日志信息失败", e);  
            }  
        }  
  
        /** 
         * 关闭流 
         */  
        private static void closeLogStream()  
        {  
            try  
            {  
                fileWriter.close();  
                logWrite.close();  
            }  
            catch (IOException e)  
            {  
                logger.error("memcached 日志对象关闭失败", e);  
            }  
        }  
    }
    
}

class Group{
    private String key;
    private Set<String> value;

    public Group(String key) {
        this.key = key;
    }

    public Group(String key, Set<String> value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        synchronized (Group.class){
            this.key = key;
        }
    }

    public Set<String> getValue() {
        return value;
    }

    public void setValue(Set<String> value) {
        synchronized (Group.class) {
            this.value = value;
        }
    }
}