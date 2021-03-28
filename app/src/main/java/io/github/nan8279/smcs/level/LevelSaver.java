package io.github.nan8279.smcs.level;

import io.github.nan8279.smcs.level.generator.Overworld;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class LevelSaver {

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
