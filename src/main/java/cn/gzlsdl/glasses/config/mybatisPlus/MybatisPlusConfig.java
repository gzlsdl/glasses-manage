package cn.gzlsdl.glasses.config.mybatisPlus;




import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * Class MybatisPlusConfig
 * Description: mybatis-plus配置 注释掉的为  sql语句输出控制台
 * @author luxiaobo
 * Created on 2019/6/1
 */
@Configuration
public class MybatisPlusConfig {

    //plus的性能优化
//    @Bean
//    public PerformanceInterceptor performanceInterceptor(){
//        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
//        /*<!-- SQL 执行性能分析，开发环境使用，线上不推荐。 maxTime 指的是 sql 最大执行时长 -->*/
//        performanceInterceptor.setMaxTime(1000);
//        /*<!--SQL是否格式化 默认false-->*/
//        performanceInterceptor.setFormat(true);
//        return performanceInterceptor;
//    }


    //Mybatis-plus的分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }




}
