package icu.cyclone.bancleaner.service.converter;

import icu.cyclone.bancleaner.client.whois.dto.WhoIsDto;
import icu.cyclone.bancleaner.domain.HostInfo;
import icu.cyclone.bancleaner.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IpWhoIsConverter {
    private final CalendarService calendarService;

    public HostInfo toHostInfo(WhoIsDto whoIsDto, String hostIp) {
        return HostInfo.builder()
            .ip(whoIsDto.isSuccess() ? whoIsDto.getIp() : hostIp)
            .lastUpdateDate(calendarService.getCalendar())
            .countryCode(whoIsDto.getCountryCode())
            .country(whoIsDto.getCountry())
            .region(whoIsDto.getRegion())
            .city(whoIsDto.getRegion())
            .latitude(whoIsDto.getLatitude())
            .longitude(whoIsDto.getLongitude())
            .org(whoIsDto.getOrg())
            .isp(whoIsDto.getIsp())
            .message(whoIsDto.getMessage())
            .build();
    }
}