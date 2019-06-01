package cn.gzlsdl.glasses.config.redis;


import cn.gzlsdl.glasses.common.constant.Parameters;
import cn.gzlsdl.glasses.common.exception.GlassesException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;


/**
 * Class JedisPoolWrapper
 * Description:redis连接池配置
 * @author luxiaobo
 * Created on 2019/5/20
 */
@Component
@Slf4j
public class JedisPoolWrapper {

    private JedisPool jedisPool=null;     //redis连接池

    @Autowired
    private Parameters parameters;    //参数

    /**
     * Method init
     *
     *@author luxiaobo
     *@date 2019/5/20
     *初始化redis，配置redis，参数在application.yml看
     * @throws GlassesException when
     */
    @PostConstruct
    public void init() throws GlassesException {
        try {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxWaitMillis(parameters.getRedisMaxWaitMillis());
            jedisPoolConfig.setMaxIdle(parameters.getRedisMaxIdle());
            jedisPoolConfig.setMaxTotal(parameters.getRedisMaxTotal());
            jedisPool=new JedisPool(jedisPoolConfig,parameters.getRedisHost(),parameters.getRedisPort(),2000,parameters.getPassword());
        }catch (Exception e){
            log.error("初始化redis失败");
            throw new GlassesException("初始化redis失败");
        }
    }

    public  JedisPool getJedisPool(){
        return jedisPool;
    }
}
