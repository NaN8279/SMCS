package io.github.nan8279.smcs.network_utils.packets;

import java.util.ArrayList;

public interface ServerBoundPacket {
    byte returnPacketID();

    ArrayList<Byte> returnFields();
}
