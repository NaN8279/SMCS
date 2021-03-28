package io.github.nan8279.smcs.CPE.extensions;

import io.github.nan8279.smcs.CPE.AbstractExtension;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.event_manager.events.PlayerJoinEvent;
import io.github.nan8279.smcs.exceptions.ClientDisconnectedException;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

import java.util.ArrayList;

public class BlockPermissionsExtension extends AbstractExtension {
    final private static ArrayList<Block> specialAllowedBlocks = new ArrayList<>();

    public BlockPermissionsExtension() {
        super("BlockPermissions", 1);
    }

    @Override
    public void onEvent(Event event) {
        if (!(event instanceof PlayerJoinEvent)) {
            return;
        }

        for (Block allowedBlock : specialAllowedBlocks) {
            try {
                event.getPlayer().send(new SetBlockPermissionsPacket(allowedBlock,
                        true, true));
            } catch (ClientDisconnectedException ignored) {}
        }
    }

    public static void addSpecialAllowedBlock(Block allowed) {
        specialAllowedBlocks.add(allowed);
    }

    static class SetBlockPermissionsPacket implements ServerBoundPacket {
        final private Block block;
        final private boolean allowPlacement;
        final private boolean allowDeletion;

        public SetBlockPermissionsPacket(Block block, boolean allowPlacement, boolean allowDeletion) {
            this.block = block;
            this.allowPlacement = allowPlacement;
            this.allowDeletion = allowDeletion;
        }

        @Override
        public byte returnPacketID() {
            return 28;
        }

        @Override
        public ArrayList<Byte> returnFields() {
            ArrayList<Byte> fields = new ArrayList<>();

            fields.add(block.blockID);
            fields.add((byte) (allowPlacement ? 1 : 0));
            fields.add((byte) (allowDeletion ? 1 : 0));

            return fields;
        }
    }
}
