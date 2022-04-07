package icu.cyclone.bancleaner.dataservice;

import icu.cyclone.bancleaner.client.storage.StorageClient;
import icu.cyclone.bancleaner.domain.HostInfo;
import icu.cyclone.bancleaner.repository.HostInfoRepository;
import icu.cyclone.bancleaner.service.converter.StorageHostInfoDtoConverter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HostInfoDataService {
    private final HostInfoRepository repository;
    private final StorageClient storageClient;
    private final StorageHostInfoDtoConverter converter;

    @PostConstruct
    private void loadStoredData() {
        deleteAll();
        var hostInfoList = storageClient.fetch()
            .orElseGet(Collections::emptyList)
            .stream()
            .map(converter::toHostInfo)
            .collect(Collectors.toList());
        saveAll(hostInfoList);
    }

    public void storeDatabase() {
        storageClient.save(
            findAll().stream()
                .map(converter::toStorageHostInfoDto)
                .collect(Collectors.toList())
        );
    }

    public void saveAll(Iterable<HostInfo> hostInfoList) {
        repository.saveAll(hostInfoList);
    }

    public void save(HostInfo hostInfo) {
        repository.save(hostInfo);
    }

    public Optional<HostInfo> find(String ip) {
        return repository.findById(ip);
    }

    public List<HostInfo> findAll() {
        var allHostInfo = repository.findAll().spliterator();
        return StreamSupport.stream(allHostInfo, false)
            .collect(Collectors.toList());
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}