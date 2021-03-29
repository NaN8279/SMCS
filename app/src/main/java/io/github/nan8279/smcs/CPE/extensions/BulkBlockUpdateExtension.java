package io.github.nan8279.smcs.CPE.extensions;

import io.github.nan8279.smcs.CPE.AbstractExtension;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.event_manager.events.SetBlockEvent;
import io.github.nan8279.smcs.exceptions.ByteArrayToBigToConvertException;
import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;
import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.network_utils.NetworkUtils;
import io.github.nan8279.smcs.network_utils.ServerPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;
import io.github.nan8279.smcs.position.BlockPosition;

import java.util.ArrayList;

public class BulkBlockUpdateExtension extends AbstractExtension {

    public BulkBlockUpdateExtension() {
        super("BulkBlockUpdate", 1);
    }

    @Override
    public void onEvent(Event event) {}

    public static class BulkBlockUpdatePacket implements ServerBoundPacket {
        final private ArrayList<SetBlockEvent> blockUpdates;

        public BulkBlockUpdatePacket(ArrayList<SetBlockEvent> blockUpdates) {
            this.blockUpdates = blockUpdates;
        }

        @Override
        public byte returnPacketID() {
            return 38;
        }

        @Override
        public ServerPacket returnPacket()
                throws StringToBigToConvertException, ByteArrayToBigToConvertException {
            ServerPacket packet = new ServerPacket();

            packet.addUnsignedByte(blockUpdates.size() - 1);

            byte[] indicesArray = new byte[1024];
            int i = 0;

            for (SetBlockEvent blockUpdate : blockUpdates) {
                ServerLevel level = blockUpdate.getPlayer().getServer().getLevel();
                BlockPosition position = blockUpdate.getBlockPosition();

                indicesArray[i] = NetworkUtils.intToBytes(level.blockPositionToInt(position))[0];
                indicesArray[i + 1] = NetworkUtils.intToBytes(level.blockPositionToInt(position))[1];
                indicesArray[i + 2] = NetworkUtils.intToBytes(level.blockPositionToInt(position))[2];
                indicesArray[i + 3] = NetworkUtils.intToBytes(level.blockPositionToInt(position))[3];

                i += 4;
            }

            packet.addByteArray(indicesArray);

            byte[] blocksArray = new byte[256];
            i = 0;
            for (SetBlockEvent blockUpdate : blockUpdates) {
                blocksArray[i] = blockUpdate.getBlock().blockID;
                i++;
            }

            packet.addByteArray(indicesArray);

            return packet;
        }
    }
}
