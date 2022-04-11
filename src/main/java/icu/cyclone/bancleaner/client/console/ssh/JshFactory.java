package icu.cyclone.bancleaner.client.console.ssh;

import icu.cyclone.bancleaner.configuration.properties.SshConnectionProperties;
import icu.cyclone.bancleaner.exception.SshClientException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.PostConstruct;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class JshFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecChannelManager.class);

    private final SshConnectionProperties connectionProperties;
    private String password;

    @Generated
    @PostConstruct
    private void initPassword() {
        LOGGER.info("Loading ssh client properties");
        var passwordPath = Path.of(connectionProperties.getPasswordPath(), connectionProperties.getPasswordFile());
        try {
            password = Files.readString(passwordPath).trim();
        } catch (IOException e) {
            throw new SshClientException("Couldn't read password file: '" + passwordPath + "'", e);
        }
    }

    @Generated
    public SessionManager getSessionManager(SshConnectionProperties connectionProperties) {
        return new SessionManager(connectionProperties, password);
    }

    @Generated
    public ExecChannelManager getExecChannelManager(SessionManager sessionManager) {
        return new ExecChannelManager(sessionManager);
    }
}
