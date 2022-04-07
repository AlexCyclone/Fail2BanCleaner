package icu.cyclone.bancleaner.service;

import icu.cyclone.bancleaner.configuration.properties.StorageProperties;
import icu.cyclone.bancleaner.dataservice.HostInfoDataService;
import icu.cyclone.bancleaner.domain.HostInfo;
import java.util.Calendar;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HostInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostInfoService.class);

    private final CalendarService calendarService;
    private final HostInfoDataService dataService;
    private final WhoIsService whoIsService;
    private final StorageProperties properties;

    public HostInfo getHostInfo(String ipAddress) {
        var hostInfo = dataService.find(ipAddress)
            .orElseGet(() -> getFirsTimeBanned(ipAddress));

        return isExpired(hostInfo) ? getUpdatedHostInfo(hostInfo) : hostInfo;
    }

    public void storeDatabase() {
        dataService.storeDatabase();
    }

    public void unbanCountIncrement(HostInfo hostInfo) {
        dataService.save(
            hostInfo.toBuilder()
                .unbannedCount(hostInfo.getUnbannedCount() + 1)
                .build()
        );
    }

    private HostInfo getFirsTimeBanned(String ipAddress) {
        LOGGER.debug("Load whois info: " + ipAddress);
        var hostInfo = whoIsService.getHostInfo(ipAddress).toBuilder()
            .firstBannedDate(calendarService.getCalendar())
            .build();
        dataService.save(hostInfo);
        return hostInfo;
    }

    private boolean isExpired(HostInfo hostInfo) {
        var expirationDate = calendarService.getCalendar();
        expirationDate.add(Calendar.DAY_OF_MONTH, -properties.getWhoIsExpirationPeriodDays());
        return hostInfo.getLastUpdateDate().before(expirationDate);
    }

    private HostInfo getUpdatedHostInfo(HostInfo hostInfo) {
        LOGGER.debug("Update whois info: " + hostInfo.getIp());
        var updatedHostInfo = whoIsService.getHostInfo(hostInfo.getIp()).toBuilder()
            .unbannedCount(hostInfo.getUnbannedCount())
            .firstBannedDate(hostInfo.getFirstBannedDate())
            .build();
        dataService.save(updatedHostInfo);
        return updatedHostInfo;
    }
}