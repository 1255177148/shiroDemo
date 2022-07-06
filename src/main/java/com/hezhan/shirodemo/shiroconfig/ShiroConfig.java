package com.hezhan.shirodemo.shiroconfig;

import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Resource
    private MyHashedCredentialsMatcher myHashedCredentialsMatcher;

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/api/login");
        Map<String, String> filterMap = new HashMap<>();
        // authc:所有url都必须认证通过才可以访问，anon:所有url都可以匿名访问
        filterMap.put("/api/*", "anon");
//        filterMap.put("/permission/*", "anon");
        filterMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public Authorizer authorizer(){
        return new MyRealm(myHashedCredentialsMatcher);
    }

//    /**
//     * 这里放在MyRealm中用构造方法注册自己的登录校验器，
//     * 现在这个写法是不使用springboot的写法，目前注释掉
//     * @return
//     */
//    @Bean
//    public MyHashedCredentialsMatcher myHashedCredentialsMatcher(){
//        MyHashedCredentialsMatcher myHashedCredentialsMatcher = new MyHashedCredentialsMatcher();
//        myHashedCredentialsMatcher.setHashAlgorithmName("SHA-1");// 加密算法的名称
//        myHashedCredentialsMatcher.setHashIterations(2);// 加密的次数
//        myHashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);// 是否储存为16进制
//        return myHashedCredentialsMatcher;
//    }


//    /**
//     * 这里的写法是不使用springboot的写法，目前注释掉，
//     * springboot的写法是直接在MyRealm的类上加一个注释，被spring作为bean自动装配进去
//     * @return
//     */
//    @Bean
//    public MyRealm myRealm(){
//        MyRealm myRealm = new MyRealm();
//        myRealm.setCredentialsMatcher(myHashedCredentialsMatcher());
//        myRealm.setAuthenticationCachingEnabled(false);
//        return myRealm;
//    }


//    /**
//     * 这里的写法是不使用springboot的写法， 需要自己注入一个bean，
//     * springboot的写法里，springboot已经自动注入了一个bean，不需要我们再自行注入了，
//     * 目前先注释掉
//     * @return
//     */
//    @Bean("mySecurityManager")
//    public SessionsSecurityManager securityManager(){
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(myRealm());
//        return securityManager;
//    }

    /*
    下面的几个自行注入的bean，在springboot里都已经自动装配了，一般不需要自行注入了，先注释掉
     */

//    /**
//     * 管理Shiro中一些bean的生命周期
//     * @return
//     */
//    @Bean("lifecycleBeanPostProcessor")
//    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
//        return new LifecycleBeanPostProcessor();
//    }
//
//    /**
//     * 扫描上下文，寻找所有的Advistor(通知器）
//     * 将这些Advisor应用到所有符合切入点的Bean中。
//     */
//    @Bean
//    @DependsOn({"lifecycleBeanPostProcessor"})
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
//        proxyCreator.setProxyTargetClass(true);
//        return proxyCreator;
//    }
//
//    /**
//     * 开启shiro对注解的支持
//     * @param securityManager
//     * @return
//     */
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
//        advisor.setSecurityManager(securityManager);
//        return advisor;
//    }
}
