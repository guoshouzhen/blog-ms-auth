package top.guoshouzhen.blog.blogmsauth.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description redis操作封装
 * @date 2022/3/9 11:16
 */
@Component
@Slf4j
public class RedisUtil {
    /**
     * 分布式锁key前缀
     */
    private static final String REDIS_LOCK_PRIFIX = "REDIS_LOCK_PRIFIX_";
    /**
     * 锁的有效时间
     */
    private static final long LOCK_EXPIRE = 10 * 1000;
    /**
     * 启动时自动注入
     */
    private static RedisTemplate<String, Object> REDISTEMPLATE;


    public RedisUtil(RedisTemplate<String, Object> redisTemplate){
        RedisUtil.REDISTEMPLATE = redisTemplate;
    }

    /**
     * 获取分布式锁
     * @author shouzhen.guo
     * @date 2022/3/9 14:36
     * @param key key
     * @return boolean
     */
    public static boolean lock(String key){
        String lockKey = REDIS_LOCK_PRIFIX + key;
        return (Boolean) REDISTEMPLATE.execute((RedisCallback<Object>) redisConnection -> {
            long lExpireAt = System.currentTimeMillis() + LOCK_EXPIRE + 1;
            boolean isOk = redisConnection.setNX(lockKey.getBytes(), String.valueOf(lExpireAt).getBytes());
            if(isOk){
                //成功获取锁
                return true;
            }else{
                //等待锁
                byte[] bytes = redisConnection.get(lockKey.getBytes());
                if(Objects.nonNull(bytes) && bytes.length > 0){
                    long lExpireTime = Long.parseLong(new String(bytes));
                    if(lExpireTime < System.currentTimeMillis()){
                        //锁已经过期（此处key过期不会自动删除）
                        //注：此处当前客户端发觉时间戳过期，不能执行删除命令，因为：
                        //c1客户端删除了锁并重新setNX成功获取锁
                        //c2可能也会执行到此处删除并重新获取锁
                        //发生错误：c1、c2都获得了锁
                        long lNewExpireAt = System.currentTimeMillis() + LOCK_EXPIRE + 1;

                        //解决方案：
                        //若客户端发觉锁已过期（持有锁的客户端宕掉了，无法释放锁）：
                        //使用getset命令来设置一个新的过期时间戳，并得到旧的时间戳（注：这个时间戳可能不是过期的，因为有其他客户端可能先一步到此执行了getset操作）
                        //如果这个旧值是已经过期的，则当前客户端就获得了锁
                        //如果没有过期，就继续返回第一步尝试获取锁
                        //这样就防止了重复获取锁或者发生死锁（一直无法获取锁）
                        //getset命令设置执行的key并返回旧值
                        byte[] oldValue = redisConnection.getSet(lockKey.getBytes(), String.valueOf(lNewExpireAt).getBytes());
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
                return false;
            }
        });
    }

    /**
     * 释放锁
     * @author shouzhen.guo
     * @date 2022/3/9 14:51
     * @param key 锁
     */
    public static void releaseLock(String key){
        String lockKey = REDIS_LOCK_PRIFIX + key;
        REDISTEMPLATE.delete(lockKey);
    }

    /**
     * 生成分布式ID
     * @author guoshouzhen
     * @date 2022/4/15 14:28
     * @param key redis key，传入业务相关的名称，可以是table name
     * @return java.lang.Long
     */
    public static Long getIncrementId(String key){
        key = "id:generator:" + key;
        return REDISTEMPLATE.opsForValue().increment(key);
    }

    /**
     * 指定缓存失效时间
     * @author shouzhen.guo
     * @date 2022/3/9 15:07
     * @param key key
     * @param time 有效时间（秒）
     * @return boolean
     */
    public static boolean expire(String key, long time){
        try{
            if(time > 0){
                REDISTEMPLATE.expire(key,time, TimeUnit.SECONDS);
            }
            return true;
        }catch (Exception ex){
            handleException(ex);
            return false;
        }
    }

    /**
     * 根据指定的key获取过期时间
     * @author shouzhen.guo
     * @date 2022/3/9 15:09
     * @param key key
     * @return long
     */
    public static long getExpire(String key){
        return REDISTEMPLATE.getExpire(key);
    }

    /**
     * 判断key是否存在
     * @author shouzhen.guo
     * @date 2022/3/9 15:10
     * @param key key
     * @return boolean
     */
    public static boolean hasKey(String key){
        try{
            return REDISTEMPLATE.hasKey(key);
        }catch (Exception ex){
            handleException(ex);
            return false;
        }
    }

    /**
     * 批量删除key
     * @author shouzhen.guo
     * @date 2022/3/9 15:17
     * @param keys key数组
     */
    public static void delKeys(String...keys){
        if(keys != null && keys.length > 0){
            REDISTEMPLATE.delete(Arrays.asList(keys));
        }
    }

    /**
     * 获取值
     * @author shouzhen.guo
     * @date 2022/3/9 15:20
     * @param key key
     * @return java.lang.Object
     */
    public static Object get(String key){
        return key == null? null : REDISTEMPLATE.opsForValue().get(key);
    }

    /**
     * 添加值
     * @author shouzhen.guo
     * @date 2022/3/9 15:21
     * @param key key
     * @param value value
     * @return boolean
     */
    public static boolean set(String key, Object value){
        try{
            REDISTEMPLATE.opsForValue().set(key, value);
            return true;
        }catch (Exception ex){
            handleException(ex);
            return false;
        }
    }

    /**
     * 添加缓存并设置过期时间
     * @author shouzhen.guo
     * @date 2022/3/9 15:24
     * @param key key
     * @param value value
     * @param time 过期时间
     * @return boolean
     */
    public static boolean set(String key, Object value, long time){
        try{
            if(time > 0){
                REDISTEMPLATE.opsForValue().set(key,value, time, TimeUnit.SECONDS);
            }else{
                set(key,value);
            }
            return true;
        }catch (Exception ex){
            handleException(ex);
            return false;
        }
    }

    /**
     * 递增
     * @author shouzhen.guo
     * @date 2022/3/9 15:26
     * @param key key
     * @param delta 递增因子
     * @return long 递增后的值
     */
    public static long incr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return REDISTEMPLATE.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @author shouzhen.guo
     * @date 2022/3/9 15:28
     * @param key key
     * @param delta 递减因子
     * @return long
     */
    public static long decr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递减因子必须大于0");
        }
        return REDISTEMPLATE.opsForValue().increment(key, -delta);
    }

