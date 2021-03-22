package io.github.nan8279.classic_server.commands;

import io.github.nan8279.smcs.event_manager.events.MessageEvent;
import io.github.nan8279.smcs.exceptions.ClientDisconnectedException;
import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;

public enum Commands {
    STOP_COMMAND(new StopCommand(), "Stops the server.", "/stop");

    final private Command handler;
    final private String helpMessage;
    final private String usageMessage;
    Commands(Command handler, String helpMessage, String usageMessage) {
        this.handler = handler;
        this.helpMessage = helpMessage;
        this.usageMessage = usageMessage;
    }

    public Command getHandler() {
        return handler;
    }

    public String getHelpMessage() {
        return helpMessage;
    }

    public String getUsageMessage() {
        return usageMessage;
    }

    public static void executeCommand(MessageEvent event) {
        String commandName = event.getMessage().split(" ")[0].replaceFirst("/", "");

        if (commandName.equals("help")) {
            String helpMessage = generateHelpMessage();

            for (String line : helpMessage.split("\n")) {
                try {
                    event.getPlayer().sendMessage(line);
                } catch (ClientDisconnectedException | StringToBigToConvertException ignored) {}
            }
            return;
        }

        for (Commands command : Commands.values()) {
            if (command.getHandler().getName().equals(commandName)) {
                command.getHandler().run(event);
                return;
            }
        }
    }

    public static String generateHelpMessage() {
        StringBuilder helpMessage = new StringBuilder("&cAvailable commands:\n");

        for (Commands command : Commands.values()) {
            helpMessage.append("&c/").append(command.getHandler().getName()).append(": ");
            helpMessage.append(command.getHelpMessage()).append("\n");
        }

        return helpMessage.toString();
    }
}
