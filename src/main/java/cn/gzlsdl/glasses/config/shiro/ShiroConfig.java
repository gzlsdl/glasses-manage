package cn.gzlsdl.glasses.config.shiro;


import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class ShiroConfig
 * Description:shiro的配置 多realm 认证策略 安全管理器 shiro过滤器
 * @author luxiaobo
 * Created on 2019/6/1
 */
@Configuration
public class ShiroConfig {


    @Bean(name = "getURealm")
    public URealm getURealm(){
        return new URealm();
    }

    @Bean(name = "getPRealm")
    public PRealm getPRealm(){
        return new PRealm();
    }

    @Bean(name = "myModularRealmAuthenticator")
    public ModularRealmAuthenticator myModularRealmAuthenticator(){
        MyModularRealmAuthenticator authenticator = new MyModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return authenticator;
    }


    @Bean(name = "securityManager")
    public SecurityManager securityManager(){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setAuthenticator(myModularRealmAuthenticator());
        List<Realm> realms=new ArrayList<>();
        realms.add(getPRealm());
        realms.add(getURealm());
        manager.setRealms(realms);
        return manager;
    }



    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
//        shiroFilter.setLoginUrl("/login.html");
        shiroFilter.setUnauthorizedUrl("/");

        Map<String, String> filterMap = new LinkedHashMap<>();

        //anon代表放过，shiro不会过滤
        //auth代表需认证，登录认证通过才可以访问
        filterMap.put("/sendVerCode", "anon");
        filterMap.put("/register", "anon");
        filterMap.put("/perfectMsg", "anon");
        filterMap.put("/sendLoginVerCode", "anon");
        filterMap.put("/pLogin", "anon");
        filterMap.put("/uLogin", "anon");
        filterMap.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }





    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }






}
