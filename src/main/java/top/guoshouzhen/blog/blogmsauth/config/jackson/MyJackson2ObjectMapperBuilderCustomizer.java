package top.guoshouzhen.blog.blogmsauth.config.jackson;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 修改Spring容器中对ObjectMapper的配置（重新配置的话会覆盖掉Spring注入的对象）
 * 全局日期格式化配置，解决Jackson不支持Java8LocalDatetime的问题
 * @author guoshouzhen
 * @date 2021/11/20 17:04
 */
@Component
public class MyJackson2ObjectMapperBuilderCustomizer implements Jackson2ObjectMapperBuilderCustomizer, Ordered {
    @Value("${spring.jackson.date-format}")
    private String pattern;

    @Override
    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        jacksonObjectMapperBuilder.modules(getCustomizedTimeConfig());
    }

    @Override
    public int getOrder() {
        return 1;
    }

    /**
     * 日期格式化配置
     * @author GuoShouzhen
     * @date 2021/12/17 16:13
     * @return com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
     */
    public JavaTimeModule getCustomizedTimeConfig(){
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(pattern)));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern)));
        return javaTimeModule;
    }
//    @Value("${spring.jackson.date-format}")
//    private String pattern;
//
//    @Bean
//    public ObjectMapper initObjectMapper(){
//        ObjectMapper objectMapper = new ObjectMapper();
//        JavaTimeModule javaTimeModule = new JavaTimeModule();
//        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(pattern)));
//        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern)));
//        objectMapper.registerModule(javaTimeModule);
//        return objectMapper;
//    }

}
