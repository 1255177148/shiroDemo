package com.hezhan.shirodemo.config;

import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 设置拓扑刷新策略
 * @Author elvis
 * @Date 2021/4/26 17:04
 */
@Component
public class LettuceConfig {

    @Bean
    public DefaultClientResources lettuceClientResources() {
        return DefaultClientResources.create();
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(RedisProperties redisProperties, ClientResources clientResources) {

        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(Duration.ofSeconds(30)) //按照周期刷新拓扑
                .enableAllAdaptiveRefreshTriggers() //根据事件刷新拓扑
                .build();

        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
                //redis命令超时时间,超时后才会使用新的拓扑信息重新建立连接
                .timeoutOptions(TimeoutOptions.enabled(Duration.ofSeconds(10)))
                .topologyRefreshOptions(topologyRefreshOptions)
                .build();

        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
                .clientResources(clientResources)
                .clientOptions(clusterClientOptions)
                .build();

        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(redisProperties.getCluster().getNodes());
        clusterConfig.setMaxRedirects(redisProperties.getCluster().getMaxRedirects());
        clusterConfig.setPassword(RedisPassword.of(redisProperties.getPassword()));

        return new LettuceConnectionFactory(clusterConfig, clientConfiguration);
    }
}
