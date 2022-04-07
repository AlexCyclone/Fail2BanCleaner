package icu.cyclone.bancleaner.service;

import icu.cyclone.bancleaner.client.console.ConsoleClient;
import icu.cyclone.bancleaner.configuration.properties.CommandProperties;
import icu.cyclone.bancleaner.service.converter.ConsoleConverter;
import icu.cyclone.bancleaner.exception.Fail2BanServiceException;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static icu.cyclone.bancleaner.ResourceUtils.readFile;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {Fail2BanService.class, ConsoleConverter.class})
@EnableConfigurationProperties(CommandProperties.class)
@ExtendWith(SpringExtension.class)
public class Fail2BanServiceTest {

    private static final String PATH_CONSOLE = "client/console/";
    private static final String PATH_DATA_BANNED_340 = PATH_CONSOLE + "banned-data.txt";
    private static final String PATH_DATA_ERROR = PATH_CONSOLE + "banned-error-data.txt";

    @Autowired
    public Fail2BanService fail2BanService;

    @MockBean
    public ConsoleClient consoleClient;

    @Test
    public void fetchBannedIpSetTest() {
        when(consoleClient.execute(anyString())).thenReturn(readFile(PATH_DATA_BANNED_340));
        Set<String> bannedIpSet = fail2BanService.fetchBannedIpSet();
        assertEquals(340, bannedIpSet.size());
    }

    @Test
    public void fetchBannedIpSetFailTest() {
        when(consoleClient.execute(anyString())).thenReturn(readFile(PATH_DATA_ERROR));
        assertThrows(Fail2BanServiceException.class, () -> fail2BanService.fetchBannedIpSet());
    }

    @Test
    public void unbanTest() {
        when(consoleClient.execute(anyString())).thenReturn("1\n");
        assertDoesNotThrow(() -> fail2BanService.unban("8.8.8.8"));
    }

    @Test
    public void unbanFailTest() {
        when(consoleClient.execute(anyString())).thenReturn("\n");
        assertThrows(Fail2BanServiceException.class, () -> fail2BanService.unban("8.8.8.8"));
    }
}
