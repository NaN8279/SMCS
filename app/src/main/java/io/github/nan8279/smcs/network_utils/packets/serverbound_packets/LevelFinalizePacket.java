package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.network_utils.ServerPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

/**
 * Level finalize packet.
 */
public class LevelFinalizePacket implements ServerBoundPacket {
    final private short levelWidth;
    final private short levelHeight;
    final private short levelDepth;

    /**
     * @param level the level sent to the player.
     */
    public LevelFinalizePacket(ServerLevel level){
        levelWidth = (short) level.getLevelWidth();
        levelHeight = (short) level.getLevelHeight();
        levelDepth = (short) level.getLevelDepth();
    }

    @Override
    public byte returnPacketID() {
        return 4;
    }

    @Override
    public ServerPacket returnPacket() {
        ServerPacket packet = new ServerPacket();

        packet.addShort(levelWidth);
        packet.addShort(levelHeight);
        packet.addShort(levelDepth);

        return packet;
    }
}
