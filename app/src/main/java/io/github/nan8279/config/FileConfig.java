package io.github.nan8279.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class FileConfig {
    final private HashMap<String, Object> values = new HashMap<>();

    public void addValue(String name, Object defaultValue) {
        values.put(name, defaultValue);
    }

    public void write(String path) throws IOException {
        StringBuilder config = new StringBuilder();

        for (String name : values.keySet()) {
            config.append(name).append("=").append(values.get(name)).append("\n");
        }

        Files.writeString(Path.of(path), config.toString());
    }

    public HashMap<String, String> read(String path) throws IOException {
        HashMap<String, String> values = new HashMap<>();
        String config = Files.readString(Path.of(path));

        for (String configLine : config.split("\n")) {
            String name = configLine.split("=")[0];
            String value = configLine.split("=")[1];

            values.put(name, value);
        }

        return values;
    }
}
