package io.github.nan8279.classic_server.commands;

import io.github.nan8279.classic_server.commands.handlers.*;
import io.github.nan8279.smcs.event_manager.events.MessageEvent;
import io.github.nan8279.smcs.exceptions.ClientDisconnectedException;
import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;

import java.util.ArrayList;

public enum Command {
    STOP_COMMAND(new StopCommandHandler(), "stop", true, "Stops the server."),
    BAN_COMMAND(new BanCommandHandler(), "ban", true, "Bans a player."),
    KICK_COMMAND(new KickCommandHandler(), "kick", true, "Kicks a player."),
    SEED_COMMAND(new SeedCommandHandler(), "seed", false,
            "Returns the seed of this world."),
    INFO_COMMAND(new InfoCommandHandler(), "info", true,
            "Returns info about the given player.");

    final private static ArrayList<String> operators = new ArrayList<>();
    final private CommandHandler handler;
    final private String name;
    final private boolean requiresOperator;
    final private String helpMessage;
    Command(CommandHandler handler, String name, boolean requiresOperator, String helpMessage) {
        this.handler = handler;
        this.name = name;
        this.requiresOperator = requiresOperator;
        this.helpMessage = helpMessage;
    }

    public CommandHandler getHandler() {
        return handler;
    }

    public String getHelpMessage() {
        return helpMessage;
    }

    public String getName() {
        return name;
    }

    public boolean requiresOperator() {
        return requiresOperator;
    }

    public static void addOperator(String playerName) {
        operators.add(playerName);
    }

    public static void executeCommand(MessageEvent event) {
        String commandName = event.getMessage().split(" ")[0].replaceFirst("/", "");

        if (commandName.equals("help")) {
            String helpMessage = generateHelpMessage(operators.contains(event.getPlayer().getUsername()));

            for (String line : helpMessage.split("\n")) {
                try {
                    event.getPlayer().sendMessage(line);
                } catch (ClientDisconnectedException | StringToBigToConvertException ignored) {}
            }
            return;
        }

        for (Command command : Command.values()) {
            if (command.requiresOperator() && !(operators).contains(event.getPlayer().getUsername())) {
                continue;
            }

            if (command.getName().equals(commandName)) {
                try {
                    command.getHandler().run(event);
                } catch (CommandException exception) {
                    try {
                        event.getPlayer().sendMessage("&cError: " + exception.getMessage());
                    } catch (ClientDisconnectedException | StringToBigToConvertException ignored) {}
                }
                return;
            }
        }

        try {
            event.getPlayer().sendMessage("&cCommand not found!");
        } catch (ClientDisconnectedException | StringToBigToConvertException ignored) {}
    }

    public static String generateHelpMessage(boolean operator) {
        StringBuilder helpMessage = new StringBuilder("&cAvailable commands:\n");

        for (Command command : Command.values()) {
            if (command.requiresOperator() && !operator) {
                continue;
            }

            helpMessage.append("&c/").append(command.getName()).append(": ");
            helpMessage.append(command.getHelpMessage()).append("\n");
        }

        return helpMessage.toString();
    }
}
