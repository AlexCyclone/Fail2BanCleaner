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
        var present = isPresentInList(hostInfo, properties.getBlackList());
        if (present) {
            LOGGER.info("Host '{}' found in black list", hostInfo.getIp());
        }
        return present;
    }

    private boolean inBlackListRules(HostInfo hostInfo) {
        var present = isPresentInListRules(hostInfo, properties.getBlackListRules());
        if (present) {
            LOGGER.info("Host '{}' found in black list rules", hostInfo.getIp());
        }
        return present;
    }

    private boolean inWhiteList(HostInfo hostInfo) {
        var present = isPresentInList(hostInfo, properties.getWhiteList());
        if (present) {
            LOGGER.info("Host '{}' found in white list", hostInfo.getIp());
        }
        return present;
    }

    private boolean inWhiteListRules(HostInfo hostInfo) {
        var present = isPresentInListRules(hostInfo, properties.getWhiteListRules());
        if (present) {
            LOGGER.info("Host '{}' found in white list rules", hostInfo.getIp());
        }
        return present;
    }

    private boolean isPresentInList(HostInfo hostInfo, List<String> list) {
        var ip = hostInfo.getIp();
        var successRegex = list.stream()
            .map(converter::toRegex)
            .filter(ip::matches)
            .findAny();

        if (successRegex.isPresent()) {
            LOGGER.debug("Host '{}' found by regex: '{}'", ip, successRegex.get());
            return true;
        }
        return false;
    }

    private boolean isPresentInListRules(HostInfo hostInfo, Map<String, List<String>> listRules) {
        var fieldNames = listRules.keySet();
        for (var fieldName : fieldNames) {
            var value = getStringValue(fieldName, hostInfo);
            if (value != null && listRules.get(fieldName).contains(value)) {
                LOGGER.debug("Host '{}' found by field '{}' with value '{}'", hostInfo.getIp(), fieldName, value);
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
        LOGGER.warn("Rule parameter '{}' not found", fieldName);
        return null;
    }
}