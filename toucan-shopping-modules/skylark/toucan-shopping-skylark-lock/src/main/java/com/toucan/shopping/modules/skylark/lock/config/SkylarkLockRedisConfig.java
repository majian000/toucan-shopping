package com.toucan.shopping.modules.skylark.lock.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toucan.shopping.modules.common.properties.Toucan;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(
        prefix = "toucan.modules.skylarkLock",
        name = "type",
        havingValue = "redis"
)
public class SkylarkLockRedisConfig {

    @Autowired
    private Toucan toucan;

    @Bean
    @Qualifier("skylarkLockRedisTemplate")
    public RedisTemplate<String, Object> skylarkLockRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(skylarkLockRedisConnectionFactory());
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
    @Qualifier("categoryJackson2JsonRedisSerializer")
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
    public RedisConnectionFactory skylarkLockRedisConnectionFactory() {
        log.info(" 初始化云雀分布式锁redis模块 redis相关配置.............");
        try {
            Map<String, Object> source = new HashMap<String, Object>();
            RedisClusterConfiguration redisClusterConfiguration;
            RedisStandaloneConfiguration redisStandaloneConfiguration;
            //连接池配置
            GenericObjectPoolConfig genericObjectPoolConfig =
                    new GenericObjectPoolConfig();
            genericObjectPoolConfig.setMaxTotal(toucan.getModules().getSkylarkLock().getRedis().getMaxActive());
            genericObjectPoolConfig.setMaxWaitMillis(toucan.getModules().getSkylarkLock().getRedis().getMaxWaitMillis());
            genericObjectPoolConfig.setMaxIdle(toucan.getModules().getSkylarkLock().getRedis().getMaxIdle());
            genericObjectPoolConfig.setMinIdle(toucan.getModules().getSkylarkLock().getRedis().getMinIdle());

            //redis客户端配置
            LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder
                    builder = LettucePoolingClientConfiguration.builder().
                    commandTimeout(Duration.ofSeconds(toucan.getModules().getSkylarkLock().getRedis().getTimeout()));
            builder.poolConfig(genericObjectPoolConfig);
            LettuceClientConfiguration lettuceClientConfiguration = builder.build();

            //集群模式
            if ("cluster".equals(toucan.getModules().getSkylarkLock().getRedis().getSelect())) {
                log.info(" 初始化云雀分布式锁redis模块 加载配置 toucan.modules.skylarkLock.redis.hosts:{}",toucan.getModules().getSkylarkLock().getRedis().getHosts());
                log.info(" 初始化云雀分布式锁redis模块 加载配置 toucan.modules.skylarkLock.redis.maxRedirects:{}",toucan.getModules().getSkylarkLock().getRedis().getMaxRedirects());
                source.put("spring.redis.cluster.nodes", toucan.getModules().getSkylarkLock().getRedis().getHosts());
                source.put("spring.redis.cluster.max-redirects", toucan.getModules().getSkylarkLock().getRedis().getMaxRedirects());
                redisClusterConfiguration = new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
                if (!StringUtils.isEmpty(toucan.getModules().getSkylarkLock().getRedis().getPassword())) {
                    redisClusterConfiguration.setPassword(toucan.getModules().getSkylarkLock().getRedis().getPassword());
                }
                //根据配置和客户端配置创建连接
                LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
                lettuceConnectionFactory.afterPropertiesSet();
                return lettuceConnectionFactory;
            } else {
                log.info(" 初始化云雀分布式锁redis模块 加载配置 toucan.modules.skylarkLock.redis.host:{}",toucan.getModules().getSkylarkLock().getRedis().getHost());
                log.info(" 初始化云雀分布式锁redis模块 加载配置 toucan.modules.skylarkLock.redis.port:{}",toucan.getModules().getSkylarkLock().getRedis().getPort());
                log.info(" 初始化云雀分布式锁redis模块 加载配置 toucan.modules.skylarkLock.redis.database:{}",toucan.getModules().getSkylarkLock().getRedis().getDatabase());
                //单机模式
                redisStandaloneConfiguration = new RedisStandaloneConfiguration(toucan.getModules().getSkylarkLock().getRedis().getHost(),
                        toucan.getModules().getSkylarkLock().getRedis().getPort());
                redisStandaloneConfiguration.setDatabase(toucan.getModules().getSkylarkLock().getRedis().getDatabase());
                if (!StringUtils.isEmpty(toucan.getModules().getSkylarkLock().getRedis().getPassword())) {
                    redisStandaloneConfiguration.setPassword(toucan.getModules().getSkylarkLock().getRedis().getPassword());
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
