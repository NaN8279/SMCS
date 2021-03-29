package io.github.nan8279.smcs.CPE.extensions;

import io.github.nan8279.smcs.CPE.AbstractExtension;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.exceptions.InvalidPacketException;
import io.github.nan8279.smcs.network_utils.ClientPacket;
import io.github.nan8279.smcs.network_utils.ServerPacket;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The TwoWayPing CPE extension.
 */
public class TwoWayPingExtension extends AbstractExtension {

    public TwoWayPingExtension() {
        super("TwoWayPing", 1);
    }

    @Override
    public void onEvent(Event event) {}

    /**
     * TwoWayPing client packet.
     */
    public static class TwoWayPingClientPacket implements ClientBoundPacket {
        private byte direction;
        private short data;

        @Override
        public void fromPacket(ClientPacket clientPacket) throws InvalidPacketException {
            direction = clientPacket.readByte();
            data = clientPacket.readShort();
        }

        /**
         * @return the direction of the packet.
         */
        public byte getDirection() {
            return direction;
        }

        /**
         * @return the data of the packet.
         */
        public short getData() {
            return data;
        }
    }

    /**
     * TwoWayPing server packet.
     */
    public static class TwoWayPingServerPacket implements ServerBoundPacket {
        final private byte direction;
        final private short data;

        /**
         * @param direction the direction of the packet.
         * @param data the data of the packet.
         */
        public TwoWayPingServerPacket(byte direction, short data) {
            this.direction = direction;
            this.data = data;
        }

        /**
         * Uses a random value for data and 1 for direction.
         */
        public TwoWayPingServerPacket() {
            direction = 1;
            data = (short) ThreadLocalRandom.current().nextInt(10000);
        }

        /**
         * @return the data that will be send in the packet.
         */
        public short getData() {
            return data;
        }

        @Override
        public byte returnPacketID() {
            return 43;
        }

        @Override
        public ServerPacket returnPacket() {
            ServerPacket packet = new ServerPacket();

            packet.addByte(direction);
            packet.addShort(data);

            return packet;
        }
    }
}
