package icu.cyclone.bancleaner.service.converter;

import icu.cyclone.bancleaner.client.storage.dto.StorageHostInfoDto;
import icu.cyclone.bancleaner.domain.HostInfo;
import org.springframework.stereotype.Component;

@Component
public class StorageHostInfoDtoConverter {

    public HostInfo toHostInfo(StorageHostInfoDto dto) {
        return HostInfo.builder()
            .ip(dto.getIp())
            .unbannedCount(dto.getUnbannedCount())
            .firstBannedDate(dto.getFirstBannedDate())
            .lastUpdateDate(dto.getLastUpdateDate())
            .countryCode(dto.getCountryCode())
            .country(dto.getCountry())
            .region(dto.getRegion())
            .city(dto.getCity())
            .latitude(dto.getLatitude())
            .longitude(dto.getLongitude())
            .org(dto.getOrg())
            .isp(dto.getIsp())
            .message(dto.getMessage())
            .build();
    }

    public StorageHostInfoDto toStorageHostInfoDto(HostInfo hostInfo) {
        return StorageHostInfoDto.builder()
            .ip(hostInfo.getIp())
            .unbannedCount(hostInfo.getUnbannedCount())
            .firstBannedDate(hostInfo.getFirstBannedDate())
            .lastUpdateDate(hostInfo.getLastUpdateDate())
            .countryCode(hostInfo.getCountryCode())
            .country(hostInfo.getCountry())
            .region(hostInfo.getRegion())
            .city(hostInfo.getCity())
            .latitude(hostInfo.getLatitude())
            .longitude(hostInfo.getLongitude())
            .org(hostInfo.getOrg())
            .isp(hostInfo.getIsp())
            .message(hostInfo.getMessage())
            .build();
    }
}