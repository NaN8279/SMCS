package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.network_utils.ServerPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;
import io.github.nan8279.smcs.player.NPC;
import io.github.nan8279.smcs.position.PlayerPosition;

/**
 * Server position packet.
 */
public class ServerPositionPacket implements ServerBoundPacket {
    final private byte playerID;
    final private PlayerPosition playerPosition;

    /**
     * @param npc the npc that is being positioned.
     * @param playerPosition the new player position.
     */
    public ServerPositionPacket(NPC npc, PlayerPosition playerPosition) {
        if (npc == null) {
            playerID = -1;
        } else {
            playerID = npc.getPlayerId();
        }
        this.playerPosition = playerPosition;
    }

    @Override
    public byte returnPacketID() {
        return 8;
    }

    @Override
    public ServerPacket returnPacket() {
        ServerPacket packet = new ServerPacket();

        packet.addByte(playerID);

        packet.addShort((short) (playerPosition.getPosX() * 32));
        packet.addShort((short) (playerPosition.getPosY() * 32 + 51));
        packet.addShort((short) (playerPosition.getPosX() * 32));

        packet.addByte(playerPosition.getYaw());
        packet.addByte(playerPosition.getPitch());

        return packet;
    }
}
