package io.github.nan8279.smcs.exceptions;

public class ClientDisconnectedException extends Exception {
    public ClientDisconnectedException() {
        super("The client socket closed.");
    }
}
