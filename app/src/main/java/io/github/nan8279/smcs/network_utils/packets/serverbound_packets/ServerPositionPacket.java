package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.network_utils.NetworkUtils;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;
import io.github.nan8279.smcs.player.NPC;
import io.github.nan8279.smcs.position.PlayerPosition;

import java.util.ArrayList;

public class ServerPositionPacket implements ServerBoundPacket {
    final private byte playerID;
    final private PlayerPosition playerPosition;

    public ServerPositionPacket(NPC NPC, PlayerPosition playerPosition) {
        if (NPC == null) {
            playerID = -1;
        } else {
            playerID = NPC.getPlayerId();
        }
        this.playerPosition = playerPosition;
    }

    @Override
    public byte returnPacketID() {
        return 8;
    }

    @Override
    public ArrayList<Byte> returnFields() {
        ArrayList<Byte> packet = new ArrayList<>();
        packet.add(playerID);
        packet.add(NetworkUtils.shortToBytes((short) (playerPosition.getPosX() * 32))[0]);
        packet.add(NetworkUtils.shortToBytes((short) (playerPosition.getPosX() * 32))[1]);

        packet.add(NetworkUtils.shortToBytes((short) (playerPosition.getPosY() * 32 + 51))[0]);
        packet.add(NetworkUtils.shortToBytes((short) (playerPosition.getPosY() * 32 + 51))[1]);

        packet.add(NetworkUtils.shortToBytes((short) (playerPosition.getPosZ() * 32))[0]);
        packet.add(NetworkUtils.shortToBytes((short) (playerPosition.getPosZ() * 32))[1]);

        packet.add(playerPosition.getYaw());
        packet.add(playerPosition.getPitch());

        return packet;
    }
}
