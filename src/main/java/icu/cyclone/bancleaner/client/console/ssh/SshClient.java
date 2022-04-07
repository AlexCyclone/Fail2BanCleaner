package icu.cyclone.bancleaner.client.console.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import icu.cyclone.bancleaner.client.console.ConsoleClient;
import icu.cyclone.bancleaner.configuration.properties.SshConnectionProperties;
import icu.cyclone.bancleaner.exception.SshClientException;
import java.io.ByteArrayOutputStream;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SshClient implements ConsoleClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(SshClient.class);
    private static final long AWAIT_STEP = 100;

    private final SshConnectionProperties connectionProperties;
    private final JshFactory jshFactory;

    public String execute(String command) {
        return execute(command, connectionProperties.getExecutionTimeoutMillis());
    }

    public String execute(String command, long timeoutMillis) {
        try (var sessionManager = jshFactory.getSessionManager(connectionProperties);
             var channelManager = jshFactory.getExecChannelManager(sessionManager)) {
            ChannelExec channel = channelManager.getChannel();

            channel.setCommand(command);
            var responseStream = new ByteArrayOutputStream();
            channel.setOutputStream(responseStream);

            LOGGER.debug("Execute shell command");
            channel.connect();
            await(channel, timeoutMillis);

            return responseStream.toString();
        } catch (JSchException e) {
            LOGGER.error("Failed during execution shell command: " + command, e);
            throw new SshClientException("Connection exception", e);
        }
    }

    private void await(ChannelExec channel, long timeout) {
        try {
            long sleepStepLeft = timeout / AWAIT_STEP;
            while (channel.isConnected() && sleepStepLeft-- > 0) {
                Thread.sleep(AWAIT_STEP);
            }
        } catch (InterruptedException e) {
            throw new SshClientException("Execution interrupted", e);
        }

        if (channel.isConnected()) {
            throw new SshClientException("SSH command execution timeout");
        }
    }
}