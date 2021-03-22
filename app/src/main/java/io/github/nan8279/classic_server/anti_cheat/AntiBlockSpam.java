package io.github.nan8279.classic_server.anti_cheat;

import io.github.nan8279.smcs.event_manager.EventHandler;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.event_manager.events.SetBlockEvent;
import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;
import io.github.nan8279.smcs.player.Player;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class AntiBlockSpam implements EventHandler {
    HashMap<Player, Integer> blocksPlaced = new HashMap<>();

    @Override
    public void onEvent(Event event) {
        if (event instanceof SetBlockEvent) {
            if (blocksPlaced.get(event.getPlayer()) != null) {
                blocksPlaced.replace(event.getPlayer(), blocksPlaced.get(event.getPlayer()) + 1);
            } else {
                blocksPlaced.put(event.getPlayer(), 0);
                new Timer().schedule(new CheckMaxBlockPlaced(this, event.getPlayer()), 5000);
            }
        }
    }
}

class CheckMaxBlockPlaced extends TimerTask {
    final private AntiBlockSpam spamCheck;
    final private Player player;

    CheckMaxBlockPlaced(AntiBlockSpam spamCheck, Player player) {
        this.spamCheck = spamCheck;
        this.player = player;
    }

    @Override
    public void run() {
        if (spamCheck.blocksPlaced.get(player) > 25) {
            try {
                player.disconnect("Too much block placement.", false);
            } catch (StringToBigToConvertException ignored) {}
        }

        spamCheck.blocksPlaced.remove(player);
    }
}
