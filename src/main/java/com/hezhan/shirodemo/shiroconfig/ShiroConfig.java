package com.hezhan.shirodemo.shiroconfig;

import com.hezhan.shirodemo.util.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class ShiroConfig {

    @Bean
    public MyHashedCredentialsMatcher myHashedCredentialsMatcher(){
        MyHashedCredentialsMatcher myHashedCredentialsMatcher = new MyHashedCredentialsMatcher();
        myHashedCredentialsMatcher.setHashAlgorithmName("MD5");// 加密算法的名称
        myHashedCredentialsMatcher.setHashIterations(2);// 加密的次数
        myHashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);// 是否储存为16进制
        return myHashedCredentialsMatcher;
    }

    @Bean
    public MyRealm myRealm(){
        MyRealm myRealm = new MyRealm();
        myRealm.setCredentialsMatcher(myHashedCredentialsMatcher());
        return myRealm;
    }
}
