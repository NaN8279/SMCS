package io.github.nan8279.smcs.CPE.extensions;

import io.github.nan8279.smcs.CPE.AbstractExtension;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.exceptions.ClientDisconnectedException;
import io.github.nan8279.smcs.level.generator.TerrainGenerator;
import io.github.nan8279.smcs.level.generator.indev_map_themes.Hell;
import io.github.nan8279.smcs.level.generator.indev_map_themes.Woods;
import io.github.nan8279.smcs.network_utils.NetworkUtils;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

import java.util.ArrayList;

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

    static class EnvSetColorPacket implements ServerBoundPacket {
        final private Variable variable;
        final private short red;
        final private short green;
        final private short blue;

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
        public ArrayList<Byte> returnFields() {
            ArrayList<Byte> fields = new ArrayList<>();

            fields.add(variable.value);

            fields.add(NetworkUtils.shortToBytes(red)[0]);
            fields.add(NetworkUtils.shortToBytes(red)[1]);

            fields.add(NetworkUtils.shortToBytes(green)[0]);
            fields.add(NetworkUtils.shortToBytes(green)[1]);

            fields.add(NetworkUtils.shortToBytes(blue)[0]);
            fields.add(NetworkUtils.shortToBytes(blue)[1]);

            return fields;
        }

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
