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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HostInfoDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostInfoDataService.class);
    private final HostInfoRepository repository;
    private final StorageClient storageClient;
    private final StorageHostInfoDtoConverter converter;
    private long changesCount;

    @PostConstruct
    private void loadStoredData() {
        LOGGER.info("Loading stored database");
        deleteAll();
        var hostInfoList = storageClient.fetch()
            .orElseGet(Collections::emptyList)
            .stream()
            .map(converter::toHostInfo)
            .collect(Collectors.toList());
        saveAll(hostInfoList);
        changesCount = 0;
    }

    public void storeDatabase() {
        if (changesCount > 0) {
            LOGGER.info("Saving database");
            storageClient.save(
                findAll().stream()
                    .map(converter::toStorageHostInfoDto)
                    .collect(Collectors.toList())
            );
            changesCount = 0;
        } else {
            LOGGER.debug("No changes in database since last load.");
        }
    }

    public void saveAll(Iterable<HostInfo> hostInfoList) {
        repository.saveAll(hostInfoList);
        changesCount++;
    }

    public void save(HostInfo hostInfo) {
        repository.save(hostInfo);
        changesCount++;
    }

    public Optional<HostInfo> find(String ip) {
        return repository.findById(ip);
    }

    public List<HostInfo> findAll() {
        var allHostInfo = repository.findAll();
        return StreamSupport.stream(allHostInfo.spliterator(), false)
            .collect(Collectors.toList());
    }

    public void deleteAll() {
        repository.deleteAll();
        changesCount++;
    }
}