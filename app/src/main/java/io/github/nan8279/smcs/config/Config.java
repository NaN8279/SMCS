package io.github.nan8279.smcs.config;

import io.github.nan8279.classic_server.config.FileConfig;
import io.github.nan8279.smcs.player.NPC;

import java.util.HashMap;

public class Config {
    public static String JOIN_MESSAGE = "&e{player} joined the game";
    public static String CHAT_MESSAGE = "<{player}> {message}";
    public static String LEAVE_MESSAGE = "&e{player} left: {reason}";
    public static String KICK_MESSAGE = "&1You were kicked. Reason: &4{reason}";
    public static String DEFAULT_DISCONNECT_REASON = "disconnected";
    public static int RANDOM_TICK_SPEED = 3;

    public static String generateJoinMessage(NPC npc){
        return JOIN_MESSAGE.replace("{player}", npc.getUsername());
    }

    public static String generateChatMessage(NPC npc, String message){
        return CHAT_MESSAGE.replace("{player}", npc.getUsername()).replace("{message}", message).
                replace("%", "&").replace("&&", "%");
    }

    public static String generateLeaveMessage(NPC npc, String reason){
        return LEAVE_MESSAGE.replace("{player}", npc.getUsername()).replace("{reason}", reason);
    }

    public static String generateKickMessage(String reason){
        return KICK_MESSAGE.replace("{reason}", reason);
    }

    public static void addToConfig(FileConfig config) {
        config.addValue("join-message", JOIN_MESSAGE);
        config.addValue("chat-message", CHAT_MESSAGE);
        config.addValue("leave-message", LEAVE_MESSAGE);
        config.addValue("kick-message", KICK_MESSAGE);

        config.addValue("default-disconnect-reason", DEFAULT_DISCONNECT_REASON);

        config.addValue("random-tick-speed", RANDOM_TICK_SPEED,
                "Warning! High values increase server lag");
    }

    public static void readFromConfig(HashMap<String, Object> config) {
        JOIN_MESSAGE = (String) config.get("join-message");
        CHAT_MESSAGE = (String) config.get("chat-message");
        LEAVE_MESSAGE = (String) config.get("leave-message");
        KICK_MESSAGE = (String) config.get("kick-message");

        DEFAULT_DISCONNECT_REASON = (String) config.get("default-disconnect-reason");

        RANDOM_TICK_SPEED = (int) config.get("random-tick-speed");
    }
}
