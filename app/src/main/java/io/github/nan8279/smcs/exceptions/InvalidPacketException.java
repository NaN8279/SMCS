package io.github.nan8279.smcs.exceptions;

public class InvalidPacketException extends Exception {
    public InvalidPacketException() {
        super("An invalid packet byte array has been given.");
    }
}
