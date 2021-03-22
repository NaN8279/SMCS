package io.github.nan8279.smcs.event_manager.events;

import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.player.Player;

public class MessageEvent extends Event {
    final private String message;
    private boolean canceled = false;

    public MessageEvent(Player player, ClientBoundPacket packet, String message) {
        super(player, packet);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}

