package io.github.nan8279.smcs.exceptions;

public class InvalidBlockIDException extends Exception {
    public InvalidBlockIDException() {
        super("An invalid block ID was given.");
    }
}
