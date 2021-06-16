package com.spacialnightmare.betterdirections.util;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

// Configuration class
public class Config {

    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.IntValue NODES_PER_CHUNK;
    public static ForgeConfigSpec.IntValue DISTANCE_BETWEEN_NODES;

    static {

        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        nodesPerChunk(SERVER_BUILDER, CLIENT_BUILDER);

        SERVER_CONFIG = SERVER_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }
    // define a config value (description, etc)
    private static void nodesPerChunk(ForgeConfigSpec.Builder SERVER_BUILDER,
                                      ForgeConfigSpec.Builder CLIENT_BUILDER) {
        NODES_PER_CHUNK = CLIENT_BUILDER.comment("How many nodes get generated per chunk, " +
                "the more generated the more accurate the pathing will be.\n Amount of nodes can be: 16, 64, 256. " +
                "Anything else will not work!")
                .defineInRange("nodes_per_chunk", 16, 16, 256);
    }
    // define the Distance between nodes based on the NODES_PER_CHUNK
    public static int DistanceBetweenNodes() {
        if (NODES_PER_CHUNK.get() == 16) {
            return 4;
        } else if (NODES_PER_CHUNK.get() == 64) {
            return 2;
        } else if (NODES_PER_CHUNK.get() == 256) {
            return 1;
        } else {
            throw new IllegalArgumentException("Integer NODES_PER_CHUNK not correctly assigned!");
        }
    }
    // load the config file
    public static void loadConfigFile(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path))
                .sync().autosave().writingMode(WritingMode.REPLACE).build();

        file.load();
        config.setConfig(file);
    }
}
