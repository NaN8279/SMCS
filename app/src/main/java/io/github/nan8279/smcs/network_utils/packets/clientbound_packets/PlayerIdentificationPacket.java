package io.github.nan8279.smcs.network_utils.packets.clientbound_packets;

import io.github.nan8279.smcs.exceptions.InvalidPacketException;
import io.github.nan8279.smcs.network_utils.ClientPacket;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;

/**
 * Player identification packet.
 */
public class PlayerIdentificationPacket implements ClientBoundPacket {
    private byte protocolVersion;
    private String username;
    private String verificationKey;
    private byte unusedByte;

    @Override
    public void fromPacket(ClientPacket clientPacket) throws InvalidPacketException {
        protocolVersion = clientPacket.readByte();
        username = clientPacket.readString();
        verificationKey = clientPacket.readString();
        unusedByte = clientPacket.readByte();
    }

    /**
     * @return the unused byte in the packet.
     */
    public byte getUnusedByte() {
        return unusedByte;
    }

    /**
     * @return the protocol version in the packet.
     */
    public byte getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * @return the username in the packet.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the verification key in the packet.
     */
    public String getVerificationKey() {
        return verificationKey;
    }
}
