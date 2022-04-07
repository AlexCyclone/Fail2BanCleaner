package icu.cyclone.bancleaner.client.console.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import icu.cyclone.bancleaner.configuration.properties.SshConnectionProperties;
import icu.cyclone.bancleaner.exception.SshClientException;
import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {SshClient.class})
@EnableConfigurationProperties(SshConnectionProperties.class)
@ExtendWith(SpringExtension.class)
class SshClientTest {

    @Autowired
    public SshClient sshClient;

    @MockBean
    public JshFactory jshFactory;

    @MockBean
    public ExecChannelManager execChannelManager;

    @MockBean
    public SessionManager sessionManager;

    @MockBean
    public ChannelExec channelExec;

    @Test
    public void executeTest() throws Exception {
        when(jshFactory.getSessionManager(any())).thenReturn(sessionManager);
        when(jshFactory.getExecChannelManager(any())).thenReturn(execChannelManager);
        when(execChannelManager.getChannel()).thenReturn(channelExec);

        ArgumentCaptor<ByteArrayOutputStream> valueCapture = ArgumentCaptor.forClass(ByteArrayOutputStream.class);
        doNothing().when(channelExec).setOutputStream(valueCapture.capture());

        String result = sshClient.execute("ls");

        assertEquals(result, valueCapture.getValue().toString());

        verify(sessionManager, times(1)).close();
        verify(execChannelManager, times(1)).close();
    }

    @Test
    public void executeTimeoutFailTest() throws Exception {
        when(jshFactory.getSessionManager(any())).thenReturn(sessionManager);
        when(jshFactory.getExecChannelManager(any())).thenReturn(execChannelManager);
        when(execChannelManager.getChannel()).thenReturn(channelExec);

        doNothing().when(channelExec).setOutputStream(any());
        when(channelExec.isConnected()).thenReturn(true);

        assertThrows(SshClientException.class, () -> sshClient.execute("ls"));

        verify(sessionManager, times(1)).close();
        verify(execChannelManager, times(1)).close();
    }

    @Test
    public void executeInterruptFailTest() throws Exception {
        when(jshFactory.getSessionManager(any())).thenReturn(sessionManager);
        when(jshFactory.getExecChannelManager(any())).thenReturn(execChannelManager);
        when(execChannelManager.getChannel()).thenReturn(channelExec);

        doNothing().when(channelExec).setOutputStream(any());
        doAnswer((o) -> {
            Thread.currentThread().interrupt();
            return true;
        }).when(channelExec).isConnected();

        assertThrows(SshClientException.class, () -> sshClient.execute("ls"));

        verify(sessionManager, times(1)).close();
        verify(execChannelManager, times(1)).close();
    }

    @Test
    public void executeConnectionFailTest() throws Exception {
        when(jshFactory.getSessionManager(any())).thenReturn(sessionManager);
        when(jshFactory.getExecChannelManager(any())).thenReturn(execChannelManager);
        when(execChannelManager.getChannel()).thenThrow(new JSchException());

        assertThrows(SshClientException.class, () -> sshClient.execute("ls"));

        verify(sessionManager, times(1)).close();
        verify(execChannelManager, times(1)).close();
    }
}