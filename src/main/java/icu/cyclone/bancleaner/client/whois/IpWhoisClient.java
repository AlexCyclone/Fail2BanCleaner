package icu.cyclone.bancleaner.client.whois;

import icu.cyclone.bancleaner.client.whois.dto.WhoIsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "whois", url = "${whois.url}")
public interface IpWhoisClient {
    String HOST_INFO = "/{ipAddress}";

    @GetMapping(HOST_INFO)
    WhoIsDto getWhoIs(@PathVariable String ipAddress);
}
