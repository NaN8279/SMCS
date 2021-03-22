package io.github.nan8279.smcs.network_utils.packets.clientbound_packets;

import io.github.nan8279.smcs.exceptions.InvalidPacketException;
import io.github.nan8279.smcs.network_utils.Packet;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.position.PlayerPosition;

public class ClientPositionPacket implements ClientBoundPacket {
    private byte playerID;
    private PlayerPosition playerPosition;

    @Override
    public void fromPacket(Packet packet) throws InvalidPacketException {
        playerID = packet.readByte();
        double x = (double) (packet.readShort()) / 32;
        double y = (double) (packet.readShort() - 51) / 32;
        double z = (double) (packet.readShort()) / 32;
        byte yaw = packet.readByte();
        byte pitch = packet.readByte();

        playerPosition = new PlayerPosition(x, y, z, yaw, pitch);
    }

    public byte getPlayerID() {
        return playerID;
    }

    public PlayerPosition getPlayerPosition() {
        return playerPosition;
    }
}
