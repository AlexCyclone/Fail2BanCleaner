package icu.cyclone.bancleaner.client.console.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import icu.cyclone.bancleaner.configuration.properties.SshConnectionProperties;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
class SessionManager implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionManager.class);
    private static final String STRICT_HK = "StrictHostKeyChecking";

    private final SshConnectionProperties connectionProperties;
    private final String password;
    private Session session = null;

    @Generated
    public Session getSession() throws JSchException {
        LOGGER.debug("Preparing SSH session");
        session = new JSch().getSession(
            connectionProperties.getUsername(),
            connectionProperties.getHost(),
            connectionProperties.getPort()
        );
        session.setPassword(password);
        session.setConfig(STRICT_HK, connectionProperties.getStrictHostKey());
        session.connect();
        LOGGER.debug("SSH session opened");
        return session;
    }

    @Generated
    @Override
    public void close() {
        if (session != null) {
            session.disconnect();
            LOGGER.debug("SSH session closed");
        }
    }
}
