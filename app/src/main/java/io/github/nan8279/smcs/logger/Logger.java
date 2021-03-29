package io.github.nan8279.smcs.logger;

/**
 * Used for logging.
 */
public class Logger {

    /**
     * Prints an info message.
     *
     * @param info the message to print.
     */
    public void info(String info){
        System.out.println("[INFO] " + info);
    }

    /**
     * Prints an error message.
     *
     * @param errorMessage the message to print.
     * @param exception the exception that caused the error.
     */
    public void error(String errorMessage, Exception exception) {
        System.out.println("[ERROR] " + errorMessage);
        exception.printStackTrace();
    }
}
