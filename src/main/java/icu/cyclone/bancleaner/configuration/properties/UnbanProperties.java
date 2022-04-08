package icu.cyclone.bancleaner.configuration.properties;

import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "unban")
public class UnbanProperties {
    private List<String> blackList;
    private Map<String, List<String>> blackListRules;
    private List<String> whiteList;
    private Map<String, List<String>> whiteListRules;
}
