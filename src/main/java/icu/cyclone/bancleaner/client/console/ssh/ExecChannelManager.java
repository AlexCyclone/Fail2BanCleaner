package icu.cyclone.bancleaner.client.console.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
class ExecChannelManager implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecChannelManager.class);
    private static final String CHANNEL_EXEC = "exec";

    private final SessionManager sessionManager;
    private ChannelExec channel = null;

    @Generated
    public ChannelExec getChannel() throws JSchException {
        Session session = sessionManager.getSession();
        channel = (ChannelExec) session.openChannel(CHANNEL_EXEC);
        LOGGER.debug("Exec channel opened");
        return channel;
    }

    @Generated
    @Override
    public void close() {
        if (channel != null) {
            channel.disconnect();
            LOGGER.debug("Exec channel closed");
        }
    }
}
