package io.github.nan8279.smcs.level.generator.indev_map_themes;

import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.level.generator.Overworld;

import java.util.Random;

public class Hell extends Overworld {

    public Hell() {
        liquid = Block.LAVA;
        grassBlock = Block.DIRT;
    }

    @Override
    protected void generateExtra(Random random, ServerLevel level) {}
}
