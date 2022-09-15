package icu.cyclone.bancleaner.service;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static icu.cyclone.bancleaner.TestObjects.TEMPLATE_HOST_INFO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BanService.class, CalendarService.class})
@ExtendWith(SpringExtension.class)
class BanServiceTest {

    @Autowired
    public BanService banService;

    @MockBean
    public HostInfoService hostInfoService;

    @MockBean
    public Fail2BanService fail2BanService;

    @MockBean
    public UnbanDecisionService decisionService;

    @Test
    public void autoCleanWithoutUnbanTest() {
        when(fail2BanService.fetchBannedIpSet()).thenReturn(Set.of("8.8.8.8"));
        when(hostInfoService.getHostInfo(anyString())).thenReturn(TEMPLATE_HOST_INFO);
        when(decisionService.isUnban(any())).thenReturn(false);

        banService.autoClean();

        verify(fail2BanService, times(1)).fetchBannedIpSet();
        verify(hostInfoService, times(1)).getHostInfo(anyString());

        verify(fail2BanService, times(0)).unban(anyString());
        verify(hostInfoService, times(0)).unbanCountIncrement(any());
    }

    @Test
    public void autoCleanWithUnbanTest() {
        when(fail2BanService.fetchBannedIpSet()).thenReturn(Set.of("8.8.8.8"));
        when(hostInfoService.getHostInfo(anyString())).thenReturn(TEMPLATE_HOST_INFO);
        when(decisionService.isUnban(any())).thenReturn(true);

        banService.autoClean();

        verify(fail2BanService, times(1)).fetchBannedIpSet();
        verify(hostInfoService, times(1)).getHostInfo(anyString());

        verify(fail2BanService, times(1)).unban(anyString());
        verify(hostInfoService, times(1)).unbanCountIncrement(any());
    }
}