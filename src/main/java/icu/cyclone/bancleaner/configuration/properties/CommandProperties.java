package icu.cyclone.bancleaner.configuration.properties;

import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "console.command")
public class CommandProperties {

    @NotEmpty
    private String bannedData;
    @NotEmpty
    private String unbanHost;
}
