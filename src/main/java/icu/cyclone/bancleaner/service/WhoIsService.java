package icu.cyclone.bancleaner.service;

import icu.cyclone.bancleaner.client.whois.IpWhoisClient;
import icu.cyclone.bancleaner.domain.HostInfo;
import icu.cyclone.bancleaner.service.converter.IpWhoIsConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WhoIsService {
    private final IpWhoisClient whoisClient;
    private final IpWhoIsConverter converter;

    public HostInfo getHostInfo(String hostIp) {
        var whois = whoisClient.getWhoIs(hostIp);
        return converter.toHostInfo(whois, hostIp);
    }
}