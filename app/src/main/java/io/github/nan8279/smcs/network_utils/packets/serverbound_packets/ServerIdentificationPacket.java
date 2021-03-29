package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;
import io.github.nan8279.smcs.network_utils.ServerPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

/**
 * Server identification packet.
 */
public class ServerIdentificationPacket implements ServerBoundPacket {
    final private byte protocolVersion;
    final private String serverName;
    final private String serverMOTD;
    final private boolean operator;

    /**
     * @param protocolVersion the protocol version of the server.
     * @param serverName the server name.
     * @param serverMOTD the server MOTD.
     * @param operator if the player is operator.
     */
    public ServerIdentificationPacket(byte protocolVersion, String serverName,
                                      String serverMOTD, boolean operator) {
        this.protocolVersion = protocolVersion;
        this.serverName = serverName;
        this.serverMOTD = serverMOTD;
        this.operator = operator;
    }

    @Override
    public byte returnPacketID() {
        return 0;
    }

    @Override
    public ServerPacket returnPacket() throws StringToBigToConvertException {
        ServerPacket packet = new ServerPacket();

        packet.addByte(protocolVersion);
        packet.addString(serverName);
        packet.addString(serverMOTD);
        packet.addByte((byte) (operator ? 0x64 : 0x00));

        return packet;
    }
}
