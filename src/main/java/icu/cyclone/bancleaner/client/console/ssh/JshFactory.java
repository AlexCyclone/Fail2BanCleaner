package icu.cyclone.bancleaner.client.console.ssh;

import icu.cyclone.bancleaner.configuration.properties.SshConnectionProperties;
import lombok.Generated;
import org.springframework.stereotype.Component;

@Component
class JshFactory {

    @Generated
    public SessionManager getSessionManager(SshConnectionProperties connectionProperties) {
        return new SessionManager(connectionProperties);
    }

    @Generated
    public ExecChannelManager getExecChannelManager(SessionManager sessionManager) {
        return new ExecChannelManager(sessionManager);
    }
}
