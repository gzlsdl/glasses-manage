package cn.gzlsdl.glasses;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan(basePackages = "cn.gzlsdl.glasses.modules.dao")
public class GlassesApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlassesApplication.class, args);
    }

}
