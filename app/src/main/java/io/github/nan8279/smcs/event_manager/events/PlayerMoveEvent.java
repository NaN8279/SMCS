package io.github.nan8279.smcs.event_manager.events;

import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.player.Player;
import io.github.nan8279.smcs.position.PlayerPosition;

/**
 * Occurs when a player moves.
 */
public class PlayerMoveEvent extends Event {
    final private PlayerPosition oldPosition;
    private PlayerPosition newPosition;
    private boolean canceled = false;

    /**
     * @param newPosition the new position of the player.
     * @param oldPosition the old position of the player.
     */
    public PlayerMoveEvent(Player player, ClientBoundPacket packet,
                           PlayerPosition newPosition, PlayerPosition oldPosition) {
        super(player, packet);
        this.newPosition = newPosition;
        this.oldPosition = oldPosition;
    }

    /**
     * @return the new position of the player.
     */
    public PlayerPosition getNewPosition() {
        return newPosition;
    }

    /**
     * @return the old position of the player.
     */
    public PlayerPosition getOldPosition() {
        return oldPosition;
    }

    /**
     * Sets the new position of the player.
     *
     * @param newPosition the new position of the player.
     */
    public void setNewPosition(PlayerPosition newPosition) {
        this.newPosition = newPosition;
    }

    /**
     * @return if the event has been canceled.
     */
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * @param canceled if the event must be canceled.
     */
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
