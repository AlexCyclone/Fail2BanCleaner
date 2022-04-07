package icu.cyclone.bancleaner.client.storage;

import icu.cyclone.bancleaner.client.storage.dto.StorageHostInfoDto;
import java.util.List;
import java.util.Optional;

public interface StorageClient {
    void save(Iterable<StorageHostInfoDto> hostInfoList);

    Optional<List<StorageHostInfoDto>> fetch();
}
