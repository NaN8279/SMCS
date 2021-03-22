package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;
import io.github.nan8279.smcs.player.NPC;

import java.util.ArrayList;

public class DespawnPlayerPacket implements ServerBoundPacket {
    final private byte playerID;

    public DespawnPlayerPacket(NPC NPC) {
        this.playerID = NPC.getPlayerId();
    }

    @Override
    public byte returnPacketID() {
        return 12;
    }

    @Override
    public ArrayList<Byte> returnFields() {
        ArrayList<Byte> packet = new ArrayList<>();
        packet.add(playerID);
        return packet;
    }
}
