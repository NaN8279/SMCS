package io.github.nan8279.smcs.CPE;

import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.exceptions.InvalidPacketException;
import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;
import io.github.nan8279.smcs.network_utils.ClientPacket;
import io.github.nan8279.smcs.network_utils.ServerPacket;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

/**
 * Abstract CPE extension.
 */
public abstract class AbstractExtension {
    final private String name;
    final private int version;

    /**
     * @param name the name of the extension.
     * @param version the version of the extension.
     */
    protected AbstractExtension(String name, int version) {
        this.name = name;
        this.version = version;
    }

    /**
     * @return the name of the extension.
     */
    public String getName() {
        return name;
    }

    /**
     * Should be called when an event happens on the server.
     *
     * @param event the event that is happening.
     */
    public abstract void onEvent(Event event);

    /**
     * ExtInfo server packet.
     */
    public static class ExtInfoPacket implements ServerBoundPacket {
        final private short extensionCount;

        /**
         * @param extensionCount how many extensions the server supports.
         */
        public ExtInfoPacket(short extensionCount) {
            this.extensionCount = extensionCount;
        }

        @Override
        public byte returnPacketID() {
            return 16;
        }

        @Override
        public ServerPacket returnPacket() {
            ServerPacket packet = new ServerPacket();

            try {
                packet.addString("SMCS");
            } catch (StringToBigToConvertException ignored) {}

            packet.addShort(extensionCount);
            return packet;
        }
    }

    /**
     * ExtEntry server packet.
     */
    public static class ExtEntryPacket implements ServerBoundPacket {
        final private AbstractExtension extension;

        /**
         * @param extension the extension to send info about.
         */
        public ExtEntryPacket(AbstractExtension extension) {
            this.extension = extension;
        }

        @Override
        public byte returnPacketID() {
            return 17;
        }

        @Override
        public ServerPacket returnPacket() {
            ServerPacket packet = new ServerPacket();

            try {
                packet.addString(extension.name);
            } catch (StringToBigToConvertException ignored) {}

            packet.addInteger(extension.version);
            return packet;
        }
    }

    /**
     * ExtEntry client packet.
     */
    public static class ClientExtEntryPacket implements ClientBoundPacket {
        private String extensionName;
        private int version;

        @Override
        public void fromPacket(ClientPacket clientPacket) throws InvalidPacketException {
            extensionName = clientPacket.readString();
            version = clientPacket.readInt();
        }

        /**
         * @return the extension's name in the packet.
         */
        public String getExtensionName() {
            return extensionName;
        }

        /**
         * @return the extension's version in the packet.
         */
        public int getVersion() {
            return version;
        }
    }

    /**
     * ExtInfo client packet.
     */
    public static class ClientExtInfoPacket implements ClientBoundPacket {
        private short extensionCount;
        private String appName;

        @Override
        public void fromPacket(ClientPacket clientPacket) throws InvalidPacketException {
            appName = clientPacket.readString();
            extensionCount = clientPacket.readShort();
        }

        /**
         * @return the extension's count the client has.
         */
        public short getExtensionCount() {
            return extensionCount;
        }

        /**
         * @return the client's name.
         */
        public String getAppName() {
            return appName;
        }
    }
}
