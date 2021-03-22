package io.github.nan8279.smcs.exceptions;

public class InvalidPacketIDException extends Exception {
    public InvalidPacketIDException() {
        super("An invalid packet ID was given.");
    }
}
