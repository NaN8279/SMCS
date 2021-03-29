package io.github.nan8279.classic_server;

import io.github.nan8279.classic_server.anti_cheat.AntiCheat;
import io.github.nan8279.classic_server.commands.Command;
import io.github.nan8279.classic_server.commands.MessageHandler;
import io.github.nan8279.config.FileConfig;
import io.github.nan8279.config.InvalidConfigException;
import io.github.nan8279.smcs.config.Config;
import io.github.nan8279.smcs.level.LevelSaver;
import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.level.generator.*;
import io.github.nan8279.smcs.level.generator.indev_map_themes.Hell;
import io.github.nan8279.smcs.level.generator.indev_map_themes.Paradise;
import io.github.nan8279.smcs.level.generator.indev_map_themes.Woods;
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
        WorldType.addToConfig(config);
        config.addValue("world-seed", 0L);

        return config;
    }

    private static Server readConfigFile(HashMap<String, Object> config, Block baseBlock)
            throws InvalidConfigException {

        ServerLevel level = getLevel(baseBlock, config);

        Server server = new Server(level, (int) config.get("server-port"),
                (String) config.get("server-name"), (String) config.get("server-motd"));

        AntiCheat.addEventHandlers(config, server);
        Config.readFromConfig(config);

        return server;
    }

    public Main() {
        Block baseBlock = Block.STONE;

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
                server = new Server(getLevel(baseBlock), port, serverName, MOTD);
            } catch (IOException exception) {
                System.out.println("Error while writing to config file!");
                exception.printStackTrace();
                return;
            }
        } else {
            try {
                HashMap<String, Object> readConfig = config.read("./server.properties");
                server = readConfigFile(readConfig,
                        baseBlock);
            } catch (Exception exception) {
                System.out.println("Error while reading config file!");
                exception.printStackTrace();
                return;
            }
        }

        server.getLevel().calculateSpawnPosition();
        server.getEventManager().addEventHandler(new MessageHandler());
        server.run();

        while (!server.isStopping()) {
            server.tick();
            try {
                Thread.sleep(25);
            } catch (InterruptedException ignored) {}
        }

        try {
            LevelSaver.saveLevel(server.getLevel(), "server_level.smclevel");
        } catch (IOException exception) {
            System.out.println("Couldn't save server level.");
            exception.printStackTrace();
        }
    }

    public static void main(String [] args) {
        new Main();
    }

    private static ServerLevel getLevel(Block baseBlock) {
        ServerLevel level = null;

        if (Files.exists(Path.of("server_level.smclevel"))) {
            try {
                level = LevelSaver.readLevel("server_level.smclevel");
            } catch (IOException exception) {
                System.out.println("Couldn't read server level.");
                exception.printStackTrace();
            }
        }

        if (level == null) {
            level = new Overworld().generateLevel(
                    (short) 255, (short) 255, (short) 255,
                    baseBlock,
                    ThreadLocalRandom.current().nextLong()
            );
        }

        return level;
    }

    private static ServerLevel getLevel(Block baseBlock, HashMap<String, Object> config)
            throws InvalidConfigException {
        ServerLevel level = null;

        if (Files.exists(Path.of("server_level.smclevel"))) {
            try {
                level = LevelSaver.readLevel("server_level.smclevel");
            } catch (IOException exception) {
                System.out.println("Couldn't read server level.");
                exception.printStackTrace();
            }
        }

        if (level == null) {
            level = WorldType.fromConfig(config).generateLevel(
                    (short) 255,
                    (short) 255,
                    (short) 255,
                    baseBlock,
                    (long) config.get("world-seed") == 0L ? ThreadLocalRandom.current().nextLong() :
                            (long) config.get("world-seed")
            );
        }

        return level;
    }
}

enum WorldType {
    OVERWORLD(new Overworld()),
    FLAT(new FlatWorld()),
    FLAT_OVERWORLD(new FlatOverworld()),
    AMPLIFIED(new AmplifiedOverworld()),
    HELL(new Hell()),
    PARADISE(new Paradise()),
    WOODS(new Woods());

    final private TerrainGenerator generator;
    WorldType(TerrainGenerator generator) {
        this.generator = generator;
    }

    public static void addToConfig(FileConfig config) {
        config.addValue("world-type", OVERWORLD,
                "The world type to generate.");
    }

    public static TerrainGenerator fromConfig(HashMap<String, Object> config) throws InvalidConfigException {
        String worldType = (String) config.get("world-type");

        for (WorldType world : WorldType.values()) {
            if (world.toString().equals(worldType)) {
                return world.generator;
            }
        }

        throw new InvalidConfigException();
    }
}
