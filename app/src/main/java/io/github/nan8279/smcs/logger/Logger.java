package io.github.nan8279.smcs.logger;

public class Logger {

    public void info(String info){
        System.out.println("[INFO] " + info);
    }

    public void error(String errorMessage, Exception exception) {
        System.out.println("[ERROR] " + errorMessage);
        exception.printStackTrace();
    }
}
