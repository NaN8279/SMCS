package io.github.nan8279.classic_server;

import io.github.nan8279.classic_server.anti_cheat.AntiBlockSpam;
import io.github.nan8279.classic_server.anti_cheat.AntiSpam;
import io.github.nan8279.classic_server.anti_cheat.InvalidBlock;
import io.github.nan8279.classic_server.anti_cheat.InvalidMovement;
import io.github.nan8279.classic_server.commands.MessageHandler;
import io.github.nan8279.config.FileConfig;
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

        FileConfig config = new FileConfig();
        config.addValue("check-invalid-movement", false);
        config.addValue("check-invalid-client", false);
        config.addValue("check-invalid-block-placement", false);

        if (!Files.exists(Path.of("./server-config.txt"))) {
            try {
                config.write("./server-config.txt");
            } catch (IOException exception) {
                System.out.println("Couldn't write config file!");
                exception.printStackTrace();
                return;
            }
        }

        server.getEventManager().addEventHandler(new InvalidMovement());
        // server.getEventManager().addEventHandler(new InvalidClient());
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
