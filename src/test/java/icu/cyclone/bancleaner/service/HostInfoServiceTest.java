package icu.cyclone.bancleaner.service;

import icu.cyclone.bancleaner.configuration.properties.StorageProperties;
import icu.cyclone.bancleaner.dataservice.HostInfoDataService;
import icu.cyclone.bancleaner.domain.HostInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static icu.cyclone.bancleaner.TestObjects.TEMPLATE_HOST_INFO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {HostInfoService.class})
@EnableConfigurationProperties(StorageProperties.class)
@ExtendWith(SpringExtension.class)
class HostInfoServiceTest {

    @Autowired
    public HostInfoService hostInfoService;

    @Autowired
    public StorageProperties properties;

    @MockBean
    public HostInfoDataService dataService;

    @MockBean
    public WhoIsService whoIsService;

    @MockBean
    public CalendarService calendarService;

    @Test
    public void storeDatabaseTest() {
        hostInfoService.storeDatabase();
        verify(dataService, times(1)).storeDatabase();
    }

    @Test
    public void unbanCountIncrementTest() {
        ArgumentCaptor<HostInfo> argument = ArgumentCaptor.forClass(HostInfo.class);

        hostInfoService.unbanCountIncrement(TEMPLATE_HOST_INFO);

        verify(dataService).save(argument.capture());
        assertEquals(TEMPLATE_HOST_INFO.getUnbannedCount() + 1, argument.getValue().getUnbannedCount());
    }
}