package io.github.nan8279.classic_server.commands.handlers;

import io.github.nan8279.classic_server.commands.CommandException;
import io.github.nan8279.classic_server.commands.CommandHandler;
import io.github.nan8279.smcs.event_manager.events.MessageEvent;
import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;
import io.github.nan8279.smcs.player.NPC;
import io.github.nan8279.smcs.player.Player;

public class KickCommandHandler implements CommandHandler {

    @Override
    public void run(MessageEvent event) throws CommandException {
        if (event.getMessage().split(" ").length != 3) {
            throw new CommandException("Usage: /kick <name> <reason>");
        }

        String name = event.getMessage().split(" ")[1];
        String reason = event.getMessage().split(" ")[2];

        for (NPC onlinePlayer : event.getPlayer().getServer().getOnlinePlayers()) {
            if (onlinePlayer instanceof Player) {
                if (onlinePlayer.getUsername().equals(name)) {
                    try {
                        onlinePlayer.disconnect(reason, false);
                        return;
                    } catch (StringToBigToConvertException exception) {
                        throw new CommandException("The reason is too big.");
                    }
                }
            }
        }

        throw new CommandException("Player not found.");
    }
}
