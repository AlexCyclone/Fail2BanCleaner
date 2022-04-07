package icu.cyclone.bancleaner.service;

import icu.cyclone.bancleaner.client.whois.IpWhoisClient;
import icu.cyclone.bancleaner.service.converter.IpWhoIsConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static icu.cyclone.bancleaner.TestObjects.TEMPLATE_HOST_INFO;
import static icu.cyclone.bancleaner.TestObjects.TEMPLATE_HOST_INFO_EMPTY;
import static icu.cyclone.bancleaner.TestObjects.TEMPLATE_WHO_IS_DTO;
import static icu.cyclone.bancleaner.TestObjects.TEMPLATE_WHO_IS_DTO_ERROR;
import static icu.cyclone.bancleaner.TestObjects.TEST_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {WhoIsService.class, IpWhoIsConverter.class})
@ExtendWith(SpringExtension.class)
class WhoIsServiceTest {

    @Autowired
    public WhoIsService whoIsService;

    @MockBean
    public IpWhoisClient whoisClient;

    @MockBean
    public CalendarService calendarService;

    @BeforeEach
    public void prepareCalendar() {
        when(calendarService.getCalendar()).thenReturn(TEST_DATE);
    }

    @Test
    public void getHostInfo() {
        when(whoisClient.getWhoIs(anyString())).thenReturn(TEMPLATE_WHO_IS_DTO);
        var hostInfo = whoIsService.getHostInfo("8.8.8.8");
        assertEquals(TEMPLATE_HOST_INFO, hostInfo);
    }

    @Test
    public void getHostEmptyInfo() {
        when(whoisClient.getWhoIs(anyString())).thenReturn(TEMPLATE_WHO_IS_DTO_ERROR);
        var hostInfo = whoIsService.getHostInfo("10.8.8.8");
        assertEquals(TEMPLATE_HOST_INFO_EMPTY, hostInfo);
        assertEquals(TEMPLATE_WHO_IS_DTO_ERROR.getMessage(), hostInfo.getMessage());
    }
}