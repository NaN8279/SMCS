package io.github.nan8279.classic_server.anti_cheat;

import io.github.nan8279.config.FileConfig;
import io.github.nan8279.smcs.CPE.extensions.HackControlExtension;
import io.github.nan8279.smcs.event_manager.EventHandler;
import io.github.nan8279.smcs.server.Server;

import java.util.HashMap;

public enum AntiCheat {
    INVALID_MOVEMENT("check-invalid-movement", new InvalidMovement(),
            "Stops players from flying or noclipping."),
    INVALID_CLIENT("check-invalid-client", new InvalidClient(),
            "Stops players from joining with hacked clients."),
    INVALID_BLOCK("check-invalid-block", new InvalidBlock(),
            "Stops players from placing blocks to far from their position or placing blocks like bedrock."),
    ANTI_CHAT_SPAM("anti-chat-spam", new AntiSpam(),
            "Stops players from spamming in chat."),
    ANTI_BLOCK_SPAM("anti-block-spam", new AntiBlockSpam(),
            "Stops players from breaking or placing too much blocks.");

    final private String configOption;
    final private EventHandler handler;
    final private String description;
    AntiCheat(String configOption, EventHandler handler, String description) {
        this.configOption = configOption;
        this.handler = handler;
        this.description = description;
    }

    public static void addToConfig(FileConfig config) {
        for (AntiCheat antiCheat : AntiCheat.values()) {
            config.addValue(antiCheat.configOption, false, antiCheat.description);
        }
    }

    public static void addEventHandlers(HashMap<String, Object> config, Server server) {
        for (AntiCheat antiCheat : AntiCheat.values()) {
            if ((boolean) config.get(antiCheat.configOption)) {
                server.getEventManager().addEventHandler(antiCheat.handler);
            }
        }

        if ((boolean) config.get("check-invalid-movement")) {
            HackControlExtension.setAllowFlying(false);
            HackControlExtension.setAllowNoClip(false);
            HackControlExtension.setAllowSpeed(false);
        }
    }
}
