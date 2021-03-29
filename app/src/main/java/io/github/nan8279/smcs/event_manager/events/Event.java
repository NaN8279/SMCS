package io.github.nan8279.smcs.event_manager.events;

import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.player.Player;

/**
 * Abstract Event class.
 */
public abstract class Event {
    final private Player player;
    final private ClientBoundPacket packet;

    /**
     * @param player the player that is causing the event.
     * @param packet the packet that is causing the event.
     */
    public Event(Player player, ClientBoundPacket packet) {
        this.player = player;
        this.packet = packet;
    }

    /**
     * @return the player that is causing the event.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the packet that is causing the event.
     */
    public ClientBoundPacket getPacket() {
        return packet;
    }
}
