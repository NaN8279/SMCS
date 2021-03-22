package io.github.nan8279.classic_server.anti_cheat;

import io.github.nan8279.smcs.event_manager.EventHandler;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.event_manager.events.PlayerJoinEvent;
import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;
import io.github.nan8279.smcs.network_utils.packets.clientbound_packets.PlayerIdentificationPacket;

public class InvalidClient implements EventHandler {

    @Override
    public void onEvent(Event event) {
        if (event instanceof PlayerJoinEvent) {
            assert event.getPacket() instanceof PlayerIdentificationPacket;
            if (((PlayerIdentificationPacket) event.getPacket()).getUnusedByte() == 66) {
                try {
                    event.getPlayer().disconnect("Client not allowed!", false);
                } catch (StringToBigToConvertException ignored) {}
            }
        }
    }
}
