package com.spiderdt.common.notice.common;

import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Map;

/**
 * Created by fivebit on 2017/5/19.
 */
public class JredisClient {
    //添加不同的头部，区分不同的生产线的key
    @Value("#{appProperties['redis.prefix']}")
    public static String prefix="notice_api";


    /**
     * 设置缓存
     * @param key
     * @param value
     * @param time 超时时间 单位是秒
     */
    public static void addString(String key ,String value,long time) {
        Jedis redisClient = null;
        try {
            redisClient = Jredis.getJedis();
            value = isNullOrEmpty(value) ? "" : value;
            //EX: seconds PX:milliseconds
            // NX:Only set the key if it does not already exist. XX -- Only set the key if it already exist.
            redisClient.set(prefix+"::"+key, value,"NX","EX",time);
        } catch (Exception e) {
            // 销毁对象
            Jredis.returnRes(redisClient);
        } finally {
            // 还原到连接池
            Jredis.returnRes(redisClient);
        }
    }
    public static void addString(String key ,String value) {
        Jedis redisClient = null;
        try {
            redisClient = Jredis.getJedis();
            value = isNullOrEmpty(value) ? "" : value;
            redisClient.set(prefix+"::"+key, value);
        } catch (Exception e) {
            // 销毁对象
            Jredis.returnRes(redisClient);
        } finally {
            // 还原到连接池
            Jredis.returnRes(redisClient);
        }
    }
    public static String getString(String key){
        Jedis redisClient = null;
        try {
            redisClient = Jredis.getJedis();
            return redisClient.get(prefix+"::"+key);
        } catch (Exception e) {
            // 销毁对象
            Jredis.returnRes(redisClient);
        }  finally {
        // 还原到连接池
            Jredis.returnRes(redisClient);
        }
        return null;
    }
    public static void setDataToRedis(String key, String field, String value) {
        Jedis redisClient = null;
        try {
            redisClient = Jredis.getJedis();
            redisClient.hset(prefix+"::"+key, field, value);
        } catch (Exception e) { // 销毁对象
            Jredis.returnRes(redisClient,true);
        } finally { // 还原到连接池
            Jredis.returnRes(redisClient);
        }
    }
    public static Map<String, String> getMapData(String key) {
        Map<String, String> dataMap = null;
        Jedis redisClient = null;
        try {
            redisClient = Jredis.getJedis();
            dataMap = redisClient.hgetAll(prefix+"::"+key);
        } catch (Exception e) { // 销毁对象
            Jredis.returnRes(redisClient,true);
        } finally { // 还原到连接池
            Jredis.returnRes(redisClient);
        }
        return dataMap;
    }
    public static long deleteData(String key) {
        long result = 0;
        Jedis redisClient = null;
        try {
            redisClient = Jredis.getJedis();
            result = redisClient.del(key);
        } catch (Exception e) { // 销毁对象
            Jredis.returnRes(redisClient,true);
        } finally { // 还原到连接池
            Jredis.returnRes(redisClient);
        }
        return result;
    }
    public static String getData(String key, String field) {
        String data = null;
        Jedis redisClient = null;
        try {
            redisClient = Jredis.getJedis();
            data = redisClient.hget(key, field);
        } catch (Exception e) { // 销毁对象
            Jredis.returnRes(redisClient,true);
        } finally { // 还原到连接池
            Jredis.returnRes(redisClient);
        }
        return data;
    }
    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null)
            return true;

        if (obj instanceof CharSequence)
            return ((CharSequence) obj).length() == 0;

        if (obj instanceof Collection)
            return ((Collection) obj).isEmpty();

        if (obj instanceof Map)
            return ((Map) obj).isEmpty();

        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isNullOrEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }
    public static Object unserialize( byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {


        }
        return null;
    }
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {


        }
        return null;
    }
}
