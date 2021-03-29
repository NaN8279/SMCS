package io.github.nan8279.smcs.config;

import io.github.nan8279.config.FileConfig;
import io.github.nan8279.smcs.player.NPC;

import java.util.HashMap;

/**
 * Server config.
 *
 * This contains info about how the server works.
 */
public class Config {
    /**
     * Message send when a player joins.
     */
    public static String JOIN_MESSAGE = "&e{player} joined the game";
    /**
     * Message send when a player chats.
     */
    public static String CHAT_MESSAGE = "<{player}> {message}";
    /**
     * Message send when a player leaves.
     */
    public static String LEAVE_MESSAGE = "&e{player} left: {reason}";
    /**
     * Message send when a player gets kicked.
     */
    public static String KICK_MESSAGE = "&1You were kicked. Reason: &4{reason}";
    /**
     * Message included with LEAVE_MESSAGE by default.
     */
    public static String DEFAULT_DISCONNECT_REASON = "disconnected";
    public static int RANDOM_TICK_SPEED = 3;
    public static boolean USE_CPE = false;

    /**
     * Generates a join message for the given NPC.
     *
     * @param npc the NPC to generate the message for.
     * @return the message generated.
     */
    public static String generateJoinMessage(NPC npc){
        return JOIN_MESSAGE.replace("{player}", npc.getUsername());
    }

    /**
     * Generates a chat message for the given NPC and the given chat message.
     *
     * @param npc the NPC to generate the message for.
     * @param message the chat message.
     * @return the message generated.
     */
    public static String generateChatMessage(NPC npc, String message){
        return CHAT_MESSAGE.replace("{player}", npc.getUsername()).replace("{message}", message).
                replace("%", "&").replace("&&", "%");
    }

    /**
     * Generates a leave message for the given NPC and the given reason.
     *
     * @param npc the NPC to generate the message for.
     * @param reason the reason the NPC left.
     * @return the message generated.
     */
    public static String generateLeaveMessage(NPC npc, String reason){
        return LEAVE_MESSAGE.replace("{player}", npc.getUsername()).replace("{reason}", reason);
    }

    /**
     * Generates a kick message for the given NPC and the given reason.
     *
     * @param reason the kick reason.
     * @return the message generated.
     */
    public static String generateKickMessage(String reason){
        return KICK_MESSAGE.replace("{reason}", reason);
    }

    /**
     * Adds the values in this config to a {@link FileConfig}.
     *
     * @param config the {@link FileConfig} to add the values to.
     */
    public static void addToConfig(FileConfig config) {
        config.addValue("join-message", JOIN_MESSAGE);
        config.addValue("chat-message", CHAT_MESSAGE);
        config.addValue("leave-message", LEAVE_MESSAGE);
        config.addValue("kick-message", KICK_MESSAGE);

        config.addValue("default-disconnect-reason", DEFAULT_DISCONNECT_REASON);

        config.addValue("random-tick-speed", RANDOM_TICK_SPEED,
                "Warning! High values increase server lag");

        config.addValue("use-cpe", USE_CPE,
                "Uses communication with clients like Classicube to prevent hacking.");
    }

    /**
     * Sets the values of this config to a config from {@link FileConfig}.
     *
     * @param config a config from {@link FileConfig};
     */
    public static void readFromConfig(HashMap<String, Object> config) {
        JOIN_MESSAGE = (String) config.get("join-message");
        CHAT_MESSAGE = (String) config.get("chat-message");
        LEAVE_MESSAGE = (String) config.get("leave-message");
        KICK_MESSAGE = (String) config.get("kick-message");

        DEFAULT_DISCONNECT_REASON = (String) config.get("default-disconnect-reason");

        RANDOM_TICK_SPEED = (int) config.get("random-tick-speed");

        USE_CPE = (boolean) config.get("use-cpe");
    }
}
