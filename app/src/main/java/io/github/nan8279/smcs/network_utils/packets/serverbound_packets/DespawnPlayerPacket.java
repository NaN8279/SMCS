package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.network_utils.ServerPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;
import io.github.nan8279.smcs.player.NPC;

/**
 * Despawn player packet.
 */
public class DespawnPlayerPacket implements ServerBoundPacket {
    final private byte playerID;

    /**
     * @param npc the NPC to despawn.
     */
    public DespawnPlayerPacket(NPC npc) {
        this.playerID = npc.getPlayerId();
    }

    @Override
    public byte returnPacketID() {
        return 12;
    }

    @Override
    public ServerPacket returnPacket() {
        ServerPacket packet = new ServerPacket();
        packet.addByte(playerID);
        return packet;
    }
}
