package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;
import io.github.nan8279.smcs.network_utils.ServerPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;
import io.github.nan8279.smcs.player.NPC;
import io.github.nan8279.smcs.position.PlayerPosition;

/**
 * Spawn player packet.
 */
public class SpawnPlayerPacket implements ServerBoundPacket {
    final private byte playerID;
    final private String username;
    final private PlayerPosition playerPosition;

    /**
     * @param npc the npc being spawned.
     * @param thisPlayer if the packet is send to the npc being spawned.
     */
    public SpawnPlayerPacket(NPC npc, boolean thisPlayer) {
        if (thisPlayer) {
            playerID = -1;
        } else {
            playerID = npc.getPlayerId();
        }
        this.username = npc.getUsername();
        this.playerPosition = npc.getPos();
    }

    @Override
    public byte returnPacketID() {
        return 7;
    }

    @Override
    public ServerPacket returnPacket() throws StringToBigToConvertException {
        ServerPacket packet = new ServerPacket();

        packet.addByte(playerID);
        packet.addString(username);

        packet.addShort((short) (playerPosition.getPosX() * 32));
        packet.addShort((short) (playerPosition.getPosY() * 32 + 51));
        packet.addShort((short) (playerPosition.getPosX() * 32));

        packet.addByte(playerPosition.getYaw());
        packet.addByte(playerPosition.getPitch());

        return packet;
    }
}
