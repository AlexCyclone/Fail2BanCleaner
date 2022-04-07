package icu.cyclone.bancleaner.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "console.ssh.connection")
public class SshConnectionProperties {
    private String username;
    private String password;
    private String host;
    private int port;
    private String strictHostKey;
    private long executionTimeoutMillis;
}
