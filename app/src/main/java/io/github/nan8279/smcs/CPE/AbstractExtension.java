package io.github.nan8279.smcs.CPE;

import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.exceptions.InvalidPacketException;
import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;
import io.github.nan8279.smcs.network_utils.NetworkUtils;
import io.github.nan8279.smcs.network_utils.Packet;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

import java.util.ArrayList;

public abstract class AbstractExtension {
    final private String name;
    final private int version;

    protected AbstractExtension(String name, int version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public abstract void onEvent(Event event);

    public static class ExtInfoPacket implements ServerBoundPacket {
        final private short extensionCount;

        public ExtInfoPacket(short extensionCount) {
            this.extensionCount = extensionCount;
        }

        @Override
        public byte returnPacketID() {
            return 16;
        }

        @Override
        public ArrayList<Byte> returnFields() {
            ArrayList<Byte> fields = new ArrayList<>();

            try {
                for (byte b : NetworkUtils.generateString("SMCS")) {
                    fields.add(b);
                }
            } catch (StringToBigToConvertException ignored) {}

            fields.add(NetworkUtils.shortToBytes(extensionCount)[0]);
            fields.add(NetworkUtils.shortToBytes(extensionCount)[1]);

            return fields;
        }
    }

    public static class ExtEntryPacket implements ServerBoundPacket {
        final private AbstractExtension extension;
        final private byte[] extensionName;

        public ExtEntryPacket(AbstractExtension extension) throws StringToBigToConvertException {
            this.extension = extension;
            this.extensionName = NetworkUtils.generateString(extension.name);
        }

        @Override
        public byte returnPacketID() {
            return 17;
        }

        @Override
        public ArrayList<Byte> returnFields() {
            ArrayList<Byte> fields = new ArrayList<>();

            for (byte b : extensionName) {
                fields.add(b);
            }

            fields.add(NetworkUtils.intToBytes(extension.version)[0]);
            fields.add(NetworkUtils.intToBytes(extension.version)[1]);
            fields.add(NetworkUtils.intToBytes(extension.version)[2]);
            fields.add(NetworkUtils.intToBytes(extension.version)[3]);

            return fields;
        }
    }

    public static class ClientExtEntryPacket implements ClientBoundPacket {
        private String extensionName;
        private int version;

        @Override
        public void fromPacket(Packet packet) throws InvalidPacketException {
            extensionName = packet.readString();
            version = packet.readInt();
        }

        public String getExtensionName() {
            return extensionName;
        }

        public int getVersion() {
            return version;
        }
    }

    public static class ClientExtInfoPacket implements ClientBoundPacket {
        private short extensionCount;
        private String appName;

        @Override
        public void fromPacket(Packet packet) throws InvalidPacketException {
            appName = packet.readString();
            extensionCount = packet.readShort();
        }

        public short getExtensionCount() {
            return extensionCount;
        }

        public String getAppName() {
            return appName;
        }
    }
}
