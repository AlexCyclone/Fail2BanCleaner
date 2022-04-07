package icu.cyclone.bancleaner;

import icu.cyclone.bancleaner.configuration.properties.StorageProperties;
import icu.cyclone.bancleaner.configuration.properties.CommandProperties;
import icu.cyclone.bancleaner.configuration.properties.CsvStorageProperties;
import icu.cyclone.bancleaner.configuration.properties.SshConnectionProperties;
import icu.cyclone.bancleaner.configuration.properties.UnbanProperties;
import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableFeignClients
@EnableConfigurationProperties({
    SshConnectionProperties.class,
    CommandProperties.class,
    CsvStorageProperties.class,
    StorageProperties.class,
    UnbanProperties.class
})
public class BanCleanerApplication {

    @Generated
    public static void main(String[] args) {
        SpringApplication.run(BanCleanerApplication.class, args);
    }
}