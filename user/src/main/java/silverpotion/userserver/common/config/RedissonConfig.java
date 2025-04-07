package silverpotion.userserver.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnection;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import javax.cache.CacheManager;
import java.time.Duration;

@Configuration //spring 설정 클래스임을 의미
public class RedissonConfig {
    //redis 위치 지정
    @Value( "${spring.redis.host}")
    private String redisHost;
    @Value( "${spring.redis.port}")
    private int redisPort;

    private static final String REDISSON_HOST_PREFIX = "redis://";

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress(REDISSON_HOST_PREFIX + redisHost + ":" + redisPort);
        return Redisson.create(config);
    }

    @Bean(name = "redissonConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(redisHost,redisPort);
    }

    //캐시를 위한 설정
    @Bean
    public RedisCacheManager cacheManager(){ //캐시 매니저 등록
        RedisCacheManager.RedisCacheManagerBuilder builder = //레디스 연결 팩토리를 기반으로  캐시 매니저를 생성하는 빌더 레디스 서버와 연결 담당
                RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory());
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig() //기본 레디스 캐시 설정 객체 생성
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer( //레디스에 저장되는 VALUE값을 JSON혀익으로 저장
                                new GenericJackson2JsonRedisSerializer())).entryTtl(Duration.ofMinutes(30)); //캐시 시간 설정
        builder.cacheDefaults(configuration);
        return builder.build();
    }
}
