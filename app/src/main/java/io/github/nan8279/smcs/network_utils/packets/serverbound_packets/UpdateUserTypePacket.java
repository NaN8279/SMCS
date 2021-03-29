package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.network_utils.ServerPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

/**
 * Update user type packet.
 */
public class UpdateUserTypePacket implements ServerBoundPacket {
    final private boolean operator;

    /**
     * @param operator if the user is operator.
     */
    public UpdateUserTypePacket(boolean operator) {
        this.operator = operator;
    }

    @Override
    public byte returnPacketID() {
        return 15;
    }

    @Override
    public ServerPacket returnPacket() {
        ServerPacket packet = new ServerPacket();
        packet.addByte((byte) (operator ? 0x64: 0x00));
        return packet;
    }
}
