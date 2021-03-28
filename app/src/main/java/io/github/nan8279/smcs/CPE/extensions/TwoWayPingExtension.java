package io.github.nan8279.smcs.CPE.extensions;

import io.github.nan8279.smcs.CPE.AbstractExtension;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.exceptions.InvalidPacketException;
import io.github.nan8279.smcs.network_utils.NetworkUtils;
import io.github.nan8279.smcs.network_utils.Packet;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class TwoWayPingExtension extends AbstractExtension {

    public TwoWayPingExtension() {
        super("TwoWayPing", 1);
    }

    @Override
    public void onEvent(Event event) {}

    public static class TwoWayPingClientPacket implements ClientBoundPacket {
        private byte direction;
        private short data;

        @Override
        public void fromPacket(Packet packet) throws InvalidPacketException {
            direction = packet.readByte();
            data = packet.readShort();
        }

        public byte getDirection() {
            return direction;
        }

        public short getData() {
            return data;
        }
    }

    public static class TwoWayPingServerPacket implements ServerBoundPacket {
        final private byte direction;
        final private byte[] data;

        public TwoWayPingServerPacket(byte direction, short data) {
            this.direction = direction;
            this.data = NetworkUtils.shortToBytes(data);
        }

        public TwoWayPingServerPacket() {
            direction = 1;

            data = new byte[4];
            ThreadLocalRandom.current().nextBytes(data);
        }

        @Override
        public byte returnPacketID() {
            return 43;
        }

        @Override
        public ArrayList<Byte> returnFields() {
            ArrayList<Byte> fields = new ArrayList<>();

            fields.add(direction);

            fields.add(data[0]);
            fields.add(data[1]);

            return fields;
        }
    }
}
