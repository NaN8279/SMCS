package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;
import io.github.nan8279.smcs.network_utils.ServerPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

/**
 * Disconnect player packet.
 */
public class DisconnectPlayerPacket implements ServerBoundPacket {
    final private String disconnectReason;

    /**
     * @param disconnectReason the reason the player has been kicked.
     */
    public DisconnectPlayerPacket(String disconnectReason) {
        this.disconnectReason = disconnectReason;
    }

    @Override
    public byte returnPacketID() {
        return 14;
    }

    @Override
    public ServerPacket returnPacket() throws StringToBigToConvertException {
        ServerPacket packet = new ServerPacket();
        packet.addString(disconnectReason);
        return packet;
    }
}
