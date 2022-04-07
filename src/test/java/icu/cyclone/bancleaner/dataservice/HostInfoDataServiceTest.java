package icu.cyclone.bancleaner.dataservice;

import icu.cyclone.bancleaner.client.storage.StorageClient;
import icu.cyclone.bancleaner.client.storage.csv.CsvStorageClient;
import icu.cyclone.bancleaner.domain.HostInfo;
import icu.cyclone.bancleaner.service.converter.StorageHostInfoDtoConverter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static icu.cyclone.bancleaner.TestObjects.TEMPLATE_HOST_INFO;
import static icu.cyclone.bancleaner.TestObjects.TEMPLATE_STORAGE_HOST_INFO_DTO;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    HostInfoDataServiceTest.StorageTestConfiguration.class,
    HostInfoDataService.class,
    StorageHostInfoDtoConverter.class
})
@DataJpaTest
@EnableJpaRepositories(basePackages = {"icu.cyclone.bancleaner.*"})
@EntityScan("icu.cyclone.bancleaner.domain")
@Transactional
class HostInfoDataServiceTest {

    @Autowired
    public HostInfoDataService dataService;

    @Test
    public void saveTest() {
        var hostIp = "1.1.1.1";
        var hostInfo = TEMPLATE_HOST_INFO.toBuilder().ip(hostIp).build();
        dataService.save(hostInfo);
        var resultHostInfo = dataService.find(hostIp).orElse(null);
        assertNotNull(hostInfo);
        assertEquals(hostInfo, resultHostInfo);
    }

    @Test
    public void saveAllTest() {
        var hostList = Stream.of(
            TEMPLATE_HOST_INFO.toBuilder().ip("1.1.1.1").build(),
            TEMPLATE_HOST_INFO.toBuilder().ip("1.1.1.2").build()
        ).collect(Collectors.toList());
        dataService.saveAll(hostList);
        var list = dataService.findAll();
        assertEquals(3, list.size());
    }

    @Test
    public void findOneTest() {
        var hostList = Stream.of(
            HostInfo.builder().ip("1.1.1.1").build(),
            HostInfo.builder().ip("1.1.1.2").build()
        ).collect(Collectors.toList());
        dataService.saveAll(hostList);
        var result = dataService.find("1.1.1.1");
        assertTrue(result.isPresent());
        assertEquals("1.1.1.1", result.get().getIp());
    }

    @Test
    public void findNoneTest() {
        var result = dataService.find("1.1.1.1");
        assertTrue(result.isEmpty());
    }

    @Test
    public void deleteAllTest() {
        dataService.deleteAll();
        var result = dataService.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    public void storeDatabaseTest() {
        assertDoesNotThrow(() -> dataService.storeDatabase());
    }

    @TestConfiguration
    static class StorageTestConfiguration {
        @Bean
        public StorageClient getStorageClient() {
            CsvStorageClient client = Mockito.mock(CsvStorageClient.class);
            when(client.fetch())
                .thenReturn(Optional.of(List.of(TEMPLATE_STORAGE_HOST_INFO_DTO)));
            doNothing().when(client).save(anyList());
            return client;
        }
    }
}