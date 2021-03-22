package io.github.nan8279.classic_server.anti_cheat;

import io.github.nan8279.smcs.event_manager.EventHandler;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.event_manager.events.MessageEvent;
import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;
import io.github.nan8279.smcs.player.Player;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class AntiSpam implements EventHandler {
    HashMap<Player, Integer> messagesSent = new HashMap<>();

    @Override
    public void onEvent(Event event) {
        if (event instanceof MessageEvent) {
            if (messagesSent.get(event.getPlayer()) != null) {
                messagesSent.replace(event.getPlayer(), messagesSent.get(event.getPlayer()) + 1);
            } else {
                messagesSent.put(event.getPlayer(), 0);
                new Timer().schedule(new CheckMaxMessageTask(this, event.getPlayer()), 5000);
            }
        }
    }
}

class CheckMaxMessageTask extends TimerTask {
    final private AntiSpam spamCheck;
    final private Player player;

    CheckMaxMessageTask(AntiSpam spamCheck, Player player) {
        this.spamCheck = spamCheck;
        this.player = player;
    }

    @Override
    public void run() {
        if (spamCheck.messagesSent.get(player) > 5) {
            try {
                player.disconnect("Spam.", false);
            } catch (StringToBigToConvertException ignored) {}
        }

        spamCheck.messagesSent.remove(player);
    }
}
