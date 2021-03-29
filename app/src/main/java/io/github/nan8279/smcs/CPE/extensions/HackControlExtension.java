package io.github.nan8279.smcs.CPE.extensions;

import io.github.nan8279.smcs.CPE.AbstractExtension;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.event_manager.events.PlayerJoinEvent;
import io.github.nan8279.smcs.exceptions.ClientDisconnectedException;
import io.github.nan8279.smcs.network_utils.ServerPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

/**
 * The HackControl CPE extension.
 */
public class HackControlExtension extends AbstractExtension {
    final private static boolean allowThirdPerson = true;
    final private static boolean allowRespawn = true;
    final private static short jumpHeight = -1;
    private static boolean allowFlying = true;
    private static boolean allowNoClip = true;
    private static boolean allowSpeed = true;

    public HackControlExtension() {
        super("HackControl", 1);
    }

    /**
     * Sets if clients are allowed to fly.
     *
     * @param allowFlying if clients are allowed to fly.
     */
    public static void setAllowFlying(boolean allowFlying) {
        HackControlExtension.allowFlying = allowFlying;
    }

    /**
     * Sets if clients are allowed to no clip.
     *
     * @param allowNoClip if clients are allowed to no clip.
     */
    public static void setAllowNoClip(boolean allowNoClip) {
        HackControlExtension.allowNoClip = allowNoClip;
    }

    /**
     * Sets if clients are allowed to use speed hacks.
     *
     * @param allowSpeed if clients are allowed to use speed hacks.
     */
    public static void setAllowSpeed(boolean allowSpeed) {
        HackControlExtension.allowSpeed = allowSpeed;
    }

    @Override
    public void onEvent(Event event) {
        if (!(event instanceof PlayerJoinEvent)) {
            return;
        }

        try {
            event.getPlayer().send(new HackControlPacket());
        } catch (ClientDisconnectedException ignored) {}
    }

    /**
     * The HackControl packet.
     */
    static class HackControlPacket implements ServerBoundPacket {

        @Override
        public byte returnPacketID() {
            return 32;
        }

        @Override
        public ServerPacket returnPacket() {
            ServerPacket packet = new ServerPacket();

            packet.addByte((byte) (allowFlying ? 1 : 0));
            packet.addByte((byte) (allowNoClip ? 1 : 0));
            packet.addByte((byte) (allowSpeed ? 1 : 0));
            packet.addByte((byte) (allowRespawn ? 1 : 0));
            packet.addByte((byte) (allowThirdPerson ? 1 : 0));

            packet.addShort(jumpHeight);

            return packet;
        }
    }
}
