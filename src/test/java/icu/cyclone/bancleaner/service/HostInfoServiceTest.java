package icu.cyclone.bancleaner.service;

import icu.cyclone.bancleaner.configuration.properties.StorageProperties;
import icu.cyclone.bancleaner.dataservice.HostInfoDataService;
import icu.cyclone.bancleaner.domain.HostInfo;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void unbanCountIncrementTest() {
        ArgumentCaptor<HostInfo> argument = ArgumentCaptor.forClass(HostInfo.class);

        hostInfoService.unbanCountIncrement(TEMPLATE_HOST_INFO);

        verify(dataService).save(argument.capture());
        assertEquals(TEMPLATE_HOST_INFO.getUnbannedCount() + 1, argument.getValue().getUnbannedCount());
    }

    @Test
    public void getHostInfoTest() {
        when(dataService.find(anyString())).thenReturn(Optional.of(TEMPLATE_HOST_INFO));
        var calendar = new GregorianCalendar(2022, Calendar.JANUARY, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        when(calendarService.getCalendar()).thenReturn(calendar);
        var result = hostInfoService.getHostInfo("8.8.8.8");
        assertEquals(TEMPLATE_HOST_INFO, result);
        assertEquals(TEMPLATE_HOST_INFO.getUnbannedCount(), result.getUnbannedCount());
        assertEquals(TEMPLATE_HOST_INFO.getFirstBannedDate(), result.getFirstBannedDate());
        assertEquals(TEMPLATE_HOST_INFO.getLastUpdateDate(), result.getLastUpdateDate());
    }

    @Test
    public void getHostInfoFirstTest() {
        when(dataService.find(anyString())).thenReturn(Optional.empty());
        var calendar = new GregorianCalendar(2022, Calendar.FEBRUARY, 1);
        when(calendarService.getCalendar()).thenReturn(calendar);
        when(whoIsService.getHostInfo(anyString())).thenReturn(TEMPLATE_HOST_INFO);
        var result = hostInfoService.getHostInfo("8.8.8.8");
        assertEquals(TEMPLATE_HOST_INFO.getIp(), result.getIp());
        assertEquals(TEMPLATE_HOST_INFO.getUnbannedCount(), result.getUnbannedCount());
        assertEquals(calendar, result.getFirstBannedDate());
        assertEquals(TEMPLATE_HOST_INFO.getLastUpdateDate(), result.getLastUpdateDate());
    }

    @Test
    public void getHostInfoExpiredTest() {
        when(dataService.find(anyString())).thenReturn(Optional.of(TEMPLATE_HOST_INFO));
        var calendar = new GregorianCalendar(2022, Calendar.JANUARY, 1);
        calendar.add(Calendar.DAY_OF_MONTH, 32);
        when(calendarService.getCalendar()).thenReturn(calendar);
        when(whoIsService.getHostInfo(anyString()))
            .thenReturn(TEMPLATE_HOST_INFO.toBuilder().lastUpdateDate(calendar).build());

        var result = hostInfoService.getHostInfo("8.8.8.8");
        assertEquals(TEMPLATE_HOST_INFO.getIp(), result.getIp());
        assertEquals(TEMPLATE_HOST_INFO.getUnbannedCount(), result.getUnbannedCount());
        assertEquals(TEMPLATE_HOST_INFO.getFirstBannedDate(), result.getFirstBannedDate());
        assertEquals(calendar, result.getLastUpdateDate());
    }
}