package io.github.nan8279.smcs.network_utils.packets.serverbound_packets;

import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;
import io.github.nan8279.smcs.network_utils.NetworkUtils;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

import java.util.ArrayList;

public class ServerIdentificationPacket implements ServerBoundPacket {
    final private byte protocolVersion;
    final private byte[] serverName;
    final private byte[] serverMOTD;
    final private boolean operator;

    public ServerIdentificationPacket(byte protocolVersion, String serverName,
                                      String serverMOTD,
                                      boolean operator) throws StringToBigToConvertException {
        this.protocolVersion = protocolVersion;
        this.serverName = NetworkUtils.generateString(serverName);
        this.serverMOTD = NetworkUtils.generateString(serverMOTD);
        this.operator = operator;
    }

    @Override
    public byte returnPacketID() {
        return 0;
    }

    @Override
    public ArrayList<Byte> returnFields() {
        ArrayList<Byte> packet = new ArrayList<>();
        packet.add(protocolVersion);
        for (Byte b : serverName) {
            packet.add(b);
        }
        for (Byte b : serverMOTD) {
            packet.add(b);
        }
        packet.add((byte) (operator ? 0x64 : 0x00));

        return packet;
    }
}
