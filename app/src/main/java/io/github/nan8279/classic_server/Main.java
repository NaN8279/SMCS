package io.github.nan8279.classic_server;

import io.github.nan8279.classic_server.anti_cheat.AntiCheat;
import io.github.nan8279.classic_server.commands.Command;
import io.github.nan8279.classic_server.commands.MessageHandler;
import io.github.nan8279.classic_server.config.FileConfig;
import io.github.nan8279.smcs.config.Config;
import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.level.generator.FlatOverworld;
import io.github.nan8279.smcs.position.BlockPosition;
import io.github.nan8279.smcs.server.Server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    private static FileConfig generateConfig(int port, String serverName, String MOTD) {
        FileConfig config = new FileConfig();

        AntiCheat.addToConfig(config);
        config.addValue("server-port", port);
        config.addValue("server-name", serverName);
        config.addValue("server-motd", MOTD);
        Config.addToConfig(config);

        return config;
    }

    private static Server readConfigFile(HashMap<String, Object> config, ServerLevel level) {

        Server server = new Server(level, (int) config.get("server-port"),
                (String) config.get("server-name"), (String) config.get("server-motd"));

        AntiCheat.addEventHandlers(config, server);
        Config.readFromConfig(config);

        return server;
    }

    public Main() {
        ServerLevel level = new FlatOverworld().generateLevel(new BlockPosition((short) 1, (short) 127, (short) 1),
                (short) 255, (short) 255, (short) 255, Block.STONE, ThreadLocalRandom.current().nextLong());

        String serverName = "My Minecraft server.";
        String MOTD = "Powered by SMCS";
        int port = 25565;

        FileConfig config = generateConfig(port, serverName, MOTD);

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


        if (!Files.exists(Path.of("./server.properties"))) {
            try {
                config.write("./server.properties");
                server = new Server(level, port, serverName, MOTD);
            } catch (IOException exception) {
                System.out.println("Error while writing to config file!");
                exception.printStackTrace();
                return;
            }
        } else {
            try {
                HashMap<String, Object> readConfig = config.read("./server.properties");
                server = readConfigFile(readConfig, level);
            } catch (Exception exception) {
                System.out.println("Error while reading config file!");
                exception.printStackTrace();
                return;
            }
        }

        server.getEventManager().addEventHandler(new MessageHandler());
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
