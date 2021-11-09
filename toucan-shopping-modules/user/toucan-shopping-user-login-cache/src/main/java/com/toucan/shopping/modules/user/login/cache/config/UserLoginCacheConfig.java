package com.toucan.shopping.modules.user.login.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.properties.modules.userLoginCache.UserLoginRedis;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.redis.service.impl.ToucanStringRedisServiceImpl;
import com.toucan.shopping.modules.user.login.cache.service.UserLoginCacheService;
import com.toucan.shopping.modules.user.login.cache.service.impl.UserLoginCacheServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class UserLoginCacheConfig {

    @Autowired
    private Toucan toucan;

    @Bean
    @Qualifier("userLoginCacheService")
    public UserLoginCacheService userLoginCacheService() {
        UserLoginCacheService userLoginCacheService = new UserLoginCacheServiceImpl();

        if("redis".equals(toucan.getModules().getUserLoginCache().getCacheType()))
        {
            Map<String, ToucanStringRedisService> toucanStringRedisServiceMap = userLoginCacheService.getToucanStringRedisServiceMap();
            if(CollectionUtils.isNotEmpty(toucan.getModules().getUserLoginCache().getLoginCacheRedisList()))
            {
                for(UserLoginRedis userLoginRedis:toucan.getModules().getUserLoginCache().getLoginCacheRedisList()) {
                    ToucanStringRedisService toucanStringRedisService = new ToucanStringRedisServiceImpl();
                    ((ToucanStringRedisServiceImpl)toucanStringRedisService).setRedisTemplate(createToucanRedisTemplate(userLoginRedis));
                    toucanStringRedisServiceMap.put(userLoginRedis.getIndex(),toucanStringRedisService);
                }
            }
        }
        return userLoginCacheService;
    }



    public RedisTemplate<String, Object> createToucanRedisTemplate(UserLoginRedis userLoginRedis) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(toucanRedisConnectionFactory(userLoginRedis));
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
    public RedisConnectionFactory toucanRedisConnectionFactory(UserLoginRedis userLoginRedis) {
        log.info(" 初始化犀鸟用户中心-用户登录redis模块 .............");
        try {
            Map<String, Object> source = new HashMap<String, Object>();
            RedisClusterConfiguration redisClusterConfiguration;
            RedisStandaloneConfiguration redisStandaloneConfiguration;
            //连接池配置
            GenericObjectPoolConfig genericObjectPoolConfig =
                    new GenericObjectPoolConfig();
            genericObjectPoolConfig.setMaxTotal(userLoginRedis.getMaxActive());
            genericObjectPoolConfig.setMaxWaitMillis(userLoginRedis.getMaxWaitMillis());
            genericObjectPoolConfig.setMaxIdle(userLoginRedis.getMaxIdle());
            genericObjectPoolConfig.setMinIdle(userLoginRedis.getMinIdle());

            //redis客户端配置
            LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder
                    builder = LettucePoolingClientConfiguration.builder().
                    commandTimeout(Duration.ofSeconds(userLoginRedis.getTimeout()));
            builder.poolConfig(genericObjectPoolConfig);
            LettuceClientConfiguration lettuceClientConfiguration = builder.build();

            log.info(" 初始化犀鸟用户中心-用户登录redis模块 加载配置 toucan.modules.userLoginCache.login-cache-redis-list.index:{}",userLoginRedis.getIndex());
            //集群模式
            if ("cluster".equals(userLoginRedis.getSelect())) {
                log.info(" 初始化犀鸟用户中心-用户登录redis模块 加载配置 toucan.modules.userLoginCache.login-cache-redis-list.hosts:{}",userLoginRedis.getHosts());
                log.info(" 初始化犀鸟用户中心-用户登录redis模块 加载配置 toucan.modules.userLoginCache.login-cache-redis-list.maxRedirects:{}",userLoginRedis.getMaxRedirects());
                source.put("spring.redis.cluster.nodes", userLoginRedis.getHosts());
                source.put("spring.redis.cluster.max-redirects", userLoginRedis.getMaxRedirects());
                redisClusterConfiguration = new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
                if (!StringUtils.isEmpty(userLoginRedis.getPassword())) {
                    redisClusterConfiguration.setPassword(userLoginRedis.getPassword());
                }
                //根据配置和客户端配置创建连接
                LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
                lettuceConnectionFactory.afterPropertiesSet();
                return lettuceConnectionFactory;
            } else {
                log.info(" 初始化犀鸟用户中心-用户登录redis模块 加载配置 toucan.modules.userLoginCache.login-cache-redis-list.host:{}",userLoginRedis.getHost());
                log.info(" 初始化犀鸟用户中心-用户登录redis模块 加载配置 toucan.modules.userLoginCache.login-cache-redis-list.port:{}",userLoginRedis.getPort());
                log.info(" 初始化犀鸟用户中心-用户登录redis模块 加载配置 toucan.modules.userLoginCache.login-cache-redis-list.database:{}",userLoginRedis.getDatabase());
                //单机模式
                redisStandaloneConfiguration = new RedisStandaloneConfiguration(userLoginRedis.getHost(),
                        userLoginRedis.getPort());
                redisStandaloneConfiguration.setDatabase(userLoginRedis.getDatabase());
                if (!StringUtils.isEmpty(userLoginRedis.getPassword())) {
                    redisStandaloneConfiguration.setPassword(userLoginRedis.getPassword());
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
