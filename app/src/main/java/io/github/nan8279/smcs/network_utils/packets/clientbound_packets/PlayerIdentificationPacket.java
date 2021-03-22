package io.github.nan8279.smcs.network_utils.packets.clientbound_packets;

import io.github.nan8279.smcs.exceptions.InvalidPacketException;
import io.github.nan8279.smcs.network_utils.Packet;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;

public class PlayerIdentificationPacket implements ClientBoundPacket {
    private byte protocolVersion;
    private String username;
    private String verificationKey;
    private byte unusedByte;

    @Override
    public void fromPacket(Packet packet) throws InvalidPacketException {
        protocolVersion = packet.readByte();
        username = packet.readString();
        verificationKey = packet.readString();
        unusedByte = packet.readByte();
    }

    public byte getUnusedByte() {
        return unusedByte;
    }

    public byte getProtocolVersion() {
        return protocolVersion;
    }

    public String getUsername() {
        return username;
    }

    public String getVerificationKey() {
        return verificationKey;
    }
}
