package icu.cyclone.bancleaner.client.console;

public interface ConsoleClient {
    String execute(String command);

    String execute(String command, long execTimeoutMillis);
}