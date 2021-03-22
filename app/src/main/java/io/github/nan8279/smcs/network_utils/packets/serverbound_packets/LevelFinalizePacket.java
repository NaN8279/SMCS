package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.network_utils.NetworkUtils;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

import java.util.ArrayList;

public class LevelFinalizePacket implements ServerBoundPacket {
    final private short levelWidth;
    final private short levelHeight;
    final private short levelDepth;

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
    public ArrayList<Byte> returnFields() {
        ArrayList<Byte> packet = new ArrayList<>();
        packet.add(NetworkUtils.shortToBytes(levelWidth)[0]);
        packet.add(NetworkUtils.shortToBytes(levelWidth)[1]);

        packet.add(NetworkUtils.shortToBytes(levelHeight)[0]);
        packet.add(NetworkUtils.shortToBytes(levelHeight)[1]);

        packet.add(NetworkUtils.shortToBytes(levelDepth)[0]);
        packet.add(NetworkUtils.shortToBytes(levelDepth)[1]);

        return packet;
    }
}
