package io.github.nan8279.classic_server.config;

public class InvalidConfigException extends Exception {

    public InvalidConfigException() {
        super("An invalid config file has been given.");
    }
}
