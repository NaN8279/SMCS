package io.github.nan8279.smcs.CPE.extensions;

import io.github.nan8279.smcs.CPE.AbstractExtension;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.event_manager.events.PlayerJoinEvent;
import io.github.nan8279.smcs.exceptions.ClientDisconnectedException;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.network_utils.ServerPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

import java.util.ArrayList;

/**
 * The BlockPermissions CPE extension.
 */
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

    /**
     * Adds a special block to be allowed to be used by a client. (e.g. allow a client to place and break water.)
     *
     * @param allowed the block to be allowed.
     */
    public static void addSpecialAllowedBlock(Block allowed) {
        specialAllowedBlocks.add(allowed);
    }

    /**
     * SetBlockPermissions packet.
     */
    static class SetBlockPermissionsPacket implements ServerBoundPacket {
        final private Block block;
        final private boolean allowPlacement;
        final private boolean allowDeletion;

        /**
         * @param block the block to include in the packet.
         * @param allowPlacement if the client is allowed to place the block.
         * @param allowDeletion if the client is allowed to delete the block.
         */
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
        public ServerPacket returnPacket() {
            ServerPacket packet = new ServerPacket();

            packet.addByte(block.blockID);
            packet.addByte((byte) (allowPlacement ? 1 : 0));
            packet.addByte((byte) (allowDeletion ? 1 : 0));

            return packet;
        }
    }
}
