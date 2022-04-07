package icu.cyclone.bancleaner.client.whois.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class WhoIsDto {
    private boolean success;
    private String message;
    private String ip;
    private String type;
    private String continent;
    @JsonProperty("continent_code")
    private String continentCode;
    private String country;
    @JsonProperty("country_code")
    private String countryCode;
    @JsonProperty("country_flag")
    private String countryFlag;
    @JsonProperty("country_phone")
    private String countryPhone;
    @JsonProperty("country_neighbours")
    private String countryNeighbours;
    private String region;
    private String city;
    private Double latitude;
    private Double longitude;
    private String asn;
    private String org;
    private String isp;
    private String timezone;
    @JsonProperty("timezone_name")
    private String timezoneName;
    @JsonProperty("timezone_gmt")
    private String timezoneGmt;
    private String currency;
    @JsonProperty("currency_code")
    private String currencyCode;
    @JsonProperty("completed_requests")
    private Integer completedRequests;
}
