package icu.cyclone.bancleaner.service;

import icu.cyclone.bancleaner.domain.HostInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BanService.class);
    private final HostInfoService hostInfoService;
    private final Fail2BanService fail2BanService;
    private final UnbanDecisionService decisionService;

    public void autoClean() {
        fail2BanService.fetchBannedIpSet().stream()
            .map(hostInfoService::getHostInfo)
            .filter(decisionService::isUnban)
            .forEach(this::unban);
        hostInfoService.storeDatabase();
    }

    private void unban(HostInfo hostInfo) {
        LOGGER.debug("Unban host: " + hostInfo.getIp());
        fail2BanService.unban(hostInfo.getIp());
        hostInfoService.unbanCountIncrement(hostInfo);
    }
}
