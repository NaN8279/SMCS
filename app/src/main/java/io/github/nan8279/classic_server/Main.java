package io.github.nan8279.classic_server;

import io.github.nan8279.classic_server.anti_cheat.*;
import io.github.nan8279.classic_server.commands.Command;
import io.github.nan8279.classic_server.commands.MessageHandler;
import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.position.BlockPosition;
import io.github.nan8279.smcs.server.Server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public Main() {
        ServerLevel level = ServerLevel.generateFlatWorld(new BlockPosition((short) 1, (short) 127, (short) 1),
                (short) 255, (short) 255, (short) 255, Block.STONE);

        Server server = new Server(level);

        if (!Files.exists(Path.of("./operators.txt"))) {
            try {
                Files.writeString(Path.of("./operators.txt"), "");
            } catch (IOException exception) {
                System.out.println("Error while writing to operators file!");
                exception.printStackTrace();
                return;
            }
        } else {
            try {
                String operators = Files.readString(Path.of("./operators.txt"));

                for (String operator : operators.split("\n")) {
                    Command.addOperator(operator);
                }
            } catch (IOException exception) {
                System.out.println("Error while reading operators file!");
                exception.printStackTrace();
                return;
            }
        }

        server.getEventManager().addEventHandler(new InvalidMovement());
        server.getEventManager().addEventHandler(new InvalidClient());
        server.getEventManager().addEventHandler(new InvalidBlock());
        server.getEventManager().addEventHandler(new MessageHandler());
        server.getEventManager().addEventHandler(new AntiSpam());
        server.getEventManager().addEventHandler(new AntiBlockSpam());
        server.run();

        while (!server.isStopping()) {
            server.tick();
            try {
                Thread.sleep(25);
            } catch (InterruptedException ignored) {}
        }
    }

    public static void main(String [] args) {
        new Main();
    }
}
