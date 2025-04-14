package silverpotion.userserver.common.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

    @Bean(name = "redisConnectionFactory")
    @Qualifier("refreshToken")
    public RedisConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(0);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    @Qualifier("refreshToken")
    public RedisTemplate<String,Object> redisTemplate(@Qualifier("refreshToken") RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    @Qualifier("pay")
    public RedisConnectionFactory redisConnectionFactoryForPay(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(2);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    @Qualifier("pay")
    public RedisTemplate<String,String> redisTemplateForPay(@Qualifier("pay") RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }


    @Bean
    public StringRedisTemplate stringRedisTemplate(@Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
    @Bean
    @Qualifier("sms")
    public RedisConnectionFactory redisConnectionFactoryForSms(){ //레디스 연결을 설정하는 ConnectonFactory객체를 생성하고 반환
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host); //레디스 서버의 호스트 이름
        configuration.setPort(port); //레디스 서버의 포트 번호
        configuration.setDatabase(3); //레디스에 사용할 데이터베이스 인덱스
        return new LettuceConnectionFactory(configuration); //위에서 설정한 configuration을 기반으로 레디스에 연결할 객체
    }

    //sms기능 간섭안하는지 체크---------------------------------------------------------------------------------------------
    @Bean
    @Qualifier("sms")
    public StringRedisTemplate stringRedisTemplateforSms(@Qualifier("sms") RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }
//    ------------------------

}
