package icu.cyclone.bancleaner.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "storage.csv")
public class CsvStorageProperties {
    private boolean enabled = true;
    private boolean strictMode = false;
    private String path = System.getProperty("user.dir");
    private String fileName = "banned.csv";
}
