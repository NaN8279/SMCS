package io.github.nan8279.smcs.event_manager.events;

import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.player.Player;

public abstract class Event {
    final private Player player;
    final private ClientBoundPacket packet;

    public Event(Player player, ClientBoundPacket packet) {
        this.player = player;
        this.packet = packet;
    }

    public Player getPlayer() {
        return player;
    }

    public ClientBoundPacket getPacket() {
        return packet;
    }
}
