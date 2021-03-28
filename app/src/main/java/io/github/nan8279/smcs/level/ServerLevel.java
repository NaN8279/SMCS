package io.github.nan8279.smcs.level;

import io.github.nan8279.smcs.config.Config;
import io.github.nan8279.smcs.event_manager.events.SetBlockEvent;
import io.github.nan8279.smcs.exceptions.ByteArrayToBigToConvertException;
import io.github.nan8279.smcs.exceptions.ClientDisconnectedException;
import io.github.nan8279.smcs.exceptions.InvalidBlockIDException;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.level.generator.Overworld;
import io.github.nan8279.smcs.level.generator.TerrainGenerator;
import io.github.nan8279.smcs.network_utils.packets.serverbound_packets.LevelDataChunkPacket;
import io.github.nan8279.smcs.network_utils.packets.serverbound_packets.LevelFinalizePacket;
import io.github.nan8279.smcs.network_utils.packets.serverbound_packets.LevelInitializePacket;
import io.github.nan8279.smcs.player.NPC;
import io.github.nan8279.smcs.player.Player;
import io.github.nan8279.smcs.position.BlockPosition;
import io.github.nan8279.smcs.position.PlayerPosition;
import io.github.nan8279.smcs.server.Server;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.GZIPOutputStream;

public class ServerLevel {
    final private int levelWidth;
    final private int levelHeight;
    final private int levelDepth;
    final private byte[] data;
    final private long seed;
    final private TerrainGenerator generator;
    private PlayerPosition spawnPos;

    public ServerLevel(byte[] data, int width, int height, int depth, long seed,
                       TerrainGenerator generator){
        this.data = data;
        levelWidth = width;
        levelHeight = height;
        levelDepth = depth;
        this.seed = seed;
        this.generator = generator;
    }

    public TerrainGenerator getGenerator() {
        return generator;
    }

    byte[] getData() {
        return data;
    }

    public void calculateSpawnPosition() {
        Random random = new Random(seed);
        int randomX = random.nextInt(levelWidth);
        int randomZ = random.nextInt(levelDepth);

        this.spawnPos = PlayerPosition.fromBlockPosition(getHighestBlockPosition(randomX, randomZ));
    }

    public long getSeed() {
        return seed;
    }

    public int getLevelWidth() {
        return levelWidth;
    }

    public int getLevelHeight() {
        return levelHeight;
    }

    public int getLevelDepth() {
        return levelDepth;
    }

    public void randomTick(Server server) {
        ArrayList<BlockPosition> randomBlockPositions = new ArrayList<>();

        int xChunks = getLevelWidth() / 16;
        int yChunks = getLevelHeight() / 16;
        int zChunks = getLevelDepth() / 16;

        int blocksToSelect = Config.RANDOM_TICK_SPEED * (xChunks + yChunks + zChunks);

        for (int i = 0; i < server.getOnlinePlayers().size(); i++) {
            NPC onlinePlayer = server.getOnlinePlayers().get(i);
            if (onlinePlayer instanceof Player) {
                int minPosX = (int) (onlinePlayer.getPos().getPosX() - 32);
                int maxPosX = (int) (onlinePlayer.getPos().getPosX() + 32);
                int minPosZ = (int) (onlinePlayer.getPos().getPosZ() - 32);
                int maxPosZ = (int) (onlinePlayer.getPos().getPosZ() + 32);

                for (int j = 0; j < blocksToSelect; j++) {
                    short randomX = (short) ThreadLocalRandom.current().nextInt(minPosX, maxPosX);
                    short randomY = (short) ThreadLocalRandom.current().nextInt(getLevelHeight());
                    short randomZ = (short) ThreadLocalRandom.current().nextInt(minPosZ, maxPosZ);
                    BlockPosition randomBlockPosition = new BlockPosition(randomX, randomY, randomZ);

                    if (getBlock(randomBlockPosition) != Block.AIR) {
                        randomBlockPositions.add(randomBlockPosition);
                    }
                }
            }
        }

        for (BlockPosition blockPosition : randomBlockPositions) {
            if (getBlock(blockPosition).randomTick != null) {
                getBlock(blockPosition).randomTick.updateBlock(server, blockPosition);
            }
        }
    }

    public PlayerPosition getSpawnPos() {
        return spawnPos;
    }

