package icu.cyclone.bancleaner.domain;

import java.util.Calendar;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.Hibernate;

@Entity
@Table(name = "HostInfo")
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(onlyExplicitlyIncluded = true)
public class HostInfo {
    @Id
    @ToString.Include
    private String ip;

    private int unbannedCount;
    private Calendar firstBannedDate;
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

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        HostInfo hostInfo = (HostInfo) o;
        return ip != null && Objects.equals(ip, hostInfo.ip);
    }

    @Generated
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}