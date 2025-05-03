package silverpotion.userserver.common.feignClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class FeignConfig {
    @Bean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(
                new SpringDecoder(() -> new HttpMessageConverters(
                        new MappingJackson2HttpMessageConverter(objectMapper())
                ))
        );
    }
// 위 코드는 페인으로 받은 JSON응답을 자바 객체로 변환할 수 있도록 설정한 디코더

    @Bean
    public Encoder feignEncoder() {
        return new SpringEncoder(() -> new HttpMessageConverters(
                new MappingJackson2HttpMessageConverter(objectMapper())
        ));
    }

// 위 코드는 반대로 페인으로 보낼 요청의 바디를 JSON으로 바꾸기 위한 설정

    private ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

//    위 코드가 있어야 LocalDate 같은 타입에대해 JSON직렬화/역직렬화 시 오류를 막는다
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> template.header("Internal-Request", "true");
    }

}
//위 코드는 페인 요청 헤더에 Internal-Request : true를 추가하는 인터셉터. 내부 서비스간 호출인지 아닌지에 대한 구분용으로 붙임