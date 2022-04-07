package icu.cyclone.bancleaner.service;

import icu.cyclone.bancleaner.configuration.properties.UnbanProperties;
import icu.cyclone.bancleaner.domain.HostInfo;
import icu.cyclone.bancleaner.service.converter.UnbanConverter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

@Service
@RequiredArgsConstructor
public class UnbanDecisionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnbanDecisionService.class);

    private final UnbanProperties properties;
    private final UnbanConverter converter;

    public boolean isUnban(HostInfo hostInfo) {
        return inWhiteList(hostInfo) || inWhoisRules(hostInfo);
    }

    private boolean inWhiteList(HostInfo hostInfo) {
        var ip = hostInfo.getIp();
        var successRegex = properties.getWhiteList().stream()
            .map(converter::toRegex)
            .filter(ip::matches)
            .findAny();

        if (successRegex.isPresent()) {
            LOGGER.debug("Host " + ip + " in whitelist by regex: " + successRegex.get());
            return true;
        }

        return false;
    }

    private boolean inWhoisRules(HostInfo hostInfo) {
        var rulesMap = properties.getWhoisRules();
        var fieldNames = rulesMap.keySet();
        for (var fieldName : fieldNames) {
            var value = getStringValue(fieldName, hostInfo);
            if (value != null && rulesMap.get(fieldName).contains(value)) {
                return true;
            }
        }
        return false;
    }

    private String getStringValue(String fieldName, HostInfo hostInfo) {
        var field = ReflectionUtils.findField(HostInfo.class, fieldName);
        if (field != null) {
            ReflectionUtils.makeAccessible(field);
            var value = String.valueOf(ReflectionUtils.getField(field, hostInfo));
            field.setAccessible(false);
            return value;
        }
        LOGGER.warn("Parameter \"" + fieldName + "\" not found");
        return null;
    }
}