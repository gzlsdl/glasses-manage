package cn.gzlsdl.glasses.config.redis;


import cn.gzlsdl.glasses.common.exception.GlassesException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Class RedisCacheClient
 * Description:redis的操作封装类
 * @author luxiaobo
 * Created on 2019/6/1
 */
@Component
@Slf4j
public class RedisCacheClient {

    @Autowired
    private JedisPoolWrapper jedisPoolWrapper;


    /**
     * Method cache
     *
     * @param key   (类型：String)
     * @param value (类型：String)
     * @author luxiaobo
     * @date 2019/5/20
     * <p>
     * 缓存 可以使value永久
     */
    public void cache(String key, String value) {
        try {
            JedisPool pool = jedisPoolWrapper.getJedisPool();
            if (pool != null) {
                try (Jedis jedis = pool.getResource()) {
                    jedis.select(0);
                    jedis.set(key, value);
                }
            }
        } catch (Exception e) {
            log.error("redis缓存value失败", e);
        }
    }


    /**
     * Method getCacheValue
     *
     * @param key (类型：String)
     * @return String
     * @author luxiaobo
     * @date 2019/5/20
     * <p>
     * 通过redis存储的key值获取对应的value
     */
    public String getCacheValue(String key) {
        String value = null;
        try {
            JedisPool pool = jedisPoolWrapper.getJedisPool();
            if (pool != null) {
                try (Jedis jedis = pool.getResource()) {
                    jedis.select(0);
                    value = jedis.get(key);
                }
            }
        } catch (Exception e) {
            log.error("获取value失败", e);
        }
        return value;
    }


    /**
     * Method cacheNxExpiry
     *
     * @param key    (类型：String)
     * @param value  (类型：String)
     * @param expiry (类型：int)
     * @return long
     * @author luxiaobo
     * @date 2019/5/20
     * <p>
     * 向redis设置key，value，然后key设置过期时间expiry
     */
    public long cacheNxExpiry(String key, String value, int expiry) {
        long result = 0;
        try {
            JedisPool pool = jedisPoolWrapper.getJedisPool();
            if (pool != null) {
                try (Jedis jedis = pool.getResource()) {
                    jedis.select(0);
                    result = jedis.setnx(key, value);
                    jedis.expire(key, expiry);
                }
            }
        } catch (Exception e) {
            log.error("redis设时缓存失败", e);
        }
        return result;
    }


    /**
     * Method deleteKey
     *
     * @param key (类型：String)
     * @author luxiaobo
     * @date 2019/5/20
     * <p>
     * 向redis删除key
     */
    public void deleteKey(String key) {
        JedisPool pool = jedisPoolWrapper.getJedisPool();
        if (pool != null) {
            try (Jedis jedis = pool.getResource()) {
                jedis.select(0);
                try {
                    jedis.del(key);
                } catch (Exception e) {
                    log.error("redis删除key失败", e);
                }
            } catch (Exception e) {
                log.error("redis删除key失败", e);
            }
        }
    }


    /**
     * Method cacheForVerCodeOfIpMaxCount
     *
     *@author luxiaobo
     *@date 2019/5/20
     *
     * 缓存验证码专用，设置过期时间
     * 1 当前验证码未过期  2手机号超过当前验证码发送次数  3 当前ip超过今日发送次数
     *
     * @param phone (类型：String)
     * @param verCode (类型：String)
     * @param time (类型：int)
     * @param ip (类型：String)
     * @return int
     * @throws GlassesException when
     */
    public int cacheForVerCodeOfIpMaxCount(String phone, String verCode,String type, int time, String ip) throws GlassesException {
        try {
            JedisPool pool = jedisPoolWrapper.getJedisPool();
            if (pool != null) {
                try (Jedis jedis = pool.getResource()) {
                    jedis.select(0);
                    String ipKey = type+"limitIp:"+ip;
                    if (ip == null) {
                        return 3;
                    }else {
                        String ipSendCount=jedis.get(ipKey);
                        try {
                            if (ipSendCount!=null&&Integer.parseInt(ipSendCount)>=10){
                                return 3;
                            }
                        }catch (NumberFormatException e){
                            log.error("将ip发送次数（String）转化为int的过程失败");
                            return 3;
                        }
                    }

                    //如果没超时
                    long succ = jedis.setnx(type+phone, verCode);
                    if (succ==0){
                        return 1;
                    }

                    String phoneSendCount=jedis.get(type+"limitPhone:"+phone);
                    try {
                        if (phoneSendCount!=null&&Integer.parseInt(phoneSendCount)>=10){
                            jedis.del(type+phone);
                            return 2;
                        }
                    }catch (NumberFormatException e){
                        log.error("将key的发送次数（String）转化为int的过程失败");
                        jedis.del(type+phone);
                        return 2;
                    }

                    //设置手机验证码过期时间
                    try {
                        jedis.expire(type+phone,time);
                        long var = jedis.incr("reg:limitPhone:"+phone);
                        if (var==1){
                            jedis.expire("reg:limitPhone:"+phone,86400);
                        }
                        jedis.incr(ipKey);
                        if (var==1){
                            jedis.expire(ipKey,86400);
                        }
                    }catch (Exception e){
                        log.error("redis数据缓存失败");
                        return -5;
                    }
                }
            }
        }catch (Exception e){
            log.error("redis设置过期时间失败");
            return -5;
        }
        return 0;
    }


}
