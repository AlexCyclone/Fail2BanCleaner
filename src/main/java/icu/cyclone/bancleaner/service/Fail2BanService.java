package icu.cyclone.bancleaner.service;

import icu.cyclone.bancleaner.client.console.ConsoleClient;
import icu.cyclone.bancleaner.configuration.properties.CommandProperties;
import icu.cyclone.bancleaner.exception.Fail2BanServiceException;
import icu.cyclone.bancleaner.service.converter.ConsoleConverter;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Fail2BanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Fail2BanService.class);

    private final ConsoleClient consoleClient;
    private final CommandProperties commandProperties;
    private final ConsoleConverter consoleConverter;

    public Set<String> fetchBannedIpSet() {
        String bannedData = consoleClient.execute(commandProperties.getBannedData());
        LOGGER.debug("Banned data: '{}'", bannedData);
        return consoleConverter.toIpSet(bannedData);
    }

    public void unban(String ipAddress) {
        LOGGER.debug("Unban '{}'", ipAddress);
        String response = consoleClient.execute(
            String.format(commandProperties.getUnbanHost(), ipAddress)
        );
        LOGGER.debug("Unban response: '{}'", response);
        validateUnbanResponse(response);
    }

    private void validateUnbanResponse(String response) {
        try {
            Integer.parseInt(response.trim());
        } catch (NumberFormatException e) {
            throw new Fail2BanServiceException("Incorrect unban response: " + response);
        }
    }
}