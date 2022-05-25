package top.guoshouzhen.blog.blogmsauth.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * redis 配置类
 * spring cache使用
 * 如果需要手动做一些redis操作，可以封装一个RedisUtil，RedisUtil内部可以从容器获取一个RedisTemplate对象，在其基础上封装一些操作
 *
 * @author GuoShouzhen
 * @date 2021/12/17
 */
@Configuration
public class RedisConfig {
    /**
     * 缓存有效时间默认为1天
      */
    private static final Duration REDIS_KEY_MAX_LIVE_TIME = Duration.ofDays(1L);

    @Value("${spring.jackson.date-format}")
    private String pattern;

    /**
     * 配置缓存管理器
     *
     * @param redisConnectionFactory RedisConnectionFactory
     * @return org.springframework.data.redis.cache.RedisCacheManager
     * @author GuoShouzhen
     * @date 2021/12/17 13:25
     */
    @Primary
    @Bean
    public CacheManager chacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory)
                //默认过期时间
                .cacheDefaults(getRedisCacheConfig(REDIS_KEY_MAX_LIVE_TIME))
                //为不同cacheName配置过期时间
                .withCacheConfiguration("RoleInfo", getRedisCacheConfig(Duration.ofMinutes(1L)))
                .transactionAware()
                .build();
    }

    /**
     * 配置redisTemplate
     *
     * @param factory RedisConnectionFactory
     * @return org.springframework.data.redis.core.RedisTemplate
     * @author GuoShouzhen
     * @date 2021/12/17 14:41
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // 创建模板类
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 设置redis连接工厂
        redisTemplate.setConnectionFactory(factory);
        // 设置value和hash的value序列化方式采用jackson
        redisTemplate.setValueSerializer(getDefaultRedisSerializer());
        redisTemplate.setHashValueSerializer(getDefaultRedisSerializer());

        // 序列化String类型
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 配置redis cache
     * @author shouzhen.guo
     * @date 2022/5/18 15:09
     * @param ttl 缓存过期时间
     * @return org.springframework.data.redis.cache.RedisCacheConfiguration
     */
    private RedisCacheConfiguration getRedisCacheConfig(Duration ttl) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(getDefaultRedisSerializer()));
    }

    /**
     * 配置redis key和value的序列化器
     *
     * @return org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer<java.lang.Object>
     * @author GuoShouzhen
     * @date 2021/12/17 16:54
     */
    private Jackson2JsonRedisSerializer<Object> getDefaultRedisSerializer() {
        // 序列化配置，解析任意对象
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        // 配置ObjectMapper
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        //增加对LocalDateTime的支持
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(pattern)));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern)));
        om.registerModule(javaTimeModule);

        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }
}
