package icu.cyclone.bancleaner;

import icu.cyclone.bancleaner.client.storage.dto.StorageHostInfoDto;
import icu.cyclone.bancleaner.client.whois.dto.WhoIsDto;
import icu.cyclone.bancleaner.domain.HostInfo;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TestObjects {
    public static final Calendar TEST_DATE = new GregorianCalendar(2022, 0 , 1);
    public static final StorageHostInfoDto TEMPLATE_STORAGE_HOST_INFO_DTO = StorageHostInfoDto.builder()
        .ip("8.8.8.8")
        .unbannedCount(0)
        .firstBannedDate(TEST_DATE)
        .lastUpdateDate(TEST_DATE)
        .countryCode("US")
        .country("United States")
        .region("California")
        .city("Mountain View")
        .latitude(37.3860517)
        .longitude(-122.0838511)
        .org("Google LLC")
        .isp("Google LLC")
        .build();

    public static final HostInfo TEMPLATE_HOST_INFO = HostInfo.builder()
        .ip("8.8.8.8")
        .unbannedCount(0)
        .firstBannedDate(TEST_DATE)
        .lastUpdateDate(TEST_DATE)
        .countryCode("US")
        .country("United States")
        .region("California")
        .city("Mountain View")
        .latitude(37.3860517)
        .longitude(-122.0838511)
        .org("Google LLC")
        .isp("Google LLC")
        .build();

    public static final HostInfo TEMPLATE_HOST_INFO_EMPTY = HostInfo.builder()
        .ip("10.8.8.8")
        .message("reserved range")
        .build();

    public static final WhoIsDto TEMPLATE_WHO_IS_DTO = WhoIsDto.builder()
        .success(true)
        .ip("8.8.8.8")
        .type("IPv4")
        .continent("North America")
        .continentCode("NA")
        .country("United States")
        .countryCode("US")
        .countryFlag("https://cdn.ipwhois.io/flags/us.svg")
        .countryPhone("+1")
        .countryNeighbours("CA,MX,CU")
        .region("California")
        .city("Mountain View")
        .latitude(37.3860517)
        .longitude(-122.0838511)
        .asn("AS15169")
        .org("Google LLC")
        .isp("Google LLC")
        .timezone("America/Los_Angeles")
        .timezoneName("Pacific Standard Time")
        .timezoneGmt("GMT -8:00")
        .currency("US Dollar")
        .currencyCode("USD")
        .completedRequests(16)
        .build();

    public static final WhoIsDto TEMPLATE_WHO_IS_DTO_ERROR = WhoIsDto.builder()
        .success(false)
        .message("reserved range")
        .build();
}
