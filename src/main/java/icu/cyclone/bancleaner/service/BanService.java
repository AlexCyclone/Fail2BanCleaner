package icu.cyclone.bancleaner.service;

import icu.cyclone.bancleaner.domain.HostInfo;
import java.util.List;
import java.util.stream.Collectors;
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
        LOGGER.info("Auto clean process started");
        getUnbanList().forEach(this::unban);
        hostInfoService.storeDatabase();
        LOGGER.info("Auto clean process finished");
    }

    private void unban(HostInfo hostInfo) {
        LOGGER.info("Unban host: " + hostInfo.getIp());
        fail2BanService.unban(hostInfo.getIp());
        hostInfoService.unbanCountIncrement(hostInfo);
    }

    private List<HostInfo> getUnbanList() {
        return fail2BanService.fetchBannedIpSet().stream()
            .parallel()
            .map(hostInfoService::getHostInfo)
            .filter(decisionService::isUnban)
            .collect(Collectors.toList());
    }
}
