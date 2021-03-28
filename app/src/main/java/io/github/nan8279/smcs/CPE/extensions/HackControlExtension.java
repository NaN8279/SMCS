package io.github.nan8279.smcs.CPE.extensions;

import io.github.nan8279.smcs.CPE.AbstractExtension;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.event_manager.events.PlayerJoinEvent;
import io.github.nan8279.smcs.exceptions.ClientDisconnectedException;
import io.github.nan8279.smcs.network_utils.NetworkUtils;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

import java.util.ArrayList;

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

    public static void setAllowFlying(boolean allowFlying) {
        HackControlExtension.allowFlying = allowFlying;
    }

    public static void setAllowNoClip(boolean allowNoClip) {
        HackControlExtension.allowNoClip = allowNoClip;
    }

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

    static class HackControlPacket implements ServerBoundPacket {

        @Override
        public byte returnPacketID() {
            return 32;
        }

        @Override
        public ArrayList<Byte> returnFields() {
            ArrayList<Byte> fields = new ArrayList<>();

            fields.add((byte) (allowFlying ? 1 : 0));
            fields.add((byte) (allowNoClip ? 1 : 0));
            fields.add((byte) (allowSpeed ? 1 : 0));
            fields.add((byte) (allowRespawn ? 1 : 0));
            fields.add((byte) (allowThirdPerson ? 1 : 0));

            fields.add(NetworkUtils.shortToBytes(jumpHeight)[0]);
            fields.add(NetworkUtils.shortToBytes(jumpHeight)[1]);

            return fields;
        }
    }
}
