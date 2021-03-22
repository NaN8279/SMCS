package io.github.nan8279.classic_server.commands;

import io.github.nan8279.smcs.event_manager.events.MessageEvent;

public abstract class Command {
    final private String name;
    final private boolean requiresOperator;

    public Command(String name, boolean requiresOperator) {
        this.name = name;
        this.requiresOperator = requiresOperator;
    }

    public abstract void run(MessageEvent event);

    public String getName() {
        return name;
    }

    public boolean requiresOperator() {
        return requiresOperator;
    }
}
