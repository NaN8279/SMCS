package io.github.nan8279.classic_server.commands.handlers;

import io.github.nan8279.classic_server.commands.CommandException;
import io.github.nan8279.classic_server.commands.CommandHandler;
import io.github.nan8279.smcs.CPE.extensions.HeldBlockExtension;
import io.github.nan8279.smcs.event_manager.EventHandler;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.event_manager.events.MessageEvent;
import io.github.nan8279.smcs.event_manager.events.SetBlockEvent;
import io.github.nan8279.smcs.exceptions.ClientDisconnectedException;
import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.network_utils.packets.clientbound_packets.ClientSetBlockPacket;
import io.github.nan8279.smcs.player.NPC;
import io.github.nan8279.smcs.player.Player;
import io.github.nan8279.smcs.position.PlayerPosition;

import java.util.HashMap;

public class InfoCommandHandler implements CommandHandler {
    final private HashMap<Player, Block> heldBlocks = new HashMap<>();
    private boolean addedEventHandler = false;

    @Override
    public void run(MessageEvent event) throws CommandException {
        if (!addedEventHandler) {
            event.getPlayer().getServer().getEventManager().addEventHandler(new SetBlockHandler());
            addedEventHandler = true;
        }

        if (event.getMessage().split(" ").length != 2) {
            throw new CommandException("Usage: /info <name>");
        }

        String name = event.getMessage().split(" ")[1];

        for (NPC onlinePlayer : event.getPlayer().getServer().getOnlinePlayers()) {
            if (!(onlinePlayer instanceof Player)) {
                continue;
            }

            if (onlinePlayer.getUsername().equals(name)) {
                PlayerPosition playerPosition = onlinePlayer.getPos();
                Block blockHolding = ((Player) onlinePlayer).supportsCPE() ?
                        HeldBlockExtension.getBlockHolding((Player) onlinePlayer) : heldBlocks.get(onlinePlayer);

                String position =
                        playerPosition.getPosX() + ", " + playerPosition.getPosY() + ", " + playerPosition.getPosZ();

                String message =
                        "Player name: " + name + "\n" +
                        "IP address: " + ((Player) onlinePlayer).getIPAddress() + "\n" +
                        "Position: " + position + "\n" +
                        "Block holding (inaccurate): " + blockHolding + "\n" +
                        "Client (only works properly with CPE on): " + ((Player) onlinePlayer).getClient() + "\n" +
                        "Ping (only works with CPE clients and CPE on): " +
                                ((Player) onlinePlayer).getPing() + "ms";

                for (String part : message.split("\n")) {
                    try {
                        event.getPlayer().sendMessage("&b" + part);
                    } catch (ClientDisconnectedException | StringToBigToConvertException ignored) {}
                }
                return;
            }
        }

        throw new CommandException("Player not found.");
    }

    class SetBlockHandler implements EventHandler {

        @Override
        public void onEvent(Event event) {
            if (event instanceof SetBlockEvent) {
                assert event.getPacket() instanceof ClientSetBlockPacket;
                if (heldBlocks.get(event.getPlayer()) != null) {
                    heldBlocks.replace(event.getPlayer(),
                            ((ClientSetBlockPacket) event.getPacket()).getBlockHolding());
                } else {
                    heldBlocks.put(event.getPlayer(),
                            ((ClientSetBlockPacket) event.getPacket()).getBlockHolding());
                }
            }
        }
    }
}
