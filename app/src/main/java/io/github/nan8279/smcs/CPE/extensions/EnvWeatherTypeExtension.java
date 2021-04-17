package io.github.nan8279.smcs.CPE.extensions;

import io.github.nan8279.smcs.CPE.AbstractExtension;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.exceptions.ByteArrayToBigToConvertException;
import io.github.nan8279.smcs.exceptions.ClientDisconnectedException;
import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;
import io.github.nan8279.smcs.network_utils.ServerPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;
import io.github.nan8279.smcs.player.NPC;
import io.github.nan8279.smcs.player.Player;
import io.github.nan8279.smcs.server.Server;

/**
 * The EnvWeatherType CPE extension.
 */
public class EnvWeatherTypeExtension extends AbstractExtension {

    public EnvWeatherTypeExtension() {
        super("EnvWeatherType", 1);
    }

    /**
     * EnvSetWeatherType packet.
     */
    static class EnvSetWeatherTypePacket implements ServerBoundPacket {
        final private WeatherType type;

        /**
         * @param type the {@link WeatherType} to set.
         */
        EnvSetWeatherTypePacket(WeatherType type) {
            this.type = type;
        }

        @Override
        public byte returnPacketID() {
            return 31;
        }

        @Override
        public ServerPacket returnPacket()
                throws StringToBigToConvertException, ByteArrayToBigToConvertException {
            ServerPacket packet = new ServerPacket();

            if (type == WeatherType.CLEAR) {
                packet.addByte((byte) 0);
            } else if (type == WeatherType.RAIN) {
                packet.addByte((byte) 1);
            } else {
                packet.addByte((byte) 2);
            }

            return packet;
        }
    }

    @Override
    public void onEvent(Event event) {}

    /**
     * Sets the weather for all CPE players on a server.
     *
     * @param server the server to set the weather on.
     * @param type the new weather type.
     */
    public static void setWeather(Server server, WeatherType type) {
        EnvSetWeatherTypePacket packet = new EnvSetWeatherTypePacket(type);

        for (NPC onlinePlayer : server.getOnlinePlayers()) {
            if (onlinePlayer instanceof Player) {
                if (((Player) onlinePlayer).supportsCPE()) {
                    try {
                        ((Player) onlinePlayer).send(packet);
                    } catch (ClientDisconnectedException ignored) {}
                }
            }
        }
    }

    public enum WeatherType {
        CLEAR,
        RAIN,
        SNOW
    }
}
