package io.github.nan8279.classic_server;

import io.github.nan8279.classic_server.anti_cheat.*;
import io.github.nan8279.classic_server.commands.Command;
import io.github.nan8279.classic_server.commands.MessageHandler;
import io.github.nan8279.classic_server.config.FileConfig;
import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.position.BlockPosition;
import io.github.nan8279.smcs.server.Server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Main {

    public Main() {
        ServerLevel level = ServerLevel.generateFlatWorld(new BlockPosition((short) 1, (short) 127, (short) 1),
                (short) 255, (short) 255, (short) 255, Block.STONE);

        String serverName = "My Minecraft server.";
        String MOTD = "Powered by SMCS";
        int port = 25565;

        Server server;

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

        FileConfig config = new FileConfig();

        config.addValue("check-invalid-movement", false,
                "Stops players from flying or noclipping.");
        config.addValue("check-invalid-client", false,
                "Stops players from joining with hacked clients.");
        config.addValue("check-invalid-block", false,
                "Stops players from placing blocks to far from their position or placing blocks like bedrock.");
        config.addValue("anti-chat-spam", false, "Stops players from spamming in chat.");
        config.addValue("anti-block-spam", false,
                "Stops players from breaking or placing too much blocks.");

        config.addValue("server-port", port);
        config.addValue("server-name", serverName);
        config.addValue("server-motd", MOTD);

        if (!Files.exists(Path.of("./server.properties"))) {
            try {
                config.write("./server.properties");
                server = new Server(level, port, serverName, MOTD);
            } catch (IOException exception) {
                System.out.println("Error while writing to config file!");
                exception.printStackTrace();
                return;
            }

            server.getEventManager().addEventHandler(new InvalidMovement());
            server.getEventManager().addEventHandler(new InvalidClient());
            server.getEventManager().addEventHandler(new InvalidBlock());
            server.getEventManager().addEventHandler(new MessageHandler());
            server.getEventManager().addEventHandler(new AntiSpam());
            server.getEventManager().addEventHandler(new AntiBlockSpam());
        } else {
            try {
                HashMap<String, Object> readConfig = config.read("./server.properties");

                server = new Server(level, (int) readConfig.get("server-port"),
                        (String) readConfig.get("server-name"), (String) readConfig.get("server-motd"));

                if ((boolean) readConfig.get("check-invalid-movement")) {
                    server.getEventManager().addEventHandler(new InvalidMovement());
                }

                if ((boolean) readConfig.get("check-invalid-client")) {
                    server.getEventManager().addEventHandler(new InvalidClient());
                }

                if ((boolean) readConfig.get("check-invalid-block")) {
                    server.getEventManager().addEventHandler(new InvalidBlock());
                }

                if ((boolean) readConfig.get("anti-chat-spam")) {
                    server.getEventManager().addEventHandler(new AntiSpam());
                }

                if ((boolean) readConfig.get("anti-block-spam")) {
                    server.getEventManager().addEventHandler(new AntiBlockSpam());
                }
            } catch (Exception exception) {
                System.out.println("Error while reading config file!");
                exception.printStackTrace();
                return;
            }
        }

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
