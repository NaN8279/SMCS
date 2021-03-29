package io.github.nan8279.smcs.CPE.extensions;

import io.github.nan8279.smcs.CPE.AbstractExtension;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.exceptions.ClientDisconnectedException;
import io.github.nan8279.smcs.level.generator.TerrainGenerator;
import io.github.nan8279.smcs.level.generator.indev_map_themes.Hell;
import io.github.nan8279.smcs.level.generator.indev_map_themes.Woods;
import io.github.nan8279.smcs.network_utils.ServerPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

/**
 * The EnvColors CPE extension.
 */
public class EnvColorsExtension extends AbstractExtension {

    public EnvColorsExtension() {
        super("EnvColors", 1);
    }

    @Override
    public void onEvent(Event event) {
        try {
            TerrainGenerator generator = event.getPlayer().getServer().getLevel().getGenerator();

            if (generator instanceof Hell) {
                event.getPlayer().send(new EnvSetColorPacket(
                        EnvSetColorPacket.Variable.DIFFUSE_LIGHT,
                        (short) 13, (short) 13, (short) 13));
                event.getPlayer().send(new EnvSetColorPacket(
                        EnvSetColorPacket.Variable.AMBIENT_LIGHT,
                        (short) 13, (short) 13, (short) 13));
            } else if (generator instanceof Woods) {
                event.getPlayer().send(new EnvSetColorPacket(
                        EnvSetColorPacket.Variable.DIFFUSE_LIGHT,
                        (short) 155, (short) 155, (short) 155));
            }
        } catch (ClientDisconnectedException ignored) {}
    }

    /**
     * EnvSetColor packet.
     */
    static class EnvSetColorPacket implements ServerBoundPacket {
        final private Variable variable;
        final private short red;
        final private short green;
        final private short blue;

        /**
         * @param variable what variable the client needs to set the color for.
         * @param red red color.
         * @param green green color.
         * @param blue blue color.
         */
        EnvSetColorPacket(Variable variable, short red, short green, short blue) {
            this.variable = variable;
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        @Override
        public byte returnPacketID() {
            return 25;
        }

        @Override
        public ServerPacket returnPacket() {
            ServerPacket packet = new ServerPacket();

            packet.addByte(variable.value);

            packet.addShort(red);
            packet.addShort(green);
            packet.addShort(blue);

            return packet;
        }

        /**
         * Different variables the client can set a color for.
         */
        enum Variable {
            SKY_COLOR((byte) 0),
            CLOUD_COLOR((byte) 1),
            FOG_COLOR((byte) 2),
            AMBIENT_LIGHT((byte) 3),
            DIFFUSE_LIGHT((byte) 4);

            final private byte value;
            Variable(byte value) {
                this.value = value;
            }
        }
    }
}
