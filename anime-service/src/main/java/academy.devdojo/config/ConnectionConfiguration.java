package academy.devdojo.config;

import external.dependency.Connection;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class ConnectionConfiguration {
    @Bean
    public Connection connectionMySql(){
        return new Connection(8080,"MySQL",1821356);
    }

    @Bean(name = "connectionMongoDb")
    public Connection connectionMongoDb(){
        return new Connection(8080, "Mongo DB", 18213536);
    }
}
