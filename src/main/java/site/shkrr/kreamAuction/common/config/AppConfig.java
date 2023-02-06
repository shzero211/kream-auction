package site.shkrr.kreamAuction.common.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * 스프링 컨테이너를 static 객체로 가져오기위한 설정
 * */
@Configuration
public class AppConfig {
    @Getter
    private static ApplicationContext context;

    @Autowired
    public void setContext(ApplicationContext context){
        AppConfig.context=context;
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }

}