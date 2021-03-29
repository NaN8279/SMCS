package io.github.nan8279.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class FileConfig {
    final private ArrayList<ConfigLine> lines = new ArrayList<>();

    public void addValue(String name, Object defaultValue) {
        lines.add(new ConfigLine(name, defaultValue));
    }

    public void addValue(String name, Object defaultValue, String comment) {
        lines.add(new CommentLine(comment));
        lines.add(new ConfigLine(name, defaultValue));
    }

    public void write(String path) throws IOException {
        StringBuilder config = new StringBuilder();

        for (ConfigLine line : lines) {
            config.append(line.toString());
        }

        Files.writeString(Path.of(path), config.toString());
    }

    public HashMap<String, Object> read(String path) throws IOException, InvalidConfigException {
        String[] configLines = Files.readString(Path.of(path)).split("\n");
        HashMap<String, Object> config = new HashMap<>();

        for (ConfigLine configLine : lines) {
            if (configLine instanceof CommentLine) {
                continue;
            }
            boolean foundLine = false;

            for (String line : configLines) {
                if (line.startsWith("#")) {
                    continue;
                }

                String name = line.split("=")[0];
                String value = line.split("=")[1];

                if (name.equals(configLine.getName())) {
                    try {
                        if (configLine.getValue() instanceof Boolean) {
                            config.put(name, Boolean.valueOf(value));
                        } else if (configLine.getValue() instanceof Number) {
                            if (configLine.getValue() instanceof Long) {
                                config.put(name, Long.valueOf(value));
                            } else {
                                config.put(name, Integer.valueOf(value));
                            }
                        } else {
                            config.put(name, value);
                        }
                    } catch (Exception exception) {
                        throw new InvalidConfigException();
                    }

                    foundLine = true;
                    break;
                }
            }

            if (!foundLine) {
                throw new InvalidConfigException();
            }
        }

        return config;
    }
}

class ConfigLine {
    final private String name;
    final private Object value;

    ConfigLine(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        String string = value.toString();

        return name + "=" + string + "\n";
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}

class CommentLine extends ConfigLine {
    final private String comment;

    CommentLine(String comment) {
        super(null, null);
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "# "+ comment + "\n";
    }
}
