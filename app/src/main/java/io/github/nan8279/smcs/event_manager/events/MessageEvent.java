package io.github.nan8279.smcs.event_manager.events;

import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.player.Player;

/**
 * Occurs when a player sends a message in chat.
 */
public class MessageEvent extends Event {
    final private String message;
    private boolean canceled = false;

    /**
     * @param message the message send.
     */
    public MessageEvent(Player player, ClientBoundPacket packet, String message) {
        super(player, packet);
        this.message = message;
    }

    /**
     * @return the message send.
     */
    public String getMessage() {
        return message;
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

