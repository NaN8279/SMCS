package io.github.nan8279.smcs.exceptions;

public class TimeoutReachedException extends Exception {
    public TimeoutReachedException() {
        super("Timeout reached.");
    }
}
