package io.github.nan8279.smcs.network_utils.packets.clientbound_packets;

import io.github.nan8279.smcs.exceptions.InvalidPacketException;
import io.github.nan8279.smcs.network_utils.ClientPacket;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.position.PlayerPosition;

/**
 * Client position packet.
 */
public class ClientPositionPacket implements ClientBoundPacket {
    private byte playerID;
    private PlayerPosition playerPosition;

    @Override
    public void fromPacket(ClientPacket clientPacket) throws InvalidPacketException {
        playerID = clientPacket.readByte();
        double x = (double) (clientPacket.readShort()) / 32;
        double y = (double) (clientPacket.readShort() - 51) / 32;
        double z = (double) (clientPacket.readShort()) / 32;
        byte yaw = clientPacket.readByte();
        byte pitch = clientPacket.readByte();

        playerPosition = new PlayerPosition(x, y, z, yaw, pitch);
    }

    /**
     * @return the player ID in the packet.
     */
    public byte getPlayerID() {
        return playerID;
    }

    /**
     * @return the player position in the packet.
     */
    public PlayerPosition getPlayerPosition() {
        return playerPosition;
    }
}
