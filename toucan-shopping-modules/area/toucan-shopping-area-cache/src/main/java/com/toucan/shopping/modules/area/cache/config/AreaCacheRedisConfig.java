package com.toucan.shopping.modules.area.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toucan.shopping.modules.common.properties.Toucan;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class AreaCacheRedisConfig {

    @Autowired
    private Toucan toucan;

    @Bean
    @Qualifier("areaCacheRedisTemplate")
    public RedisTemplate<String, Object> areaCacheRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(areaCacheRedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(this.jackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(this.jackson2JsonRedisSerializer());
        return redisTemplate;
    }

    /**
     * k-v的序列化
     *
     * @return
     */
    @Bean
    public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }



    /**
     * 连接配置
     * @return
     */
    public RedisConnectionFactory areaCacheRedisConnectionFactory() {
        log.info(" 初始化地区模块 redis缓存功能.............");
        try {
            Map<String, Object> source = new HashMap<String, Object>();
            RedisClusterConfiguration redisClusterConfiguration;
            RedisStandaloneConfiguration redisStandaloneConfiguration;
            //连接池配置
            GenericObjectPoolConfig genericObjectPoolConfig =
                    new GenericObjectPoolConfig();
            genericObjectPoolConfig.setMaxTotal(toucan.getModules().getAreaCache().getRedis().getMaxActive());
            genericObjectPoolConfig.setMaxWaitMillis(toucan.getModules().getAreaCache().getRedis().getMaxWaitMillis());
            genericObjectPoolConfig.setMaxIdle(toucan.getModules().getAreaCache().getRedis().getMaxIdle());
            genericObjectPoolConfig.setMinIdle(toucan.getModules().getAreaCache().getRedis().getMinIdle());

            //redis客户端配置
            LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder
                    builder = LettucePoolingClientConfiguration.builder().
                    commandTimeout(Duration.ofSeconds(toucan.getModules().getAreaCache().getRedis().getTimeout()));
            builder.poolConfig(genericObjectPoolConfig);
            LettuceClientConfiguration lettuceClientConfiguration = builder.build();

            //集群模式
            if ("cluster".equals(toucan.getModules().getAreaCache().getRedis().getSelect())) {
                log.info(" 初始化地区模块 redis缓存功能 加载配置 toucan.modules.areaCache.redis.hosts:{}",toucan.getModules().getAreaCache().getRedis().getHosts());
                log.info(" 初始化地区模块 redis缓存功能 加载配置 toucan.modules.areaCache.redis.maxRedirects:{}",toucan.getModules().getAreaCache().getRedis().getMaxRedirects());

                source.put("spring.redis.cluster.nodes", toucan.getModules().getAreaCache().getRedis().getHosts());
                source.put("spring.redis.cluster.max-redirects", toucan.getModules().getAreaCache().getRedis().getMaxRedirects());
                redisClusterConfiguration = new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
                if (!StringUtils.isEmpty(toucan.getModules().getAreaCache().getRedis().getPassword())) {
                    redisClusterConfiguration.setPassword(toucan.getModules().getAreaCache().getRedis().getPassword());
                }
                //根据配置和客户端配置创建连接
                LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
                lettuceConnectionFactory.afterPropertiesSet();
                return lettuceConnectionFactory;
            } else {
                log.info(" 初始化地区模块 redis缓存功能 加载配置 toucan.modules.areaCache.redis.host:{}",toucan.getModules().getAreaCache().getRedis().getHost());
                log.info(" 初始化地区模块 redis缓存功能 加载配置 toucan.modules.areaCache.redis.port:{}",toucan.getModules().getAreaCache().getRedis().getPort());
                log.info(" 初始化地区模块 redis缓存功能 加载配置 toucan.modules.areaCache.redis.database:{}",toucan.getModules().getAreaCache().getRedis().getDatabase());
                //单机模式
                redisStandaloneConfiguration = new RedisStandaloneConfiguration(toucan.getModules().getAreaCache().getRedis().getHost(),
                        toucan.getModules().getAreaCache().getRedis().getPort());
                redisStandaloneConfiguration.setDatabase(toucan.getModules().getAreaCache().getRedis().getDatabase());
                if (!StringUtils.isEmpty(toucan.getModules().getAreaCache().getRedis().getPassword())) {
                    redisStandaloneConfiguration.setPassword(toucan.getModules().getAreaCache().getRedis().getPassword());
                }
                //根据配置和客户端配置创建连接工厂
                LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
                lettuceConnectionFactory.afterPropertiesSet();
                return lettuceConnectionFactory;

            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }
}
