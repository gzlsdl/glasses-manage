package cn.gzlsdl.glasses.config.interceptor;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Class MyWebAppConfigurer
 * Description: web配置，将自定义的ip拦截器装入bean工厂，再进行跨域处理
 * @author luxiaobo
 * Created on 2019/6/1
 */
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurationSupport {

    //将拦截器打包成bean装入容器
    @Bean
    public HandlerInterceptor getMyInterceptor(){
        return new IpInterceptor();
    }

    //设置ip拦截
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getMyInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }


    //跨域处理
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE")
                .allowCredentials(true);
    }
}
