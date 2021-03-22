package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;
import io.github.nan8279.smcs.network_utils.NetworkUtils;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;
import io.github.nan8279.smcs.player.NPC;
import io.github.nan8279.smcs.position.PlayerPosition;

import java.util.ArrayList;

public class SpawnPlayerPacket implements ServerBoundPacket {
    final private byte playerID;
    final private byte[] username;
    final private PlayerPosition playerPosition;

    public SpawnPlayerPacket(NPC npc,
                             boolean thisPlayer) throws StringToBigToConvertException {
        if (thisPlayer) {
            playerID = -1;
        } else {
            playerID = npc.getPlayerId();
        }
        this.username = NetworkUtils.generateString(npc.getUsername());
        this.playerPosition = npc.getPos();
    }

    @Override
    public byte returnPacketID() {
        return 7;
    }

    @Override
    public ArrayList<Byte> returnFields() {
        ArrayList<Byte> packet = new ArrayList<>();

        packet.add(playerID);

        for (Byte b : username) {
            packet.add(b);
        }

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
