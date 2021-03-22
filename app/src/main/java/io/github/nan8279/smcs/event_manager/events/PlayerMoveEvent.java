package io.github.nan8279.smcs.event_manager.events;

import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.player.Player;
import io.github.nan8279.smcs.position.PlayerPosition;

public class PlayerMoveEvent extends Event {
    final private PlayerPosition oldPosition;
    private PlayerPosition newPosition;
    private boolean cancelled = false;

    public PlayerMoveEvent(Player player, ClientBoundPacket packet,
                           PlayerPosition newPosition, PlayerPosition oldPosition) {
        super(player, packet);
        this.newPosition = newPosition;
        this.oldPosition = oldPosition;
    }

    public PlayerPosition getNewPosition() {
        return newPosition;
    }

    public PlayerPosition getOldPosition() {
        return oldPosition;
    }

    public void setNewPosition(PlayerPosition newPosition) {
        this.newPosition = newPosition;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
