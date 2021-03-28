package io.github.nan8279.smcs.CPE.extensions;

import io.github.nan8279.smcs.CPE.AbstractExtension;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.event_manager.events.PlayerMoveEvent;
import io.github.nan8279.smcs.exceptions.InvalidBlockIDException;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.network_utils.packets.clientbound_packets.ClientPositionPacket;
import io.github.nan8279.smcs.player.Player;

import java.util.HashMap;

public class HeldBlockExtension extends AbstractExtension {
    final private static HashMap<Player, Block> heldBlocks = new HashMap<>();

    public HeldBlockExtension() {
        super("HeldBlock", 1);
    }

    public static Block getBlockHolding(Player player) {
        return heldBlocks.get(player);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof PlayerMoveEvent) {
            assert event.getPacket() instanceof ClientPositionPacket;
            try {
                if (heldBlocks.get(event.getPlayer()) != null) {
                    heldBlocks.replace(event.getPlayer(),
                            Block.fromID(((ClientPositionPacket) event.getPacket()).getPlayerID()));
                } else {
                    heldBlocks.put(event.getPlayer(),
                            Block.fromID(((ClientPositionPacket) event.getPacket()).getPlayerID()));
                }
            } catch (InvalidBlockIDException ignored) {}
        }
    }
}
