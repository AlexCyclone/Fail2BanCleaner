package icu.cyclone.bancleaner.client.storage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Calendar;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
@JsonPropertyOrder({
    "ip",
    "unbannedCount",
    "firstBannedDate",
    "lastUpdateDate",
    "countryCode",
    "country",
    "region",
    "city",
    "latitude",
    "longitude",
    "org",
    "isp",
    "message"
})
public class StorageHostInfoDto {
    private String ip;
    private Integer unbannedCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Calendar firstBannedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Calendar lastUpdateDate;
    private String countryCode;
    private String country;
    private String region;
    private String city;
    private Double latitude;
    private Double longitude;
    private String org;
    private String isp;
    private String message;
}