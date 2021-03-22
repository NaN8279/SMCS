package io.github.nan8279.smcs.config;

import io.github.nan8279.smcs.player.NPC;

public enum Message {
    JOIN_MESSAGE("&2Everyone say hi to &1{player}&2!"),
    CHAT_MESSAGE("<{player}> {message}"),
    LEAVE_MESSAGE("&1{player}&2 left: {reason}"),
    KICK_MESSAGE("&1You were kicked. Reason: &4{reason}"),
    DEFAULT_DISCONNECT_REASON("disconnected");

    final private String message;
    Message(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

    public static String generateJoinMessage(NPC npc){
        return JOIN_MESSAGE.toString().replace("{player}", npc.getUsername());
    }

    public static String generateChatMessage(NPC npc, String message){
        return CHAT_MESSAGE.toString().replace("{player}", npc.getUsername()).replace("{message}", message).
                replace("%", "&").replace("&&", "%");
    }

    public static String generateLeaveMessage(NPC npc, String reason){
        return LEAVE_MESSAGE.toString().replace("{player}", npc.getUsername()).replace("{reason}", reason);
    }

    public static String generateKickMessage(String reason){
        return KICK_MESSAGE.toString().replace("{reason}", reason);
    }
}
