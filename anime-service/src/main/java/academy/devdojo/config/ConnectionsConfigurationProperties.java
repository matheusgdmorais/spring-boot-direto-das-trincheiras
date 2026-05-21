package academy.devdojo.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "database")
public record ConnectionsConfigurationProperties(String url, String username, String password) {
}