    /**
     * 根据指定的key获取整个字典
     * @author shouzhen.guo
     * @date 2022/3/9 15:31
     * @param key key
     * @return java.util.Map<java.lang.Object,java.lang.Object>
     */
    public static Map<Object, Object> hmGet(String key){
        return REDISTEMPLATE.opsForHash().entries(key);
    }

    /**
     * 添加一个map到缓存
     * @author shouzhen.guo
     * @date 2022/3/9 15:33
     * @param key key
     * @param map map
     * @return boolean
     */
    public static boolean hmSet(String key, Map<String, Object> map){
        try{
            REDISTEMPLATE.opsForHash().putAll(key, map);
            return true;
        }catch (Exception ex){
            handleException(ex);
            return false;
        }
    }

    /**
     * 添加一个map到缓存并设置过期时间
     * @author shouzhen.guo
     * @date 2022/3/9 15:35
     * @param key key
     * @param map map
     * @param time 过期时间（秒）
     * @return boolean
     */
    public static boolean hmSet(String key, Map<String, Object> map, long time){
        try{
            REDISTEMPLATE.opsForHash().putAll(key, map);
            if(time > 0){
                expire(key,time);
            }
            return true;
        }catch (Exception ex){
            handleException(ex);
            return false;
        }
    }

    /**
     * 向缓存中的字典放入一条记录，如果字典不存在，则创建
     * @author shouzhen.guo
     * @date 2022/3/9 15:37
     * @param key 字典key
     * @param field 记录字段
     * @param value 记录值
     * @return boolean
     */
    public static boolean hmSet(String key, String field, String value){
        try{
            REDISTEMPLATE.opsForHash().put(key, field, value);
            return true;
        }catch (Exception ex){
            handleException(ex);
            return false;
        }
    }

    /**
     * 向缓存中的字典放入一条记录并设置过期时间
     * 如果字典不存在，则创建
     * @author shouzhen.guo
     * @date 2022/3/9 15:40
     * @param key 字典key
     * @param field 记录字段
     * @param value 记录值
     * @param time 过期时间
     * @return boolean
     */
    public static boolean hmSet(String key, String field, String value, long time){
        try{
            REDISTEMPLATE.opsForHash().put(key, field, value);
            if(time > 0){
                expire(key,time);
            }
            return true;
        }catch (Exception ex){
            handleException(ex);
            return false;
        }
    }

    /**
     * 删除指定字典中的记录
     * @author shouzhen.guo
     * @date 2022/3/9 15:42
     * @param key key
     * @param fields 字段
     */
    public static void hmDel(String key, Object... fields){
        REDISTEMPLATE.opsForHash().delete(key,fields);
    }

    /**
     * 判断字典中是否有记录
     * @author shouzhen.guo
     * @date 2022/3/9 15:44
     * @param key key
     * @param field field
     * @return boolean
     */
    public static boolean hmHasKey(String key, String field){
        return REDISTEMPLATE.opsForHash().hasKey(key, field);
    }

    /**
     * 获取指定key对应hash表中字段的值
     * @author shouzhen.guo
     * @date 2022/5/16 23:24
     * @param key 键
     * @param field 字段
     * @return java.lang.Object
     */
    public static Object hmGet(String key, String field){
        if(StringUtil.isNullOrWhiteSpace(key) || StringUtil.isNullOrWhiteSpace(field)){
            return null;
        }
        return REDISTEMPLATE.opsForHash().get(key,field);
    }

    /**
     * 处理redis操作异常
     * @author shouzhen.guo
     * @date 2022/3/9 15:01
     * @param ex 异常
     */
    private static void handleException(Exception ex){
        log.error("Redis执行操作时发生异常，异常信息", ex);
    }
}
