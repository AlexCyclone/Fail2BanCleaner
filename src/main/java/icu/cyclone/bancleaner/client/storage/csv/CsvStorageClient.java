package icu.cyclone.bancleaner.client.storage.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import icu.cyclone.bancleaner.client.storage.StorageClient;
import icu.cyclone.bancleaner.client.storage.dto.StorageHostInfoDto;
import icu.cyclone.bancleaner.configuration.properties.CsvStorageProperties;
import icu.cyclone.bancleaner.exception.CsvStorageException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CsvStorageClient implements StorageClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvStorageClient.class);
    private static final CsvMapper CSV_MAPPER = CsvMapper.builder()
        .defaultTimeZone(TimeZone.getDefault())
        .build();
    private static final CsvSchema SCHEMA = CSV_MAPPER
        .schemaFor(StorageHostInfoDto.class)
        .withNullValue("null")
        .withHeader();

    private final CsvStorageProperties csvStorageProperties;

    @Override
    public void save(Iterable<StorageHostInfoDto> hostInfoList) {
        if (!csvStorageProperties.isEnabled()) {
            return;
        }

        var storagePath = Path.of(csvStorageProperties.getPath(), csvStorageProperties.getFileName());
        try (var writer = Files.newBufferedWriter(storagePath)) {
            CSV_MAPPER.writer(SCHEMA)
                .writeValues(writer)
                .writeAll(hostInfoList);
        } catch (IOException e) {
            if (csvStorageProperties.isStrictMode()) {
                LOGGER.error("Can not write datafile: " + storagePath, e);
                throw new CsvStorageException("Can not write datafile: " + storagePath, e);
            } else {
                LOGGER.warn("Can not write datafile: " + storagePath, e);
            }
        }
    }

    @Override
    public Optional<List<StorageHostInfoDto>> fetch() {
        var storagePath = Path.of(csvStorageProperties.getPath(), csvStorageProperties.getFileName());
        if (!csvStorageProperties.isEnabled() || Files.notExists(storagePath)) {
            return Optional.empty();
        }

        var reader = CSV_MAPPER
            .readerFor(StorageHostInfoDto.class)
            .with(SCHEMA);

        try {
            try (Reader fileReader = Files.newBufferedReader(storagePath)) {
                MappingIterator<StorageHostInfoDto> hostInfoIterator = reader.readValues(fileReader);
                return Optional.of(hostInfoIterator.readAll());
            }
        } catch (IOException e) {
            LOGGER.warn("Can not read datafile: " + storagePath, e);
        }

        return Optional.empty();
    }
}
