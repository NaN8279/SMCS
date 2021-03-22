package io.github.nan8279.smcs.network_utils.packets;

import io.github.nan8279.smcs.exceptions.InvalidPacketException;
import io.github.nan8279.smcs.network_utils.Packet;

public interface ClientBoundPacket {
    void fromPacket(Packet packet) throws InvalidPacketException;
}
