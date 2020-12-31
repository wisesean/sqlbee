package site.autzone.sqlbee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.autzone.sqlbee.SqlRunner;

@Configuration
public class RunnerConfiguration {
    @Bean
    public SqlRunner sqlRunner() {
        return new SqlRunner();
    }
}
