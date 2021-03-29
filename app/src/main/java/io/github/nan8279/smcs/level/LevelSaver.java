package io.github.nan8279.smcs.level;

import io.github.nan8279.smcs.level.generator.Overworld;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Used to save a {@link ServerLevel} to a file.
 */
public class LevelSaver {

    /**
     * Saves the given server level to the given path.
     *
     * @param level the level to save.
     * @param path the path to save the level to.
     * @throws IOException when something goes wrong while saving the level.
     */
    public static void saveLevel(ServerLevel level, String path) throws IOException {
        if (!Files.exists(Path.of(path))) {
            Files.createFile(Path.of(path));
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
        DataOutputStream dataOutputStream = new DataOutputStream(gzipOutputStream);

        dataOutputStream.writeLong(level.getSeed());
        dataOutputStream.writeInt(level.getLevelWidth());
        dataOutputStream.writeInt(level.getLevelHeight());
        dataOutputStream.writeInt(level.getLevelDepth());
        dataOutputStream.write(level.getData());

        dataOutputStream.flush();

        dataOutputStream.close();
        gzipOutputStream.close();

        byte[] payload = outputStream.toByteArray();
        outputStream.close();

        Files.write(Path.of(path), payload);
    }

    /**
     * Reads a server level from the given path.
     *
     * @param path the path to read the server level from.
     * @return the server level read.
     * @throws IOException when something goes wrong while reading the given level.
     */
    public static ServerLevel readLevel(String path) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Files.readAllBytes(Path.of(path)));

        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
        DataInputStream dataInputStream = new DataInputStream(gzipInputStream);

        long seed = dataInputStream.readLong();
        int xSize = dataInputStream.readInt();
        int ySize = dataInputStream.readInt();
        int zSize = dataInputStream.readInt();

        byte[] blockData = new byte[xSize * ySize * zSize];

        for (int block = 0; block < blockData.length; block++) {
            blockData[block] = dataInputStream.readByte();
        }

        dataInputStream.close();
        gzipInputStream.close();
        inputStream.close();

        return new ServerLevel(blockData, xSize, ySize, zSize, seed, new Overworld());
    }
}
