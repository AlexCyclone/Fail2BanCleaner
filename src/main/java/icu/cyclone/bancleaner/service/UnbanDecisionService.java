package icu.cyclone.bancleaner.service;

import icu.cyclone.bancleaner.configuration.properties.UnbanProperties;
import icu.cyclone.bancleaner.domain.HostInfo;
import icu.cyclone.bancleaner.service.converter.UnbanConverter;
import java.util.List;
import java.util.Map;
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
        if (inBlackList(hostInfo) || inBlackListRules(hostInfo)) {
            return false;
        }
        return inWhiteList(hostInfo) || inWhiteListRules(hostInfo);
    }

    private boolean inBlackList(HostInfo hostInfo) {
        return isPresentInList(hostInfo, properties.getBlackList());
    }

    private boolean inBlackListRules(HostInfo hostInfo) {
        return isPresentInListRules(hostInfo, properties.getBlackListRules());
    }

    private boolean inWhiteList(HostInfo hostInfo) {
        return isPresentInList(hostInfo, properties.getWhiteList());
    }

    private boolean inWhiteListRules(HostInfo hostInfo) {
        return isPresentInListRules(hostInfo, properties.getWhiteListRules());
    }

    private boolean isPresentInList(HostInfo hostInfo, List<String> list) {
        var ip = hostInfo.getIp();
        var successRegex = list.stream()
            .map(converter::toRegex)
            .filter(ip::matches)
            .findAny();

        if (successRegex.isPresent()) {
            LOGGER.info("Host " + ip + " found by regex: " + successRegex.get());
            return true;
        }
        return false;
    }

    private boolean isPresentInListRules(HostInfo hostInfo, Map<String, List<String>> listRules) {
        var fieldNames = listRules.keySet();
        for (var fieldName : fieldNames) {
            var value = getStringValue(fieldName, hostInfo);
            if (value != null && listRules.get(fieldName).contains(value)) {
                LOGGER.info("Host " + hostInfo.getIp() + " found by field " + fieldName + ": " + value);
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