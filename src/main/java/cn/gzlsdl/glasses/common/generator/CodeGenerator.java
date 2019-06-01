package cn.gzlsdl.glasses.common.generator;


import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;

import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.HashMap;
import java.util.Map;


/**
 * Class CodeGenerator
 * Description:自动生成controller、dao、service（impl）、entity、mapper模块
 * 可以在resources/templates/下查看生成模块，如果不使用自定义模板将用默认模板生成
 * @author luxiaobo
 * Created on 2019/5/17
 */
public class CodeGenerator {


    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir("D:\\IdeaProjects\\glasses-manage\\src\\main\\java");
        gc.setFileOverride(true);
        gc.setActiveRecord(false);// 不需要ActiveRecord特性的请改为false
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setAuthor("xiaobo");// 作者

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setControllerName("%sController");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("zxcvbnm123...");
        dsc.setUrl("jdbc:mysql://111.230.95.206:3306/test?useUnicode=true&characterEncoding=utf8");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setTablePrefix(new String[]{"sys_"});// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(new String[] {"member"}); // 需要生成的表

        strategy.setSuperServiceClass(null);
        strategy.setSuperServiceImplClass(null);
        strategy.setSuperMapperClass(null);

        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("cn.gzlsdl.glasses.modules");
        pc.setController("controller");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setMapper("dao");
        pc.setEntity("entity");
        pc.setXml("mapper");
        mpg.setPackageInfo(pc);

        //自定义模板
         InjectionConfig cfg=new InjectionConfig() {
             @Override
             public void initMap() {
                 Map<String,Object> map=new HashMap<>();
                 map.put("abc",this.getConfig().getGlobalConfig().getAuthor()+"-mp");
                 this.setMap(map);
             }
         };

        TemplateConfig tc=new TemplateConfig();
        tc.setController("templates/controller.java.vm");
        mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();



    }

}
