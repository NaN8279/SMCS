package io.github.nan8279.config;

public class InvalidConfigException extends Exception {

    public InvalidConfigException() {
        super("An invalid config file has been given.");
    }
}
