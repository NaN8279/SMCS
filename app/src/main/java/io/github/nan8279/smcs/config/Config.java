package io.github.nan8279.smcs.config;

import io.github.nan8279.smcs.player.NPC;

public class Config {
    public static String JOIN_MESSAGE = "&e{player} joined the game";
    public static String CHAT_MESSAGE = "<{player}> {message}";
    public static String LEAVE_MESSAGE = "&e{player} left: {reason}";
    public static String KICK_MESSAGE = "&1You were kicked. Reason: &4{reason}";
    public static String DEFAULT_DISCONNECT_REASON = "disconnected";
    public static int randomTickSpeed = 3;

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
}
