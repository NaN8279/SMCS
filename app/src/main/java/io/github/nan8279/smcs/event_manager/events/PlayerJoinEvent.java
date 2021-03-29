package io.github.nan8279.smcs.event_manager.events;

import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.player.Player;

/**
 * Occurs when a player joins the server.
 */
public class PlayerJoinEvent extends Event {
    public PlayerJoinEvent(Player player, ClientBoundPacket packet) {
        super(player, packet);
    }
}
