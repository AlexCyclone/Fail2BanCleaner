package icu.cyclone.bancleaner.client.storage.csv;

import icu.cyclone.bancleaner.configuration.properties.CsvStorageProperties;
import icu.cyclone.bancleaner.exception.CsvStorageException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static icu.cyclone.bancleaner.TestObjects.TEMPLATE_STORAGE_HOST_INFO_DTO;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {CsvStorageClient.class})
@EnableConfigurationProperties(CsvStorageProperties.class)
@ExtendWith(SpringExtension.class)
class CsvStorageClientTest {

    @Autowired
    public CsvStorageClient client;

    @Autowired
    public CsvStorageProperties csvStorageProperties;

    @AfterEach
    public void removeFile() throws IOException {
        var storagePath = Path.of(csvStorageProperties.getPath(), csvStorageProperties.getFileName());
        Files.deleteIfExists(storagePath);
    }

    @Test
    public void saveTest() {
        var hostList = List.of(TEMPLATE_STORAGE_HOST_INFO_DTO);
        assertDoesNotThrow(() -> client.save(hostList));
    }

    @Test
    public void disableStorageTest() {
        boolean enabledState = csvStorageProperties.isEnabled();
        try {
            ReflectionTestUtils.setField(csvStorageProperties, "enabled", false);
            var hostList = List.of(TEMPLATE_STORAGE_HOST_INFO_DTO);
            assertDoesNotThrow(() -> client.save(hostList));
            var result = client.fetch();
            assertTrue(result.isEmpty());
        } finally {
            ReflectionTestUtils.setField(csvStorageProperties, "enabled", enabledState);
        }
    }

    @Test
    public void fetchIOExceptionTest() {
        String fileNameState = csvStorageProperties.getFileName();
        try {
            ReflectionTestUtils.setField(csvStorageProperties, "fileName", "");
            var result = client.fetch();
            assertTrue(result.isEmpty());
        } finally {
            ReflectionTestUtils.setField(csvStorageProperties, "fileName", fileNameState);
        }
    }

    @Test
    public void saveStrictIOExceptionTest() {
        String fileNameState = csvStorageProperties.getFileName();
        boolean strictState = csvStorageProperties.isStrictMode();
        try {
            ReflectionTestUtils.setField(csvStorageProperties, "fileName", "");
            ReflectionTestUtils.setField(csvStorageProperties, "strictMode", true);
            assertThrows(CsvStorageException.class, () -> client.save(Collections.emptyList()));
        } finally {
            ReflectionTestUtils.setField(csvStorageProperties, "fileName", fileNameState);
            ReflectionTestUtils.setField(csvStorageProperties, "strictMode", strictState);
        }
    }

    @Test
    public void saveNotStrictIOExceptionTest() {
        String fileNameState = csvStorageProperties.getFileName();
        boolean strictState = csvStorageProperties.isStrictMode();
        try {
            ReflectionTestUtils.setField(csvStorageProperties, "fileName", "");
            ReflectionTestUtils.setField(csvStorageProperties, "strictMode", false);
            assertDoesNotThrow(() -> client.save(Collections.emptyList()));
        } finally {
            ReflectionTestUtils.setField(csvStorageProperties, "fileName", fileNameState);
            ReflectionTestUtils.setField(csvStorageProperties, "strictMode", strictState);
        }
    }

    @Test
    public void saveAndFetchTest() {
        var host1 = TEMPLATE_STORAGE_HOST_INFO_DTO.toBuilder().ip("1.1.1.1").build();
        var host2 = TEMPLATE_STORAGE_HOST_INFO_DTO.toBuilder().ip("2.2.2.2").build();
        var hostList = List.of(host1, host2);

        client.save(hostList);

        var result = client.fetch();
        assertTrue(result.isPresent());

        var resultList = result.get();
        assertEquals(2, resultList.size());
        assertEquals(host1, resultList.get(0));
        assertEquals(host2, resultList.get(1));
    }

    @Test
    public void fetchEmptyTest() {
        var result = client.fetch();
        assertTrue(result.isEmpty());
    }
}