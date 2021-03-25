package io.github.nan8279.smcs.level;

import io.github.nan8279.smcs.config.Config;
import io.github.nan8279.smcs.event_manager.events.SetBlockEvent;
import io.github.nan8279.smcs.exceptions.ByteArrayToBigToConvertException;
import io.github.nan8279.smcs.exceptions.ClientDisconnectedException;
import io.github.nan8279.smcs.exceptions.InvalidBlockIDException;
import io.github.nan8279.smcs.level.blocks.Block;
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
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.GZIPOutputStream;

public class ServerLevel {
    final private PlayerPosition spawnPos;
    final private int levelWidth;
    final private int levelHeight;
    final private int levelDepth;
    final private byte[] data;
    final private long seed;

    public ServerLevel(byte[] data, int width, int height, int depth, PlayerPosition spawnPos,
                       long seed){
        this.data = data;
        levelWidth = width;
        levelHeight = height;
        levelDepth = depth;
        this.spawnPos = spawnPos;
        this.seed = seed;
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

    public SetBlockEvent checkPhysic(SetBlockEvent event) {
        if (event.getBlock().physic != null) {
            event.getBlock().physic.updateBlock(event);
        }
        return event;
    }

    public boolean inLevel(BlockPosition position) {
        return 0 <= position.getPosX() && position.getPosX() < levelWidth &&
                0 <= position.getPosY() && position.getPosY() < levelHeight &&
                0 <= position.getPosZ() && position.getPosZ() < levelDepth;
    }

    public void sendWorldData(Player p) throws ClientDisconnectedException {
        try {
            byte[] worldData = data;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            GZIPOutputStream gout = new GZIPOutputStream(bout);
            DataOutputStream dout = new DataOutputStream(gout);

            dout.writeInt(worldData.length);
            dout.write(worldData);
            dout.close();
            gout.close();
            byte[] gzip = bout.toByteArray();
            bout.close();

            p.send(new LevelInitializePacket());
            int position = 0;
            int length;
            int percent;
            byte[] buffer = new byte[1024];
            while (position != gzip.length) {
                length = Math.min(gzip.length - position, 1024);
                System.arraycopy(gzip, position, buffer, 0, length);
                percent = (int) (((double) (position + length) / (double) gzip.length) * 100);
                p.send(new LevelDataChunkPacket((short) length, buffer, (byte) percent));
                position += length;
            }
            p.send(new LevelFinalizePacket(this));
        } catch (IOException | ByteArrayToBigToConvertException exception) {
            throw new ClientDisconnectedException();
        }
    }
}
