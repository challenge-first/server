package challenge.competer.global.config;

import challenge.competer.global.log.LogTrace;
import challenge.competer.global.log.LogTraceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfig {

    @Bean
    public LogTrace logTrace() {
        return new LogTraceImpl();
    }
}
