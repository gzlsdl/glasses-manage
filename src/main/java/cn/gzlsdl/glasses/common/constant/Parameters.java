package cn.gzlsdl.glasses.common.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Class Parameters
 * Description: 初始化redis的配置参数，从application-dev那里读取
 * @author luxiaobo
 * Created on 2019/6/1
 */
@Component
@Data
public class Parameters {

    /*****redis config start*******/
    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;
    @Value("${redis.password}")
    private String password;
    @Value("${redis.auth}")
    private String redisAuth;
    @Value("${redis.max-idle}")
    private int redisMaxTotal;
    @Value("${redis.max-total}")
    private int redisMaxIdle;
    @Value("${redis.max-wait-millis}")
    private int redisMaxWaitMillis;

    /*****redis config end*******/




}