    public void setBlock(BlockPosition position, Block blockType) {
        data[(position.getPosY() * levelHeight + position.getPosZ()) *
                levelWidth + position.getPosX()] = blockType.blockID;
    }

    public Block getBlock(BlockPosition position) {
        try {
            return Block.fromID(data[(position.getPosY() * levelHeight + position.getPosZ()) *
                    levelWidth + position.getPosX()]);
        } catch (InvalidBlockIDException | ArrayIndexOutOfBoundsException exception) {
            return Block.AIR;
        }
    }

    public BlockPosition getHighestBlockPosition(int x, int z) {
        return getHighestBlockPosition(x, z, levelHeight);
    }

    public BlockPosition getHighestBlockPosition(int x, int z, int maxY) {
        BlockPosition highestBlock = new BlockPosition((short) x, (short) 0, (short) z);

        for (int y = 0; y < maxY; y++) {
            BlockPosition position = new BlockPosition((short) x, (short) y, (short) z);

            if (getBlock(position).solid) {
                highestBlock = position;
            }
        }

        return highestBlock;
    }

    public void updateBlock(SetBlockEvent event) {
        if (event.getBlock().physic != null) {
            event.getBlock().physic.updateBlock(event);
        }
    }

    public void updateNeighbours(SetBlockEvent event) {
        for (BlockPosition newPosition : getNeighbours(event.getBlockPosition())) {
            if (!getBlock(newPosition).liquid) {
                continue;
            }

            SetBlockEvent newEvent = new SetBlockEvent(
                    event.getPlayer(),
                    event.getPacket(),
                    newPosition,
                    getBlock(newPosition)
            );

            updateBlock(newEvent);
        }
    }

    private static ArrayList<BlockPosition> getNeighbours(BlockPosition position) {
        ArrayList<BlockPosition> neighbours = new ArrayList<>();

        neighbours.add(new BlockPosition(
                (short) (position.getPosX() - 1),
                position.getPosY(),
                position.getPosZ()
        ));

        neighbours.add(new BlockPosition(
                (short) (position.getPosX() + 1),
                position.getPosY(),
                position.getPosZ()
        ));

        neighbours.add(new BlockPosition(
                position.getPosX(),
                (short) (position.getPosY() - 1),
                position.getPosZ()
        ));

        neighbours.add(new BlockPosition(
                position.getPosX(),
                (short) (position.getPosY() + 1),
                position.getPosZ()
        ));

        neighbours.add(new BlockPosition(
                position.getPosX(),
                position.getPosY(),
                (short) (position.getPosZ() - 1)
        ));

        neighbours.add(new BlockPosition(
                position.getPosX(),
                position.getPosY(),
                (short) (position.getPosZ() + 1)
        ));

        return neighbours;
    }

    public boolean inLevel(BlockPosition position) {
        return 0 <= position.getPosX() && position.getPosX() < levelWidth &&
                0 <= position.getPosY() && position.getPosY() < levelHeight &&
                0 <= position.getPosZ() && position.getPosZ() < levelDepth;
    }

    public void sendWorld(Player player) throws ClientDisconnectedException {
        try {
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

            GZIPOutputStream zipOutputStream = new GZIPOutputStream(byteArray);

            DataOutputStream outputStream = new DataOutputStream(zipOutputStream);

            outputStream.writeInt(data.length);
            outputStream.write(data);
            outputStream.close();
            zipOutputStream.close();

            byte[] payload = byteArray.toByteArray();
            byteArray.close();

            player.send(new LevelInitializePacket());

            int position = 0;
            while (position != payload.length) {
                short length = (short) Math.min(payload.length - position, 1024);
                byte percent = (byte) (((double) (position + length) / (double) data.length) * 100);
                byte[] buffer = new byte[1024];

                System.arraycopy(payload, position, buffer, 0, length);
                player.send(new LevelDataChunkPacket(length, buffer, percent));
                position += length;
            }

            player.send(new LevelFinalizePacket(this));
        } catch (IOException | ByteArrayToBigToConvertException exception) {
            throw new ClientDisconnectedException();
        }
    }

    public static ServerLevel fromDatFile(String path) {
        try {
            FileInputStream inputStream = new FileInputStream(path);
            inputStream.skip(344);
            byte[] blocks = new byte[256 * 256 * 256];
            inputStream.read(blocks, 0, 4194304);
            return new ServerLevel(blocks, 256, 256, 256, 0L, new Overworld());
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
