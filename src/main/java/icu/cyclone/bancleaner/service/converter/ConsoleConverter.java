package icu.cyclone.bancleaner.service.converter;

import icu.cyclone.bancleaner.exception.Fail2BanServiceException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ConsoleConverter {
    private static final String BANNED_LINE_SIGNATURE = "Banned IP list";
    private static final String START_BANNED_LINE_REGEXP = ".*" + BANNED_LINE_SIGNATURE + "[\\p{P}\\s]+";
    private static final String EMPTY_STRING = "";
    private static final String SPACE = " ";

    public Set<String> toIpSet(String consoleData) {
        validateBannedIp(consoleData);
        return consoleData.lines()
            .filter(s -> s.contains(BANNED_LINE_SIGNATURE))
            .map(s -> s.replaceFirst(START_BANNED_LINE_REGEXP, EMPTY_STRING))
            .map(String::trim)
            .flatMap(s -> Arrays.stream(s.split(SPACE)))
            .filter(s -> !s.isBlank())
            .collect(Collectors.toSet());
    }

    private void validateBannedIp(String consoleData) {
        if (!consoleData.contains(BANNED_LINE_SIGNATURE)) {
            throw new Fail2BanServiceException("Incorrect banned ip data: " + consoleData);
        }
    }
}
