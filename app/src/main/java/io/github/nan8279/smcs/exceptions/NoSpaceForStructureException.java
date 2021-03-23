package io.github.nan8279.smcs.exceptions;

public class NoSpaceForStructureException extends Exception {

    public NoSpaceForStructureException() {
        super("There is no space for a structure at the given position.");
    }
}
